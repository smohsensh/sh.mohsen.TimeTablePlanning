package sh.mohsen.tt.evolutionary;

import java.util.List;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

import sh.mohsen.tt.general.Loader;
import sh.mohsen.tt.general.TeacherPair;


public class ArrayEvaluator  implements FitnessEvaluator<byte[]>{

	Loader loader;
	int tnum, inum , gnum;
	byte[][][] humanly;
	public ArrayEvaluator(int tnum, int inum, int gnum, Loader l ) {
		super();
		this.tnum = tnum;
		this.inum = inum;
		this.gnum = gnum;
		this.loader =l;
	}
	
	public double getFitness(byte[] candidate) {
		return getFitness(candidate, null);
	}
	
	@Override
	public double getFitness(byte[] candidate, List<? extends byte[]> population) {
		double fine = 1;
		int numOfOnes = covnertHumanly(candidate);
		
		if(numOfOnes > gnum*inum)
			fine+= Math.abs(numOfOnes -gnum*inum)*8000;
		
		
		
		//check that only one teacher is assigned to a specific group-interval
		// and free times for teachers are filled 
		for (int group = 0; group < gnum; group++) {
			for (int inter = 0; inter < inum; inter++) {
				int teachersINspecificTime =0; //shows how many teachers are assigned to one interval-group
				for (int teacher = 0; teacher < tnum; teacher++) {
					if(humanly[teacher][inter][group]>0){
						teachersINspecificTime++;
						if(!loader.getFreetimes()[teacher].contains(inter))
							//fine for teacher 
							fine++;
					}
				}
				if(teachersINspecificTime >1)
					fine+= 1000*teachersINspecificTime;
				if(teachersINspecificTime <1)
					fine+= 1000;
			}
		}
		
		
		// true number of intervals are assigned to each teacher ('jim' in doc)
		// and no teacher assigned same interval for different groups ('d' in doc)
		for (int teacher = 0; teacher < tnum; teacher++) {
			int sumForTeach =0;
			for (int inter = 0; inter < inum; inter++) {
				int sumOfsameGroupInterval=0;
				for (int gr = 0; gr < gnum; gr++) {
					if(humanly[teacher][inter][gr]==1 ){
						sumForTeach++;
						sumOfsameGroupInterval++;
					}
				}
				
				// teacher assigned to more than one group in same time
				if(sumOfsameGroupInterval > 1)
					fine+= 8500;
			}
			if(sumForTeach != loader.getNumberForTeacher(teacher)){
				if(sumForTeach % loader.getNumberForCourse(teacher)==0)
					fine += 2;
				else
					fine+= 20;
			}
		}
		
		// heh in doc
		for (int teacher = 0; teacher < tnum; teacher++) {
			for (int gr = 0; gr < gnum; gr++) {
				int sumOfIntervalsForTeacerAndGroup = getSumofIntervalForTeacherAndGroup(
						teacher, gr);
				if( sumOfIntervalsForTeacerAndGroup !=0 
						&& sumOfIntervalsForTeacerAndGroup%loader.getNumberForCourse(teacher)!=0){
					fine += 20;
				}
			}
		}
		
		for (TeacherPair tp : loader.getSameCourse()) {
			for (int gr = 0; gr < gnum; gr++) {
				if(getSumofIntervalForTeacherAndGroup(tp.first(), gr) >0 
						&& getSumofIntervalForTeacherAndGroup(tp.second(), gr)> 0){
					fine+=20;
				}
			}
		}
		
		for (TeacherPair tp : loader.getSameResource()) {
			for (int gr = 0; gr < gnum; gr++) {
				if(getSumofIntervalForTeacherAndGroup(tp.first(), gr) >0 
						&& getSumofIntervalForTeacherAndGroup(tp.second(), gr)> 0){
					fine+=12;
				}
			}
		}
		return fine;
	}

	private int covnertHumanly(byte[] candidate) {
		humanly = new byte[tnum][inum][gnum];
		
		if(candidate.length != tnum*inum*gnum)
			try {
				throw new Exception("wtf?");
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		int numOfOnes=0;
		for (int i = 0; i < tnum; i++) {
			for (int j = 0; j < inum; j++) {
				for (int k = 0; k < gnum; k++) {
					byte x = candidate[i*(inum*gnum) + j*gnum + k];
					humanly[i][j][k] =x;
					if(x==1)
						numOfOnes++;
	 			}
			}
		}
		return numOfOnes;
	}
	
	
	private int getSumofIntervalForTeacherAndGroup(int teacher, int gr) {
		int sumOfIntervalsForTeacerAndGroup =0;
		for (int inter = 0; inter < inum; inter++) {
			if(humanly[teacher][inter][gr] ==1)
				sumOfIntervalsForTeacerAndGroup ++;
		}
		return sumOfIntervalsForTeacerAndGroup;
	}
	
	@Override
	public boolean isNatural() {
		return false;
	}
	
}
