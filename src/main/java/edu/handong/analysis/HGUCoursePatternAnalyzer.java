package edu.handong.analysis;

import java.io.File;
import java.util.*;
import edu.handong.analysis.datamodel.*;
import edu.handong.analysis.utils.*;

public class HGUCoursePatternAnalyzer {

	private HashMap<String,Student> students;
	
	/**
	 * This method runs our analysis logic to save the number courses taken by each student per semester in a result file.
	 * Run method must not be changed!!
	 * @param args
	 */
	public void run(String[] args) {
		
		/*
		try {
			// when there are not enough arguments from CLI, it throws the NotEnoughArgmentException which must be defined by you.
			if(args.length<2)
				throw new NotEnoughArgumentException();
		} catch (NotEnoughArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		*/

		
		String dataPath = args[0]; // csv file to be analyzed
		String resultPath = args[1]; // the file path where the results are saved.
		ArrayList<String> lines = Utils.getLines(dataPath, true);
		
		students = loadStudentCourseRecords(lines);
		
		// To sort HashMap entries by key values so that we can save the results by student ids in ascending order.
		Map<String, Student> sortedStudents = new TreeMap<String,Student>(students); 
		
		// Generate result lines to be saved.
		ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
		
		// Write a file (named like the value of resultPath) with linesTobeSaved.
		Utils.writeAFile(linesToBeSaved, resultPath);
	}
	
	/**
	 * This method create HashMap<String,Student> from the data csv file. Key is a student id and the corresponding object is an instance of Student.
	 * The Student instance have all the Course instances taken by the student.
	 * @param lines
	 * @return
	 */
	private HashMap<String,Student> loadStudentCourseRecords(ArrayList<String> lines) {
		
		// TODO: Implement this method
		HashMap<String,Student> resultHashMap = new HashMap<String,Student>();
		Student newStudent = null;
		
		for (int i = 0; i < lines.size(); i++) {
			Course course = new Course(lines.get(i));
			
			if (i == 0 || !newStudent.getStudentId().equals(course.getStudentId())) 
				newStudent = new Student(course.getStudentId());
			
			newStudent.addCourse(course);
			resultHashMap.put(course.getStudentId(), newStudent);
		}
		
		return resultHashMap; // do not forget to return a proper variable.
	}

	/**
	 * This method generate the number of courses taken by a student in each semester. The result file look like this:
	 * StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester
	 * 0001,14,1,9
     * 0001,14,2,8
	 * ....
	 * 
	 * 0001,14,1,9 => this means, 0001 student registered 14 semesters in total. In the first semester (1), the student took 9 courses.
	 * 
	 * 
	 * @param sortedStudents
	 * @return
	 */
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents) {
		// TODO: Implement this method
		ArrayList<String> new_file = new ArrayList<String>();
		HashMap<String, Integer> searchTotalSemester;
		String new_line = null;
		String sortedStudentKey = null;
		Student newStudentByStudentId;
		Iterator<String> itr = sortedStudents.keySet().iterator();
		int semesterCourseNum, maxSemester;
		
		String defaultmenu = "StudentID" + ", " + "TotalNumberOfSemestersRegistered" + ", " 
							+ "Semester" + ", " + "NumCoursesTakenInTheSemester";
		new_file.add(defaultmenu);
		
		while (itr.hasNext()) {
			maxSemester = 1;
			sortedStudentKey = (String) itr.next();
			newStudentByStudentId = sortedStudents.get(sortedStudentKey);
			searchTotalSemester = newStudentByStudentId.getSemestersByYearAndSemester();
			
			
			while (searchTotalSemester.containsValue(maxSemester))
				maxSemester++;
			
			
			for (int index = 1; index < maxSemester; index++) {
				semesterCourseNum = newStudentByStudentId.getNumCourseInNthSemester(index);
				new_line = newStudentByStudentId.getStudentId() + ", " +
							(maxSemester - 1) + ", " +
							index + ", " +
							semesterCourseNum;
				new_file.add(new_line);
			}
		}
		
		return new_file; // do not forget to return a proper variable.
	}
}
