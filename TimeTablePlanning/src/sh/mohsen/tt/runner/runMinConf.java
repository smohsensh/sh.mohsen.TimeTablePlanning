package sh.mohsen.tt.runner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import sh.mohsen.tt.csp.MinConflictsCSP;
import sh.mohsen.tt.general.Loader;
import sh.mohsen.tt.general.TimeTableModel;

public class runMinConf {

	public static void main(String[] args) throws IOException {
		
	
	File freetimes =new File("freetimes.txt");
	File teacher_num_interval = new File("teacherNum.txt");
	File course_num_interval = new File("courseNum.txt");
	File same_resource = new File("sameResource.txt");
	File same_course = new File("sameCourse.txt");
	File init_data = new File("initData.txt");
	
	Loader l = new Loader(freetimes, teacher_num_interval, course_num_interval, same_resource, same_course,init_data);
	int teachersNum= l.getTeacherNum();
	int intervalsNum = l.getIntervalNum();
	int groupsNum = l.getGroupNum();
	TimeTableModel timetable = new TimeTableModel(teachersNum, intervalsNum, groupsNum);
	MinConflictsCSP mcc =new MinConflictsCSP( l);
	Integer[][] ans = mcc.solve();
	
	File out = new File("csp_" + System.currentTimeMillis()/10000 +"_t_" + teachersNum +"_g_" +groupsNum);
	Writer wr = new FileWriter(out);
//	
//	for (int tea = 0; tea < teachersNum ; tea++) {
//		for (int in = 0; in < intervalsNum; in++) {
//			for (int gr = 0; gr < groupsNum; gr++) {
//				String s = " teacher " + tea + " interval " + in +   ", group " + gr + " =" ;
//				if(ans[tea][in][gr] >0 ){
//					String str = s+ 1;
//					wr.write(str+"\n");
//					System.out.println(str);
//				}
//			}
//		}
//	}
	wr.flush();
	wr.close();
	
}

}