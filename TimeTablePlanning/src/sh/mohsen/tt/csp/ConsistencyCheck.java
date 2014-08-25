package sh.mohsen.tt.csp;

import java.util.ArrayList;

import sh.mohsen.tt.general.Loader;
import sh.mohsen.tt.general.TimeTableModel;

public class ConsistencyCheck {

	TimeTableModel timetable;
	Loader loader;

	public ConsistencyCheck(TimeTableModel timetable, Loader loader) {
		super();
		this.timetable = timetable;
		this.loader = loader;
	}

	public boolean violatesFreeTime() {

		for (int teacher = 0; teacher < timetable.getTeachersNum(); teacher++) {
			ArrayList<Integer> teacherFreeTimes = loader.getFreetimes()[teacher];
			for (int i = 0; i < timetable.getIntervalsNum(); i++) {
				for (int gr = 0; gr < timetable.getGroupsNum(); gr++) {
					if (timetable.getCell(teacher, i, gr)>0 && !teacherFreeTimes.contains(i))
						return true;
				}
			}
		}

		return false;
	}

	public boolean violatesExactlyOneTeacher() {

		for (int i = 0; i < timetable.getIntervalsNum(); i++) {
			for (int gr = 0; gr < timetable.getGroupsNum(); gr++) {
				int teachersInSpecificTime =0;
				for (int teacher = 0; teacher < timetable.getTeachersNum(); teacher++) {
					if(timetable.getCell(teacher, i, gr)>0){
						teachersInSpecificTime++;
						if(teachersInSpecificTime>1)
							return true;
					}
				}
				if(teachersInSpecificTime==0)
					return true;
			}
		}
		return false;
	}
	
	public boolean violateNumberForTeahcers(){
		for (int i = 0; i < timetable.getTeachersNum(); i++) {
			if(timetable.getNumberForTeacher(i) != loader.getNumberForTeacher(i))
				return false;
		}
		return true;
	}
	
	

}
