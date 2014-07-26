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
		if (rng.nextDouble() > p.doubleValue())
			return selectedCandidates;
		List<byte[]> ans = new ArrayList<byte[]>(selectedCandidates.size());
		for (byte[] bytes : selectedCandidates) {
			ans.add(mutate(bytes, rng));
		}
		return ans;
	}

	private byte[] mutate(byte[] in, Random rng) {
		byte[] arr = new byte[in.length];
		System.arraycopy(in, 0, arr, 0, arr.length);

		if (rng.nextDouble() < 0.4) {
			return shuffleTeachers(arr, rng);
		}

		if (rng.nextDouble() < 0.3)
			shuffleIntevals(arr, rng);

		int numOfTeachersToMutate = rng.nextInt(11);
		for (int i = 0; i < numOfTeachersToMutate; i++) {
			int index = rng.nextInt(inum * gnum * tnum);
			if (rng.nextBoolean())
				arr[index] = 1;
			else
				arr[index] = 0;
		}

		// int teacher = rng.nextInt(tnum);
		// int numberOfOnes = 0;
		// for (int j = 0; j < inum*gnum; j++) {
		// if(in[teacher*inum*gnum+j]==1)
		// numberOfOnes++;
		// in[teacher*(inum*gnum)+j] = 0;
		// }
		// for (int j = 0; j < numberOfOnes +(rng.nextInt(6)-2); j++) {
		// in[teacher*(inum*gnum) + rng.nextInt(inum*gnum)] = 1;
		// }
		return arr;
	}

	private byte[] shuffleTeachers(byte[] in, Random rng) {
		int firstTeacher = rng.nextInt(tnum);
		int secondTeacher = rng.nextInt(tnum);
		byte[] temp = new byte[tnum * gnum * inum];
		System.arraycopy(in, firstTeacher, temp, 0, inum * gnum);
		System.arraycopy(in, secondTeacher, temp, inum * gnum, inum * gnum);
		System.arraycopy(temp, 0, in, secondTeacher, inum * gnum);
		System.arraycopy(temp, inum * gnum, in, firstTeacher, inum * gnum);

		return in;

	}

	private byte[] shuffleIntevals(byte[] in, Random rng) {
		int numberOfDispalces = rng.nextInt(5);

		for (int j = 0; j < numberOfDispalces; j++) {

			int firstTeacher = rng.nextInt(tnum);
			int secondTeacher = rng.nextInt(tnum);
			int firstIndex = 0;
			int secondIndex = 0;
			for (int i = 0; i < inum * gnum; i++) {
				if (in[firstTeacher * inum * gnum + i] == 1
						&& rng.nextDouble() * i < 0.8)
					firstIndex = i;
			}
			for (int i = 0; i < inum * gnum; i++) {
				if (in[secondTeacher * inum * gnum + i] == 1
						&& rng.nextDouble() < 0.5)
					secondIndex = i;
			}

			in[firstTeacher * inum * gnum + firstIndex] = 0;
			in[firstTeacher * inum * gnum + secondIndex] = 1;
			in[secondTeacher * inum * gnum + secondIndex] = 0;
			in[secondTeacher * inum * gnum + firstIndex] = 1;

		}
		return in;

	}

}
