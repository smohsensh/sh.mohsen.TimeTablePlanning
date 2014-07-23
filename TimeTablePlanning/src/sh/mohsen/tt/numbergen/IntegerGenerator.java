package sh.mohsen.tt.numbergen;

import java.util.Random;

import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.MersenneTwisterRNG;

public class IntegerGenerator implements NumberGenerator<Integer> {

	int maxVal;
	
	Random rnd =  new MersenneTwisterRNG();
	
	
	public IntegerGenerator(int intervalsize) {
		super();
		this.maxVal = intervalsize;
	}
	@Override
	public Integer nextValue() {
		return rnd.nextInt(maxVal);
	}
	

}
