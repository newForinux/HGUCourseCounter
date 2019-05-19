package edu.handong.analysis.datamodel;

public class Course {
	private String studentId;
	private String yearMonthGraduated;
	private String firstMajor;
	private String secondMajor;
	private String courseCode;
	private String courseName;
	private String courseCredit;
	int yearTaken;
	int semesterCourseTaken;
	
	public Course (String line) {
		String[] array_str = line.split(",");
		studentId = array_str[0].trim();
		yearMonthGraduated = array_str[1].trim();
		firstMajor = array_str[2].trim();
		secondMajor = array_str[3].trim();
		courseCode = array_str[4].trim();
		courseName = array_str[5].trim();
		courseCredit = array_str[6].trim();
		yearTaken = Integer.parseInt(array_str[7].trim());
		semesterCourseTaken = Integer.parseInt(array_str[8].trim());
	}
	
	public String getStudentId() {
		return studentId;
	}

	public String getYearMonthGraduated() {
		return yearMonthGraduated;
	}

	public String getFirstMajor() {
		return firstMajor;
	}

	public String getSecondMajor() {
		return secondMajor;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public String getCourseName() {
		return courseName;
	}

	public String getCourseCredit() {
		return courseCredit;
	}

	public int getYearTaken() {
		return yearTaken;
	}

	public int getSemesterCourseTaken() {
		return semesterCourseTaken;
	}
}
