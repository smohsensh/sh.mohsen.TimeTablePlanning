package sh.mohsen.tt.evolutionary;

import java.util.Random;

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

public class ArrayFactory extends AbstractCandidateFactory<byte[]>{
	int tnum, inum, gnum;

	public ArrayFactory(int tnum, int inum, int gnum) {
		super();
		this.tnum = tnum;
		this.inum = inum;
		this.gnum = gnum;
	}

	@Override
	public byte[] generateRandomCandidate(Random rng) {
		byte[] b = new byte[tnum*inum*gnum];
		for (int inter = 0; inter < inum; inter++) {
			for (int gr = 0; gr < gnum; gr++) {
				b[rng.nextInt(tnum)*inum*gnum+ inter*gnum + gr] =1;

			}
		}
		return b;
	}
	

}
