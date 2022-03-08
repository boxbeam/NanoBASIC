package redempt.nanobasic;

public class Script {
	
	private int[] vars = new int[26];
	private int line;
	private Instruction[] instructions;
	
	public Script(Instruction[] instructions) {
		this.instructions = instructions;
	}
	
	public int getLine() {
		return line;
	}
	
	public void setLine(int line) {
		this.line = line - 1;
	}
	
	public int[] getVariables() {
		return vars;
	}
	
	public void run() {
		line = 0;
		while (line < instructions.length) {
			instructions[line].execute(this);
			line++;
		}
	}
	
}
