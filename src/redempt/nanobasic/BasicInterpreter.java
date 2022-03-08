package redempt.nanobasic;

import redempt.nanobasic.expression.ExprToken;
import redempt.nanobasic.expression.LiteralValue;
import redempt.nanobasic.expression.Operation;
import redempt.nanobasic.expression.Operator;
import redempt.nanobasic.expression.RandomValue;
import redempt.nanobasic.expression.Value;
import redempt.nanobasic.expression.Variable;
import redempt.nanobasic.string.ExpressionString;
import redempt.nanobasic.string.LiteralString;
import redempt.nanobasic.string.StringPart;
import redempt.redlex.bnf.BNFParser;
import redempt.redlex.data.Token;
import redempt.redlex.processing.CullStrategy;
import redempt.redlex.processing.Lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.function.BiPredicate;

public class BasicInterpreter {
	
	private static Lexer lexer;
	
	private static Lexer getLexer() {
		if (lexer == null) {
			lexer = BNFParser.createLexer(BasicInterpreter.class.getClassLoader().getResourceAsStream("basic.bnf"));
			lexer.setRetainStringLiterals(false);
			lexer.setRetainEmpty(false);
			lexer.setUnnamedRule(CullStrategy.LIFT_CHILDREN);
			lexer.setRuleByName(CullStrategy.DELETE_ALL, "sep", "comment");
			lexer.setRuleByName(CullStrategy.LIFT_CHILDREN, "term", "value", "line");
		}
		return lexer;
	}
	
	public static Script createScript(String str) {
		Token root = getLexer().tokenize(str);
		return parseScript(root);
	}
	
	private static Script parseScript(Token root) {
		List<GotoInstruction> gotos = new ArrayList<>();
		Map<Integer, Instruction> instructions = parseInstructions(root, gotos);
		Map<Integer, Integer> lines = new HashMap<>();
		int i = 0;
		Instruction[] instructionList = new Instruction[instructions.size()];
		for (Integer lineNum : instructions.keySet()) {
			lines.put(lineNum, i);
			instructionList[i] = instructions.get(lineNum);
			i++;
		}
		for (GotoInstruction instruction : gotos) {
			Integer line = lines.get(instruction.getLine());
			if (line == null) {
				throw new IllegalArgumentException("Illegal goto for line " + instruction.getLine() + ", no line exists with that number");
			}
			instruction.setLine(line);
		}
		return new Script(instructionList);
	}
	
	private static Map<Integer, Instruction> parseInstructions(Token root, List<GotoInstruction> gotos) {
		Map<Integer, Instruction> map = new TreeMap<>();
		for (Token child : root.getChildren()) {
			Token[] children = child.getChildren();
			int lineNum = Integer.parseInt(children[0].getValue());
			Instruction instruction = parseInstruction(children[1], gotos);
			map.put(lineNum, instruction);
		}
		return map;
	}
	
	private static Instruction parseInstruction(Token line, List<GotoInstruction> gotos) {
		switch (line.getType().getName()) {
			case "goto":
			case "gosub":
				int lineNum = Integer.parseInt(line.getChildren()[0].getValue());
				GotoInstruction instruction = new GotoInstruction(lineNum, line.getType().getName().equals("gosub"));
				gotos.add(instruction);
				return instruction;
			case "assign":
				Token[] children = line.getChildren();
				int variable = children[0].getValue().charAt(0) - 'A';
				Value expr = parseExpression(children[1]);
				return s -> s.getVariables()[variable] = expr.getValue(s);
			case "print":
				if (line.getChildren().length == 0) {
					return s -> System.out.println();
				}
				Token child = line.getChildren()[0];
				StringPart[] parts = parseFormatString(child);
				return s -> {
					StringBuilder builder = new StringBuilder();
					for (StringPart part : parts) {
						builder.append(part.getValue(s));
					}
					System.out.println(builder);
				};
			case "if":
				children = line.getChildren();
				Token[] compare = children[0].getChildren();
				BiPredicate<Integer, Integer> comp = getComparator(compare[1].getValue());
				Value a = parseExpression(compare[0]);
				Value b = parseExpression(compare[2]);
				Instruction toRun = parseInstruction(children[1], gotos);
				return s -> {
					if (comp.test(a.getValue(s), b.getValue(s))) {
						toRun.execute(s);
					}
				};
			case "input":
				int varNum = line.getChildren()[0].getValue().charAt(0) - 'A';
				Scanner scanner = new Scanner(System.in);
				return s -> {
					s.getVariables()[varNum] = scanner.nextInt();
				};
			case "end":
				return s -> System.exit(0);
			case "return":
				return Script::ret;
			default:
				return null;
		}
	}
	
	private static StringPart[] parseFormatString(Token formatString) {
		Token[] children = formatString.getChildren();
		StringPart[] parts = new StringPart[children.length];
		for (int i = 0; i < parts.length; i++) {
			Token part = children[i];
			if (part.getType().getName().equals("string")) {
				String str = part.getValue().replace("\\", "");
				str = str.substring(1, str.length() - 1);
				parts[i] = new LiteralString(str);
				continue;
			}
			Value expr = parseExpression(part);
			parts[i] = new ExpressionString(expr);
		}
		return parts;
	}
	
	private static Value parseExpression(Token expr) {
		List<ExprToken> tokens = new ArrayList<>();
		for (Token child : expr.getChildren()) {
			ExprToken token = switch (child.getType().getName()) {
				case "expr" -> parseExpression(child);
				case "num" -> new LiteralValue(Integer.parseInt(child.getValue()));
				case "var" -> parseVariable(child);
				case "op" -> getOperator(child.getValue().charAt(0));
				case "rand" -> new RandomValue(parseExpression(child.getChildren()[0]));
				default -> null;
			};
			tokens.add(token);
		}
		for (int priority : new int[] {2, 1}) {
			for (int i = 0; i < tokens.size(); i++) {
				ExprToken token = tokens.get(i);
				if (!(token instanceof Operator op) || op.getPriority() != priority) {
					continue;
				}
				Value before = (Value) tokens.remove(i - 1);
				i--;
				Value after = (Value) tokens.remove(i + 1);
				Operation operation = new Operation(before, after, op.getOp());
				Value replace = operation.isOnlyLiterals() ? new LiteralValue(operation.getValue(null)) : operation;
				tokens.set(i, replace);
			}
		}
		return (Value) tokens.get(0);
	}
	
	private static Variable parseVariable(Token var) {
		String name = var.getValue();
		char c = name.charAt(name.length() - 1);
		return new Variable(c - 'A', name.startsWith("-"));
	}
	
	private static BiPredicate<Integer, Integer> getComparator(String comp) {
		return switch (comp) {
			case ">" -> (a, b) -> a > b;
			case "<" -> (a, b) -> a < b;
			case ">=" -> (a, b) -> a >= b;
			case "<=" -> (a, b) -> a <= b;
			case "<>" -> (a, b) -> !Objects.equals(a, b);
			case "=" -> Objects::equals;
			default -> null;
		};
	}
	
	private static Operator getOperator(char symbol) {
		return switch (symbol) {
			case '+' -> Operator.ADD;
			case '-' -> Operator.SUBTRACT;
			case '*' -> Operator.MULTIPLY;
			case '/' -> Operator.DIVIDE;
			default -> null;
		};
	}
	
}
