package sh.mohsen.tt.evolutionary.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;

public class MyArrayCrossOver extends AbstractCrossover<byte[]>{

	
	private FitnessEvaluator<byte[]> evaluator;
	private int tnum, inum, gnum;
	public MyArrayCrossOver(FitnessEvaluator<byte[]> evaluator,  NumberGenerator<Integer> crossoverPointsVariable, int tnum, int inum , int gnum) {
		super(crossoverPointsVariable);
		this.tnum = tnum;
		this.inum = inum;
		this.gnum = gnum;
		this.evaluator = evaluator;
	}

	@Override
	protected List<byte[]> mate(byte[] parent1, byte[] parent2,
			int numberOfCrossoverPoints, Random rng) {
		
		byte[] ans1 = new byte[parent1.length];
		byte[] ans2 = new byte[parent2.length];
		System.arraycopy(parent1, 0, ans1, 0, parent1.length);
		System.arraycopy(parent2, 0, ans2, 0, parent2.length);
		byte[] tmp = new byte[parent1.length];
		for (int i = 0; i < numberOfCrossoverPoints; i++) {
			int rnd = rng.nextInt(tnum)*gnum*inum;
			System.arraycopy(ans1, 0, tmp,0,rnd);
		    System.arraycopy(ans2, 0, ans1, 0, rnd);
            System.arraycopy(tmp, 0, ans2, 0, rnd);			
		}
        List<byte[]> result = new ArrayList<byte[]>(2);
        
//        if(firstBetterFitness(parent1, ans1) && rng.nextDouble()<0.8)
//        	result.add(parent1);
//        else 
        	result.add(ans1);
        
        if(firstBetterFitness(parent2, ans2) && rng.nextDouble()<0.9)
        	result.add(parent2);
        else 
        	result.add(ans2);
        
        
        
        
		return result;
	}
	
	private boolean firstBetterFitness(byte[] first, byte[] second){
		if(evaluator.isNatural())
			return evaluator.getFitness(first, null) > evaluator.getFitness(second, null);
		else
			return evaluator.getFitness(first, null) < evaluator.getFitness(second, null);
		
	}

}
