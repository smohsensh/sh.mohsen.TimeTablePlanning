package sh.mohsen.tt.csp;

import java.util.ArrayList;

import org.jacop.constraints.AndBool;
import org.jacop.constraints.Constraint;
import org.jacop.constraints.Count;
import org.jacop.constraints.Sum;
import org.jacop.core.BooleanVar;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleSelect;
import org.jacop.search.SmallestDomain;

import sh.mohsen.tt.general.Loader;
import sh.mohsen.tt.general.TeacherPair;

public class CSPSolver {

	Store store = new Store();
	BooleanVar zero = new BooleanVar(store, "zero", 0, 0);
	
	IntVar costValue;
	final int teacherNum ;
	final int intervalsNum ;
	final int groupsNum;
	
	int timeoutMinutes ;

	int[] numOfIntervalEachTeacher ;

	private BooleanVar[][][] vars ;

	
	private int[] howManyIntervalsForCourse ;
	private ArrayList<Integer>[] freetimes ;

	//	private Map<TimeInterval, Integer> intevalToNum = new HashMap<TimeInterval, Integer>();
	private ArrayList<TeacherPair> sameCourse = new ArrayList<TeacherPair>();
	private ArrayList<TeacherPair> sameResource = new ArrayList<TeacherPair>();

	public CSPSolver(Loader l, int teacherNum, int intervalsNum, int groupsNum , int timeout) {
		super();
		
		
		this.teacherNum = teacherNum;
		this.intervalsNum=intervalsNum;
		this.groupsNum = groupsNum;
		this.timeoutMinutes = timeout;
		
		numOfIntervalEachTeacher = new int[teacherNum];
		setVars(new BooleanVar[teacherNum][intervalsNum][groupsNum]);
		howManyIntervalsForCourse = new int[teacherNum];

			
		this.numOfIntervalEachTeacher = l.load_number_of_interval_for_teachers();
		this.howManyIntervalsForCourse = l.loadNumber_for_cousers();
		this.freetimes = 	l.loadFreeTimes();
		this.sameCourse = l.loadSameCourse(); 
		this.sameResource = l.loadSameResource();
	
		
		/**
		 * the if-else down here assures 'b' constraints in doc
		 */
		for (int teacher = 0; teacher < teacherNum; teacher++) {
			for (int interval = 0; interval < intervalsNum; interval++) {
				for (int group = 0; group < groupsNum; group++) {
					String name = "t" + teacher + " c" + group + " interval"
							+ interval;

					if (freetimes[teacher].contains(interval))
						getVars()[teacher][interval][group] = new BooleanVar(store,
								name);
					else
						getVars()[teacher][interval][group] = new BooleanVar(store,
								name, 0, 0);

				}
			}
		}
		oneClass_atATime_atMost();
		addNonEmptyIntervalConstraint();
		add_if_one_interval_then_all_for_group();
		true_num_of_intervals_for_teacher();
		no_teacher_of_same_course();
		addConstraint_sameResource();
		
		
		//SEARCH FOR ANSWER
		
		Search<BooleanVar> res = new DepthFirstSearch<BooleanVar>();
		
		if(timeoutMinutes>0)
			res.setTimeOut(timeoutMinutes*60);
		
		ArrayList<BooleanVar> oneDvars = new ArrayList<BooleanVar>(teacherNum*intervalsNum*groupsNum);
		
		for (int i = 0; i < getVars().length; i++) {
			for (int j = 0; j <getVars()[i].length; j++) {
				for (int k = 0; k < getVars()[i][j].length; k++) {
					oneDvars.add(getVars()[i][j][k]);
				}
			}
		}
		BooleanVar [] shit = new BooleanVar[teacherNum*intervalsNum*groupsNum];
		for (int i = 0; i < shit.length; i++) {
			shit[i] = oneDvars.get(i);
		}
		IntVar cost = new IntVar(store, 0, 100); 
		SelectChoicePoint<BooleanVar> scp = new SimpleSelect<BooleanVar>(shit, new SmallestDomain<BooleanVar>(), new IndomainMin<BooleanVar>());
		boolean result = res.labeling(store, scp, cost);
		System.out.println(result);
		

	}// end method

	
	public String getTeacherTimes(int teacher){
		String s ="";
		for (int interval = 0; interval < intervalsNum; interval++) {
			s += "interval " + interval;
			for (int group = 0; group < groupsNum; group++) {
				if(getVars()[teacher][interval][group].value() >0)
					s += "group " + group;
			}
			s += "\n";
		}
		return s;
	}
	
	@Override
	public String toString() {
		
		String s = "";
		for (int i = 0; i < groupsNum; i++) {
			
		}
		
		return s;

	}
	
	/**
	 * Add a constraint that for a teacher and a group sum of different
	 * intervals is zero or a specified number which means if a teacher, teaches
	 * a course for a group it should be completely thought by him
	 * 
	 * Constraint 'heh' in doc
	 */

	private void add_if_one_interval_then_all_for_group() {
		for (int teacher = 0; teacher < teacherNum; teacher++) {
			for (int group = 0; group < groupsNum; group++) {
				
				// domain of sum is {0, howManyIntervalsForCourse[teacher]}
				IntVar sum = new IntVar(store, 0, 0);
				sum.addDom(howManyIntervalsForCourse[teacher],
						howManyIntervalsForCourse[teacher]);
				
				Constraint noneOrAll = new Sum(getIntervalsForTeacherAndGroup(
						teacher, group), sum);
				store.impose(noneOrAll);
			}
		}
	}

