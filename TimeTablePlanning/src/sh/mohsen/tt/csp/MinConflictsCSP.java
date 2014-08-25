package sh.mohsen.tt.csp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.uncommons.maths.random.MersenneTwisterRNG;

import sh.mohsen.tt.general.Loader;
import sh.mohsen.tt.general.TeacherPair;
import sh.mohsen.tt.general.TimeTableModel;

public class MinConflictsCSP {
	
	int tnum , inum ,gnum, size;
	Integer[][] humanly;
	Loader  loader;
	Random rng = new MersenneTwisterRNG();
	int[][] sumOfIntervalTeacherGroup;
	
	public Integer[][] solve(){
		int previousConflicts = getConfilictsNumber(humanly);
		while(true){
			System.out.println("confs = " +previousConflicts );
			if(previousConflicts==0)
				return humanly;
			int tIndex= rng.nextInt(tnum);
			int iIndex= rng.nextInt(inum);
			int gIndex= rng.nextInt(gnum);
			int lastValue = humanly[iIndex][gIndex];
			
			int bestTeacher=0;
			for (int teach = 0; teach < tnum; teach++) {
				
				humanly[iIndex][gIndex] = teach;
				int conf1 = getConfilictsNumber(humanly);
				if(conf1 < previousConflicts){
					previousConflicts = conf1;
					bestTeacher  = teach;
				}
			}
			humanly[iIndex][gIndex] = bestTeacher;
		}
	}
	
	
	
	public MinConflictsCSP(Loader loader) {
		this.tnum = loader.getTeacherNum();
		this.inum = loader.getIntervalNum();
		this.gnum = loader.getGroupNum();
		this.size = tnum*inum*gnum;
		humanly = new Integer[inum][gnum];
//		for (int group = 0; group < gnum; group++) {
//			for (int inter = 0; inter < inum; inter++) {
//				for (int teacher = 0; teacher < tnum; teacher++) {
//					humanly[teacher][inter][group]=1;
//				}
//			}
//		}
		this.loader = loader;
		
	}
	
	
	
	

	public int getConfilictsNumber(Integer[][] humanly) {
		int conflicts = 0;
//		int numOfOnes = 0;
//		
//		
//		if(numOfOnes > gnum*inum)
//			conflicts+= 2;
		
		
		
		//check that only one teacher is assigned to a specific group-interval
		// and free times for teachers are filled
		int nullTimes=0;
		for (int group = 0; group < gnum; group++) {
			for (int inter = 0; inter < inum; inter++) {
				if(humanly[inter][group] == null)
					nullTimes ++;
			}
		}
		conflicts +=nullTimes;
			
		
		// true number of intervals are assigned to each teacher ('jim' in doc)
		// and no teacher assigned same interval for different groups ('d' in doc)
		int[]numberOfIntervalsForTeacher  = new int[tnum];
			for (int inter = 0; inter < inum; inter++) {
				ArrayList<Integer> teachersInThisInterval = new ArrayList<Integer>();
				for (int gr = 0; gr < gnum; gr++) {
					int t = humanly[inter][gr];
					numberOfIntervalsForTeacher[t] ++; 
					if(teachersInThisInterval.contains(t))
							conflicts ++;
					else
						teachersInThisInterval.add(t);
				}
			}

			for (int teacher = 0; teacher < tnum; teacher++) {
				
				if(numberOfIntervalsForTeacher[teacher] != loader.getNumberForTeacher(teacher)){
						conflicts+= 1;
				}
			}
		
		// heh in doc
		for (int teacher = 0; teacher < tnum; teacher++) {
			for (int gr = 0; gr < gnum; gr++) {
				int sumOfIntervalsForTeacerAndGroup = getSumofIntervalForTeacherAndGroup(
						teacher, gr);
				if( sumOfIntervalsForTeacerAndGroup !=0 
						&& sumOfIntervalsForTeacerAndGroup%loader.getNumberForCourse(teacher)!=0){
					conflicts += 1;
				}
			}
		}
		
		for (TeacherPair tp : loader.getSameCourse()) {
			for (int gr = 0; gr < gnum; gr++) {
				if(getSumofIntervalForTeacherAndGroup(tp.first(), gr) >0 
						&& getSumofIntervalForTeacherAndGroup(tp.second(), gr)> 0){
					conflicts+=1;
				}
			}
		}
		
		for (TeacherPair tp : loader.getSameResource()) {
			for (int gr = 0; gr < gnum; gr++) {
				if(getSumofIntervalForTeacherAndGroup(tp.first(), gr) >0 
						&& getSumofIntervalForTeacherAndGroup(tp.second(), gr)> 0){
					conflicts+=1;
				}
			}
		}
		return conflicts;
	}
	
	
	private int getSumofIntervalForTeacherAndGroup(int teacher, int gr) {
		
		int sumOfIntervalsForTeacerAndGroup =0;
		for (int inter = 0; inter < inum; inter++) {
			if(humanly[inter][gr] == teacher)
				sumOfIntervalsForTeacerAndGroup ++;
		}
		return sumOfIntervalsForTeacerAndGroup;
	}
}
