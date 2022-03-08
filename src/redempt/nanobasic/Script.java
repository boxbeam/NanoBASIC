package redempt.nanobasic;

import java.util.Stack;

public class Script {
	
	private int[] vars = new int[26];
	private int line;
	private Instruction[] instructions;
	private Stack<Integer> stack = new Stack<>();
	
	public Script(Instruction[] instructions) {
		this.instructions = instructions;
	}
	
	public int getLine() {
		return line;
	}
	
	public void goTo(int line) {
		this.line = line - 1;
	}
	
	public void goSub(int line) {
		stack.add(this.line);
		goTo(line);
	}
	
	public void ret() {
		goTo(stack.pop() + 1);
	}
	
	public int[] getVariables() {
		return vars;
	}
	
	public void run() {
		line = 0;
		while (line < instructions.length) {
			instructions[line].execute(this);
			line++;
		}
	}
	
}
