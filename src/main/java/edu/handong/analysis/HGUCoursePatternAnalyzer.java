package edu.handong.analysis;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import java.io.File;
import java.util.*;
import edu.handong.analysis.datamodel.*;
import edu.handong.analysis.utils.*;

public class HGUCoursePatternAnalyzer {

	private HashMap<String,Student> students;
	
	String insert;			// file path
	String output;			// result file path
	String analysis;		// 1. hw5 result, 2. coursecode new result
	String coursecode;		// course code
	String startyear;		// starting year
	String endyear;			// end year
	boolean help;
	
	
	public void run(String[] args) {
		
		Options options = createOptions();
		
		if (parseOptions(options, args)) {
			if (help) {
				printHelp(options);
				return;
			}
			
			System.out.println (insert);
			ArrayList<Course> lines = Utils.getLines(insert, true);
			
			// To sort HashMap entries by key values so that we can save the results by student ids in ascending order.
			students = loadStudentCourseRecords(lines, startyear, endyear);
			Map<String, Student> sortedStudents = new TreeMap<String,Student>(students);
			
			if (analysis.equals("1")) {
				// Generate result lines to be saved.
				ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
				Utils.writeAFile(linesToBeSaved, output);
			}
			
			else if (analysis.equals("2")) {
				
				ArrayList<String> linesToBeSaved = countPerCourseNameYear(students, coursecode, startyear, endyear);
				Utils.writeAFile(linesToBeSaved, output);
			}
			
			
			System.out.println("-------program terminated.");
		}
	}
	
	/**
	 * This method create HashMap<String,Student> from the data csv file. Key is a student id and the corresponding object is an instance of Student.
	 * The Student instance have all the Course instances taken by the student.
	 * @param lines
	 * @return
	 */
	private HashMap<String,Student> loadStudentCourseRecords(ArrayList<Course> lines, String startyear, String endyear) {
		
		// TODO: Implement this method
		HashMap<String,Student> resultHashMap = new HashMap<String,Student>();
		Student newStudent = null;
		int count = 0;
		int start, end;
		
		start = Integer.parseInt(startyear);
		end = Integer.parseInt(endyear);
		
		for (int i = 0; i < lines.size(); i++) {
			Course course = new Course();
			course = lines.get(i);
			
			if (i == 0 || !newStudent.getStudentId().equals(course.getStudentId())) {
				newStudent = new Student(course.getStudentId());
				count++;
			}
			
			for (int j = start; j <= end; j++) {
				if (course.getYearTaken() == j) {
					newStudent.addCourse(course);
					resultHashMap.put(course.getStudentId(), newStudent);
				}
			}
		}
		
		return resultHashMap; // do not forget to return a proper variable.
	}
	
	private HashMap<Course, Integer> loadCourseYearRecords(ArrayList<Course> lines) {
		HashMap<Course, Integer> resultHashMap = new HashMap<Course, Integer>();
		
		for (int i = 0; i < lines.size(); i++) {
			Course newCourse = new Course();
			newCourse = lines.get(i);
			resultHashMap.put(newCourse, newCourse.getYearTaken());
		}
		System.out.println (resultHashMap.size());
		return resultHashMap;
	}

	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();
		
		try {
			CommandLine cmd = parser.parse(options, args);
			
			insert = cmd.getOptionValue("i");
			output = cmd.getOptionValue("o");
			analysis = cmd.getOptionValue("a");
			coursecode = cmd.getOptionValue("c");
			if (analysis.equals("2")) {
				if (coursecode == null) {
					printHelp(options);
					return false;
				}
			}
			startyear = cmd.getOptionValue("s");
			endyear = cmd.getOptionValue("e");
			help = cmd.hasOption("h");
			
		} catch (Exception e) {
			printHelp(options);
			return false;
		}
		
