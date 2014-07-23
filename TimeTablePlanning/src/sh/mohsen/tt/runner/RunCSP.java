package sh.mohsen.tt.runner;



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import sh.mohsen.tt.csp.CSPSolver;
import sh.mohsen.tt.general.Loader;

public class RunCSP {

	public static void main(String[] args) throws IOException {
		int timeoutMin=0;
		if(args.length>0)
			timeoutMin = Integer.valueOf(args[0]);
		
		File freetimes =new File("freetimes.txt");
		File teacher_num_interval = new File("teacherNum.txt");
		File course_num_interval = new File("courseNum.txt");
		File same_resource = new File("sameResource.txt");
		File same_course = new File("sameCourse.txt");
		File init_data = new File("initData.txt");
		
		Loader l = new Loader(freetimes, teacher_num_interval, course_num_interval, same_resource, same_course,init_data);
		int teachernum= l.getTeacherNum();
		int intervalsNum = l.getIntervalNum();
		int groupsNum = l.getGroupNum();
		
		CSPSolver tb =new CSPSolver(l, teachernum, intervalsNum, groupsNum,timeoutMin);
		
		File out = new File("csp_" + System.currentTimeMillis()/10000 +"_t_" + teachernum +"_g_" +groupsNum);
		Writer wr = new FileWriter(out);
		for (int tea = 0; tea < tb.getVars().length; tea++) {
			for (int in = 0; in < tb.getVars()[tea].length; in++) {
				for (int gr = 0; gr < tb.getVars()[tea][in].length; gr++) {
//					System.out.print( tb.vars[tea][in][gr].value() + " ,");
					String s = " teacher " + tea + " interval " + in +   ", group " + gr + " =" ;
					if(tb.getVars()[tea][in][gr].value() >0 ){
						
						String str = s+ tb.getVars()[tea][in][gr].value();
						wr.write(str+"\n");
						System.out.println(str);
					}
				}
			}
		}
		wr.flush();
		wr.close();
		
	}
}
