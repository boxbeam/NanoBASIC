package redempt.nanobasic.expression;

import redempt.nanobasic.Script;

import java.util.function.IntBinaryOperator;

public class Operation implements Value {
	
	private Value val1;
	private Value val2;
	private IntBinaryOperator op;
	
	public Operation(Value val1, Value val2, IntBinaryOperator op) {
		this.op = op;
		this.val1 = val1;
		this.val2 = val2;
	}
	
	public boolean isOnlyLiterals() {
		return val1 instanceof LiteralValue && val2 instanceof LiteralValue;
	}
	
	@Override
	public int getValue(Script script) {
		return op.applyAsInt(val1.getValue(script), val2.getValue(script));
	}
	
}