		return true;
	}
	
	private Options createOptions() {
		
		Options options = new Options();
		
		//add insert options
		options.addOption(Option.builder("i").longOpt("input")
						.desc("Set an input file path")
						.hasArg()
						.argName("Input path")
						.required()
						.build());
		
		//add output options
		options.addOption(Option.builder("o").longOpt("output")
						.desc("Set an output file path")
						.hasArg()
						.argName("Output path")
						.required()
						.build());
		
		//add analysis options
		options.addOption(Option.builder("a").longOpt("analysis")
						.desc("1: Count courses per semester, 2: Count per course name and year")
						.hasArg()
						.argName("Analysis option")
						.required()
						.build());
		
		//add coursecode options
		options.addOption(Option.builder("c").longOpt("coursecode")
						.desc("Course code for '-a 2' option")
						.hasArg()
						.build());
		
		//add startyear options
		options.addOption(Option.builder("s").longOpt("startyear")
						.desc("Set the start year for analysis e.g., -s 2002")
						.hasArg()
						.required()
						.build());
		
		//add endyear options
		options.addOption(Option.builder("e").longOpt("endyear")
						.desc("Set the end year for analysis e.g., -e 2005")
						.hasArg()
						.required()
						.build());
		
		//add help options
		options.addOption(Option.builder("h").longOpt("help")
						.desc("Show a Help page")
						.build());
		
		return options;
	}
	
	private void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		String header = "HGU Course Analyzer";
		String footer ="";
		formatter.printHelp("HGUCourseCounter", header, options, footer, true);
	}
	
	
	//incompleted method
	private ArrayList<String> countPerCourseNameYear(Map<String, Student> sortedStudents, String courseCode, String startyear, String endyear) {
		
		ArrayList<String> newFile = new ArrayList<String>();
		ArrayList<Course> courseOfStudent;
		ArrayList<String> stuId = new ArrayList<String>();
		
		String sortedStudentKey = null;
		String newLine, rate;
		String courseName = null, code;
		Student newStudentByStudentId;
		Iterator<String> itr = sortedStudents.keySet().iterator();
		
		int start = Integer.parseInt(startyear);
		int end = Integer.parseInt(endyear);

		
		int[][] total = new int[end-start+1][4];
		int[][] taken = new int[end-start+1][4];
		
		String header = "Year" + "," + "Semester" + "," + "CourseCode"
				+ "," + "CourseName" + "," + "TotalStudents" 
				+ "," + "StudentsTaken" + "," + "Rate";
		
		newFile.add(header);
		
		while (itr.hasNext()) {	
			sortedStudentKey = (String) itr.next();
			newStudentByStudentId = sortedStudents.get(sortedStudentKey);
			courseOfStudent = newStudentByStudentId.getCoursesTaken();
		
			for (int i = 0; i < courseOfStudent.size(); i++) {	
				
				Course newC = courseOfStudent.get(i);		
				code = newC.getCourseCode();				
				String course1 = newC.getYearTaken() + "-" + newC.getSemesterCourseTaken();
				
				for (int j = start; j <= end; j++) {
					for (int z = 0; z < 4; z++) {
						String course2 = j + "-" + (z + 1);
						
						if (course1.equals(course2)) {
							if (!stuId.contains(course2)) {
								total[j-start][z]++;
								stuId.add(course2);
							}
							
							if (code.equals(courseCode)) {
								courseName = newC.getCourseName();
								taken[j-start][z]++;
							}
						}
						
					}
				}
			}
			
			stuId.clear();
		}
		
		for (int i = start; i <= end; i++) {
			for (int j = 0; j < 4; j++) {
				rate = String.format("%.1f%%", (double)taken[i-start][j]/total[i-start][j]*100);
				newLine = i + "," + (j+1) + "," + courseCode + "," + courseName + "," +
							total[i-start][j] + "," + taken[i-start][j] + "," + rate;
				
				if (total[i-start][j] != 0)
					newFile.add(newLine);
			}
		}
	
		return newFile;
	}

	
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents) {
		// TODO: Implement this method
		ArrayList<String> new_file = new ArrayList<String>();
		HashMap<String, Integer> searchTotalSemester;
		String new_line = null;
		String sortedStudentKey = null;
		Student newStudentByStudentId;
		Iterator<String> itr = sortedStudents.keySet().iterator();
		int semesterCourseNum, maxSemester;
		
		
		String defaultmenu = "StudentID" + "," + "TotalNumberOfSemestersRegistered" + "," 
							+ "Semester" + "," + "NumCoursesTakenInTheSemester";
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
				new_line = newStudentByStudentId.getStudentId() + "," +
							(maxSemester - 1) + "," +
							index + "," +
							semesterCourseNum;
				new_file.add(new_line);
			}
		}
		
		return new_file; // do not forget to return a proper variable.
	}
}