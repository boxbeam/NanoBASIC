package redempt.nanobasic;

public class GotoInstruction implements Instruction {
	
	private int line;
	private boolean sub;
	
	public GotoInstruction(int line, boolean sub) {
		this.line = line;
		this.sub = sub;
	}
	
	public int getLine() {
		return line;
	}
	
	public void setLine(int line) {
		this.line = line;
	}
	
	@Override
	public void execute(Script script) {
		if (sub) {
			script.goSub(line);
		} else {
			script.goTo(line);
		}
	}
	
}
