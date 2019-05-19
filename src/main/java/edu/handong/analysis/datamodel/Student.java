package edu.handong.analysis.datamodel;

import java.util.*;

public class Student {
	private String studentId;
	private ArrayList<Course> coursesTaken;
	private HashMap<String, Integer> semestersByYearAndSemester;
	
	public Student (String studentId) {
		this.studentId = studentId;
		coursesTaken = new ArrayList<Course>();
		semestersByYearAndSemester = new HashMap<String, Integer>();
	}
	
	public void addCourse (Course newRecord) {
		coursesTaken.add(newRecord);
	}
	
	public HashMap<String, Integer> getSemestersByYearAndSemester() {
		int semester = 0, temp = 0, tempYear = 0;
		String key = null;
		semestersByYearAndSemester = new HashMap<String, Integer>(); 
		
		for (int i = 0; i < coursesTaken.size(); i++) {
			
			if (i == 0 || temp != coursesTaken.get(i).semesterCourseTaken) {
				key = coursesTaken.get(i).yearTaken + "-" + coursesTaken.get(i).semesterCourseTaken;
				semester++;
				temp = coursesTaken.get(i).semesterCourseTaken;
				tempYear = coursesTaken.get(i).yearTaken;
				//System.out.println (coursesTaken.get(i).getStudentId() + " " + key + " " + semester);
				
				semestersByYearAndSemester.put(key, semester);
			}
			
			else if (temp == coursesTaken.get(i).semesterCourseTaken && tempYear != coursesTaken.get(i).yearTaken) {
				key = coursesTaken.get(i).yearTaken + "-" + coursesTaken.get(i).semesterCourseTaken;
				semester++;
				temp = coursesTaken.get(i).semesterCourseTaken;
				tempYear = coursesTaken.get(i).yearTaken;
				semestersByYearAndSemester.put(key, semester);
			}
		}
		
		return semestersByYearAndSemester;
	}
	
	public int getNumCourseInNthSemester (int semester) {
		semestersByYearAndSemester = getSemestersByYearAndSemester();
		Map<String, Integer> getter = new TreeMap<>(semestersByYearAndSemester);
		String key = null;
		int countNum = 0;
		
		for (int i = 0; i < coursesTaken.size(); i++) {
			key = coursesTaken.get(i).yearTaken + "-" + coursesTaken.get(i).semesterCourseTaken;
			if (semester == getter.get(key))
				countNum++;
		}
		
		return countNum;
	}

	public String getStudentId() {
		return studentId;
	}

	public ArrayList<Course> getCoursesTaken() {
		return coursesTaken;
	}
}