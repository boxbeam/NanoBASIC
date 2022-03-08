package redempt.nanobasic.expression;

import redempt.nanobasic.Script;

public class LiteralValue implements Value {
	
	private int value;
	
	public LiteralValue(int value) {
		this.value = value;
	}
	
	@Override
	public int getValue(Script script) {
		return value;
	}
	
}
