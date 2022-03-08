package redempt.nanobasic.expression;

import redempt.nanobasic.Script;

public class Variable implements Value {
	
	private int index;
	
	public Variable(int index) {
		this.index = index;
	}
	
	@Override
	public int getValue(Script script) {
		return script.getVariables()[index];
	}
	
}
