package redempt.nanobasic.expression;

import redempt.nanobasic.Script;

public class Variable implements Value {
	
	private int index;
	private boolean negative;
	
	public Variable(int index, boolean negative) {
		this.index = index;
	}
	
	@Override
	public int getValue(Script script) {
		return (negative ? -1 : 1) * script.getVariables()[index];
	}
	
}
