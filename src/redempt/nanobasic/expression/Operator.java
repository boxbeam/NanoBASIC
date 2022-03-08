package redempt.nanobasic.expression;

import java.util.function.IntBinaryOperator;

public enum Operator implements ExprToken {
	
	ADD('+', 1, (a, b) -> a + b),
	SUBTRACT('-', 1, (a, b) -> a - b),
	MULTIPLY('*', 2, (a, b) -> a * b),
	DIVIDE('/', 2, (a, b) -> a / b);
	
	private char symbol;
	private int priority;
	private IntBinaryOperator op;
	
	Operator(char symbol, int priority, IntBinaryOperator op) {
		this.symbol = symbol;
		this.priority = priority;
		this.op = op;
	}
	
	public char getSymbol() {
		return symbol;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public IntBinaryOperator getOp() {
		return op;
	}
	
}
