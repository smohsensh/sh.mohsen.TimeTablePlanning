package sh.mohsen.tt.general;

import java.util.ArrayList;

public class TimeTableModel {

	private byte[][][] table;
	private int teachersNum, intervalsNum, groupsNum;
	private int[] numberForTeachers;
	public int[] getNumberForTeachers() {
		return numberForTeachers;
	}


	public ArrayList<TimeInterval>[] getTeacherIntervals() {
		return teacherIntervals;
	}


	private ArrayList<TimeInterval>[] teacherIntervals;
	
	
	@SuppressWarnings("unchecked")
	public TimeTableModel(int teachersNum, int intervalsNum, int groupsNum) {
		super();
		this.teachersNum = teachersNum;
		this.intervalsNum = intervalsNum;
		this.groupsNum = groupsNum;
		table = new byte[teachersNum][intervalsNum][groupsNum];
		numberForTeachers = new int[teachersNum];
		teacherIntervals = new ArrayList[teachersNum];
		for (int i = 0; i < teacherIntervals.length; i++) {
			teacherIntervals[i] = new ArrayList<TimeInterval>();
		}
	}
	
	
	public int getNumberForTeacher(int teacher){
		return numberForTeachers[teacher];
	}
	public ArrayList<TimeInterval> getTeacherTimes(int teacher){
		return teacherIntervals[teacher];
	}
	
	
	public void setCell(int teacher, int interval, int group){

		if(table[teacher][interval][group] == 0){
			numberForTeachers[teacher]++;
			teacherIntervals[teacher].add(new TimeInterval(interval, group));
		}
		
		table[teacher][interval][group] = 1;
	}
	public void unsetCell(int teacher, int interval, int group){
		if(table[teacher][interval][group] == 1){
			numberForTeachers[teacher]--;
			teacherIntervals[teacher].remove(new TimeInterval(interval, group));
		}
		table[teacher][interval][group] = 0;
	
	}
	
	
	public byte getCell(int teacher, int interval, int group){
		return table[teacher][interval][group] ;
	}

	public byte[][][] getTable() {
		return table;
	}


	public int getTeachersNum() {
		return teachersNum;
	}


	public int getIntervalsNum() {
		return intervalsNum;
	}


	public int getGroupsNum() {
		return groupsNum;
	}
	
	
	
	
	
	
}
