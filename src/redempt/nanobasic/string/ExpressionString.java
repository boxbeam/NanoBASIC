package redempt.nanobasic.string;

import redempt.nanobasic.Script;
import redempt.nanobasic.expression.Value;

public class ExpressionString implements StringPart {
	
	private Value value;
	
	public ExpressionString(Value value) {
		this.value = value;
	}
	
	@Override
	public String getValue(Script script) {
		return value.getValue(script) + "";
	}
	
}
