package sh.mohsen.tt.evolutionary.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import sh.mohsen.tt.numbergen.ProbabilityGenerator;

public class ArrayMutation implements EvolutionaryOperator<byte[]> {

	int tnum, inum, gnum;
	Probability p;
	ProbabilityGenerator gen = new ProbabilityGenerator(1);
	
	public ArrayMutation(int tnum, int inum, int gnum, Probability p) {
		super();
		this.tnum = tnum;
		this.inum = inum;
		this.gnum = gnum;
		this.p = p;
		
	}

	@Override
	public List<byte[]> apply(List<byte[]> selectedCandidates, Random rng) {
		if(rng.nextDouble() > p.doubleValue())
			return selectedCandidates;
		List <byte[]>  ans = new ArrayList<byte[]>(selectedCandidates.size());
		for (byte[] bytes : selectedCandidates) {
			ans.add(mutate(bytes, rng));
		}
		return ans;
	}
	
	private byte[] mutate(byte[] in , Random rng){
//		if(gen.nextValue().compareTo(p)>1)
//			return in;
		int numOfTeachersToMutate = 1;
		for (int i = 0; i < numOfTeachersToMutate; i++) {
			int teacher = rng.nextInt(tnum);
//			int index= rng.nextInt(inum*gnum*tnum) ;
//			if(rng.nextBoolean())
//				in[index  ] =1;
//			else
//				in[index] =0;
			
			
			int numberOfOnes = 0;
			for (int j = 0; j < inum*gnum; j++) {
				if(in[teacher*inum*gnum+j]==1)
					numberOfOnes++;
				in[teacher*(inum*gnum)+j] = 0;
			}
			for (int j = 0; j < numberOfOnes +(rng.nextInt(6)-2); j++) {
				in[teacher*(inum*gnum) + rng.nextInt(inum*gnum)] = 1;
			}
		}
		return in;
	}

}
