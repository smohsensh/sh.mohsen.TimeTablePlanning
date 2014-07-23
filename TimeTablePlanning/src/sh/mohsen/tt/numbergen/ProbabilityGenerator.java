package sh.mohsen.tt.numbergen;

import java.util.Random;

import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;

public class ProbabilityGenerator implements NumberGenerator<Probability> {
	Random rnd =  new MersenneTwisterRNG();
	double maxVal;
	
	public ProbabilityGenerator(double maxVal) {
		super();
		this.maxVal = maxVal;
	}

	@Override
	public Probability nextValue() {
		
		return new Probability(rnd.nextDouble()*maxVal);
	}

}
