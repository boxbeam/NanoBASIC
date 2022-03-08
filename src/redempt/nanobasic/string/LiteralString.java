package redempt.nanobasic.string;

import redempt.nanobasic.Script;

public class LiteralString implements StringPart {
	
	private String str;
	
	public LiteralString(String str) {
		this.str = str;
	}
	
	@Override
	public String getValue(Script script) {
		return str;
	}
	
}
