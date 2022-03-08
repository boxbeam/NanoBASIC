package redempt.nanobasic.expression;

import redempt.nanobasic.Script;

public interface Value extends ExprToken {

	public int getValue(Script script);
	
}
