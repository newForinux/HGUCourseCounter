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
	
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public void setYearMonthGraduated(String yearMonthGraduated) {
		this.yearMonthGraduated = yearMonthGraduated;
	}

	public void setFirstMajor(String firstMajor) {
		this.firstMajor = firstMajor;
	}

	public void setSecondMajor(String secondMajor) {
		this.secondMajor = secondMajor;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public void setCourseCredit(String courseCredit) {
		this.courseCredit = courseCredit;
	}

	public void setYearTaken(int yearTaken) {
		this.yearTaken = yearTaken;
	}

	public void setSemesterCourseTaken(int semesterCourseTaken) {
		this.semesterCourseTaken = semesterCourseTaken;
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
