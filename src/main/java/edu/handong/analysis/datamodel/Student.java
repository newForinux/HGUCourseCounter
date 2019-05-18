package edu.handong.analysis.datamodel;

import java.util.*;

public class Student {
	private String studentId;
	private ArrayList<Course> coursesTaken;
	private HashMap<String, Integer> semestersByYearAndSemester;
	
	public Student (String studentId) {
		
	}
	
	public void addCourse (Course newRecord) {
		coursesTaken.add(newRecord);
	}
	
	public HashMap<String, Integer> getSemestersByYearAndSemester() {
		int semester = 0, temp = 0;
		String key = null;
		HashMap<String, Integer> getter = new HashMap<String, Integer>();
		
		for (int i = 0; i < coursesTaken.size(); i++) {
			if (i == 0 || temp == coursesTaken.get(i).semesterCourseTaken) {
				key = coursesTaken.get(i).yearTaken + "-" + coursesTaken.get(i).semesterCourseTaken;
				semester++;
				temp = semester;
				getter.put(key, semester);
			}
		}
		
		return getter;
	}
	
	public int getNumCourseInNthSemester (int semester) {
		HashMap<String, Integer> getter = getSemestersByYearAndSemester();
		String key = null;
		int countNum = 0;
		
		for (int i = 0; i < coursesTaken.size(); i++) {
			key = coursesTaken.get(i).yearTaken + "-" + coursesTaken.get(i).semesterCourseTaken;
			if (semester == getter.get(key))
				countNum++;
		}
		
		return countNum;
	}
}