	/**
	 * 
	 * @param teacher
	 * @param group
	 * @return intervalsForTeacherAndGroup an array of Boolean var containing
	 *         all intervals belonging to a teacher and group specified by @param
	 *         teacher and @param group
	 * 
	 */
	private BooleanVar[] getIntervalsForTeacherAndGroup(int teacher, int group) {
		BooleanVar[] intervalsForTeacherAndGroup = new BooleanVar[intervalsNum];
		for (int interval = 0; interval < intervalsForTeacherAndGroup.length; interval++) {
			intervalsForTeacherAndGroup[interval] = getVars()[teacher][interval][group];
		}
		return intervalsForTeacherAndGroup;
	}

	/**
	 * making sure no interval of for any class is left without assigning a
	 * teacher
	 * 
	 * constraint 'a' in doc
	 */
	private void addNonEmptyIntervalConstraint() {
		IntVar sum = new IntVar(store, 1, 1);
		for (int interval = 0; interval < intervalsNum; interval++) {
			for (int group = 0; group < groupsNum; group++) {
				Constraint c = new Sum(
						getColumnOfSpecificTime(interval, group), sum);
				store.impose(c);
			}
		}
	}

	private IntVar[] getColumnOfSpecificTime(int interval, int group) {
		BooleanVar[] SpecificTimeForAllTeachers = new BooleanVar[teacherNum];
		for (int i = 0; i < teacherNum; i++) {
			SpecificTimeForAllTeachers[i] = getVars()[i][interval][group];
		}
		return SpecificTimeForAllTeachers;
	}

	/**
	 * Trivial Constraint making sure that no teacher is assigned to hold more
	 * that one class in the same time. In Other Words for a given time and
	 * teacher the group array contains one 1 at most
	 * 
	 * constraint 'd' in doc
	 */
	private void oneClass_atATime_atMost() {
		for (int teacher = 0; teacher < teacherNum; teacher++) {
			for (int interval = 0; interval < intervalsNum; interval++) {
				IntVar boundToOne = new IntVar(store, 0, 1);
				Constraint trivialCons = new Count(getVars()[teacher][interval], boundToOne, 1);
				store.impose(trivialCons);
			}
		}
	}

	/**
	 * Constraint j in doc
	 */
	private void true_num_of_intervals_for_teacher() {
		for (int teacher = 0; teacher < teacherNum; teacher++) {
			IntVar sum = new IntVar(store, numOfIntervalEachTeacher[teacher],
					numOfIntervalEachTeacher[teacher]);
			// ArrayList<BooleanVar> tmp = new
			// ArrayList<BooleanVar>(intervalsNum*groupsNum);
			// for (int interval = 0; interval < intervalsNum; interval++) {
			// for (int group = 0; group < groupsNum; group++) {
			// tmp.add(vars[teacher][interval][group]);
			// }
			// }
			Constraint trueNumberTeacher = new Sum(getTeachersVars(teacher),
					sum);
			store.impose(trueNumberTeacher);
		}
	}

	private void addConstraint_sameResource() {
		BooleanVar[ ] tmp = new BooleanVar[2];
		for (TeacherPair tp : sameResource) {
			for (int group = 0; group < groupsNum; group++) {
				for (int interval = 0; interval < intervalsNum;interval++) {
					tmp[0] = getVars()[tp.first()][interval][group];
					tmp[1] = getVars()[tp.second()][interval][group];
					Constraint c = new AndBool(tmp, zero);
					store.impose(c);
				}
			}
		}
	}
	
	

	/**
	 * Confirming no teachers of same course has been assigned to a single group
	 * constraint z in doc
	 */
	private void no_teacher_of_same_course() {

		for (TeacherPair tp : sameCourse) {
			for (int group = 0; group < groupsNum; group++) {
				for (BooleanVar firstTime : getTeacherGroupVars(tp.first(), group)) {
					BooleanVar[] tmp = new BooleanVar[2];
					tmp[0] = firstTime;
					for (BooleanVar secTime : getTeacherGroupVars(tp.second(), group)) {
						tmp[1] = secTime;
						Constraint c = new AndBool(tmp, zero);
						c.impose(store);
					} // t2
				} //t1
			} //group for
		}// pairs for
	} // end method

	
	
	private ArrayList<BooleanVar> getTeacherGroupVars(int teacher_id,
			int group_id) {
		ArrayList<BooleanVar> ans = new ArrayList<BooleanVar>(intervalsNum);
		for (int interval = 0; interval < intervalsNum; interval++) {
			ans.add(getVars()[teacher_id][interval][group_id]);
		}
		return ans;

	}

	private ArrayList<BooleanVar> getTeachersVars(int teacher_id) {
		ArrayList<BooleanVar> ans = new ArrayList<BooleanVar>(groupsNum
				* intervalsNum);
		for (int interval = 0; interval < intervalsNum; interval++) {
			for (int group = 0; group < groupsNum; group++) {
				ans.add(getVars()[teacher_id][interval][group]);
			}
		}
		return ans;
	}

	
	public ArrayList<Integer>[] getFreetimes() {
		return freetimes;
	}


	public BooleanVar[][][] getVars() {
		return vars;
	}


	public void setVars(BooleanVar[][][] vars) {
		this.vars = vars;
	}

	

}
