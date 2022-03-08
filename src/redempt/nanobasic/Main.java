package redempt.nanobasic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
	
	public static void main(String[] args) throws IOException {
		Path path = Paths.get(args[0]);
		String contents = Files.readString(path);
		try {
			Script script = BasicInterpreter.createScript(contents);
			script.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
