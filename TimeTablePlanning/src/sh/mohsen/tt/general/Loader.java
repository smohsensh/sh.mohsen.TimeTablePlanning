package sh.mohsen.tt.general;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Loader {

	
	private File freetimesFile ;
	private File teacher_num_interval ;
	private File course_num_interval ;
	private File same_resource ;
	private File same_course ;
	private File init_data;
	
	
	private int teacherNum ;
	

	private int intervalNum;
	private int groupNum;
	
	private int[] numberForTeachers, numberForCourses;
	private ArrayList<Integer> [] freetimes;
	private ArrayList<TeacherPair> sameCourse, sameResource;
	

	
	public Loader(File freetimesFile, File teacher_num_interval,
			File course_num_interval, File same_resource, File same_course,
			File initData) {
		super();
		this.freetimesFile = freetimesFile;
		this.teacher_num_interval = teacher_num_interval;
		this.course_num_interval = course_num_interval;
		this.same_resource = same_resource;
		this.same_course = same_course;
		this.init_data=initData;
		loadInitData();
		
	}

	private void loadInitData(){
		Scanner sc = null;
		try {
			sc = new Scanner(init_data);
		} catch (FileNotFoundException e) {
			System.out.println("init data load failed");
			e.printStackTrace();
		}
		teacherNum = sc.nextInt();
		intervalNum = sc.nextInt();
		groupNum = sc.nextInt();
	}
	
	@SuppressWarnings("resource")
	public  int[] load_number_of_interval_for_teachers (File num_inteval_teacher_file){
		
		int[] numOfIntervalEachTeacher = new int[teacherNum];
		Scanner sc = null;
		try {
			sc = new Scanner(num_inteval_teacher_file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < teacherNum; i++) {
			numOfIntervalEachTeacher[i] = sc.nextInt();
		}

		numberForTeachers = numOfIntervalEachTeacher;
		return numOfIntervalEachTeacher;
	}
	public  int[] load_number_of_interval_for_teachers (){
		
		return  numberForTeachers=load_number_of_interval_for_teachers(teacher_num_interval);
	}
	
	
	@SuppressWarnings("resource")
	public  int[] loadNumber_for_cousers(File file) {
		Scanner sc = null;
		int[] howManyIntervalsForCourse = new int [teacherNum];
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < teacherNum; i++) {
			howManyIntervalsForCourse[i] = sc.nextInt();
		}
		numberForCourses=howManyIntervalsForCourse;
		return howManyIntervalsForCourse;
	}
	public  int[] loadNumber_for_cousers() {
		return numberForCourses=loadNumber_for_cousers(course_num_interval);
	}
	
	
	@SuppressWarnings("resource")
	public  ArrayList<Integer>[] loadFreeTimes(File fileofFreeTimes) {
		Scanner sc = null;
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[] freetimes = new ArrayList[teacherNum];
		try {
			sc = new Scanner(fileofFreeTimes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < teacherNum; i++) {
			if (!sc.hasNextLine()) {
				System.err.println("no line :O ");
			}
			String line = sc.nextLine();
			Scanner tmp = new Scanner(line);
			freetimes[i] = new ArrayList<Integer>();
			while (tmp.hasNextInt()) {
				freetimes[i].add(tmp.nextInt());
			}
		}
		this.freetimes = freetimes;
		return freetimes;
	}
	public  ArrayList<Integer>[] loadFreeTimes( ){
		return freetimes=loadFreeTimes(freetimesFile);
	}
	
	
	@SuppressWarnings("resource")
	public  ArrayList<TeacherPair> loadSameResource(File file_same_resource){
		ArrayList<TeacherPair> sameResource = new ArrayList<TeacherPair>();
		Scanner sc = null;
		try {
			sc = new Scanner(file_same_resource);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while(sc.hasNextLine()){
			Scanner lineScan = new Scanner(sc.nextLine());
			sameResource.add(new TeacherPair(lineScan.nextInt(), lineScan.nextInt()));
		}
		this.sameResource = sameResource;
		return sameResource;
	}
	public  ArrayList<TeacherPair> loadSameResource(){
		return sameResource=loadSameResource(same_resource);
	}
	
	
	@SuppressWarnings("resource")
	public  ArrayList<TeacherPair> loadSameCourse (File file_same_course){
		ArrayList<TeacherPair> sameCourse = new ArrayList<TeacherPair>();
		Scanner sc = null;
		try {
			sc = new Scanner(file_same_course);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while(sc.hasNextLine()){
			Scanner lineScan = new Scanner(sc.nextLine());
			sameCourse.add(new TeacherPair(lineScan.nextInt(), lineScan.nextInt()));
		}
		this.sameCourse = sameCourse;
		return sameCourse;
	}
	public  ArrayList<TeacherPair> loadSameCourse ( ){
		return sameCourse= loadSameCourse(same_course);
	}


	public int getTeacherNum() {
		return teacherNum;
	}

	public int[] getNumberForTeachers() {
		if(numberForTeachers == null)
			load_number_of_interval_for_teachers();
		return numberForTeachers;
	}

	public int[] getNumberForCourses() {
		if(numberForCourses == null)
			loadNumber_for_cousers();
		return numberForCourses;
	}
	public int getNumberForTeacher(int teacher) {
		if(numberForTeachers == null)
			load_number_of_interval_for_teachers();
		return numberForTeachers[teacher];
	}

	public int getNumberForCourse(int teacher) {
		if(numberForCourses == null)
			loadNumber_for_cousers();
		return numberForCourses[teacher];
	}

	public ArrayList<Integer>[] getFreetimes() {
		if(freetimes == null)
			loadFreeTimes();
		return freetimes;
	}

	public ArrayList<TeacherPair> getSameCourse() {
		if(sameCourse==null)
			loadSameCourse();
		return sameCourse;
	}

	public ArrayList<TeacherPair> getSameResource() {
		if(sameResource == null)
			loadSameResource();
		return sameResource;
	}
	
	public int getIntervalNum() {
		return intervalNum;
	}

	public int getGroupNum() {
		return groupNum;
	}
	
	
	
}



