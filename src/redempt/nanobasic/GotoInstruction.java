package redempt.nanobasic;

public class GotoInstruction implements Instruction {
	
	private int line;
	
	public GotoInstruction(int line) {
		this.line = line;
	}
	
	public int getLine() {
		return line;
	}
	
	public void setLine(int line) {
		this.line = line;
	}
	
	@Override
	public void execute(Script script) {
		script.setLine(line);
	}
	
}
