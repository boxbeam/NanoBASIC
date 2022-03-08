package redempt.nanobasic.expression;

import redempt.nanobasic.Script;

import java.util.Random;

public class RandomValue implements Value {
	
	private Random random = new Random(System.currentTimeMillis());
	
	private Value bound;
	
	public RandomValue(Value bound) {
		this.bound = bound;
	}
	
	@Override
	public int getValue(Script script) {
		return random.nextInt(bound.getValue(script));
	}
	
}
