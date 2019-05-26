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
				
			if (analysis.equals("1")) {
				students = loadStudentCourseRecords(lines);
				
				// To sort HashMap entries by key values so that we can save the results by student ids in ascending order.
				Map<String, Student> sortedStudents = new TreeMap<String,Student>(students); 
				
				// Generate result lines to be saved.
				ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
			
				// Write a file (named like the value of resultPath) with linesTobeSaved.
				Utils.writeAFile(linesToBeSaved, output);
			}
			
			else if (analysis.equals("2")) {
				
				ArrayList<String> linesToBeSaved = countPerCourseNameYear(lines, coursecode, startyear, endyear);
				
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
	private HashMap<String,Student> loadStudentCourseRecords(ArrayList<Course> lines) {
		
		// TODO: Implement this method
		HashMap<String,Student> resultHashMap = new HashMap<String,Student>();
		Student newStudent = null;
		int count = 0;
		
		for (int i = 0; i < lines.size(); i++) {
			Course course = new Course();
			course = lines.get(i);
			
			if (i == 0 || !newStudent.getStudentId().equals(course.getStudentId())) {
				newStudent = new Student(course.getStudentId());
				count++;
				System.out.println (count);
			}
			
			newStudent.addCourse(course);
			resultHashMap.put(course.getStudentId(), newStudent);
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
			startyear = cmd.getOptionValue("s");
			endyear = cmd.getOptionValue("e");
			help = cmd.hasOption("h");
			
		} catch (Exception e) {
			printHelp(options);
			System.out.println ("IOException!!");
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
						.required()
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
	
	
	
	private ArrayList<String> countPerCourseNameYear(ArrayList<Course> lines, String courseCode, String startyear, String endyear) {
		ArrayList<String> newFile = new ArrayList<String>();
		ArrayList<String> stuId = new ArrayList<String>();
		HashMap<String, Integer> search = new HashMap<String, Integer>();
		HashMap<String, Integer> search2 = new HashMap<String, Integer>();
		String newLine;
		
		int start = Integer.parseInt(startyear);
		int end = Integer.parseInt(endyear);
		int year, semester, value;
		String coursecode, coursename = null, rate;
		int[][] Total = new int [end-start+1][4];
		int[][] Taken = new int [end-start+1][4];
		
		String header = "Year" + "," + "Semester" + "," + "CourseCode"
						+ "," + "CourseName" + "," + "TotalStudents" 
						+ "," + "StudentsTaken" + "," + "Rate";
		
		newFile.add(header);
		
		for (int i = 0; i < lines.size(); i++) {
			Course newCourse = new Course();
			newCourse = lines.get(i);
			year = newCourse.getYearTaken();
			semester = newCourse.getSemesterCourseTaken();
			coursecode = newCourse.getCourseCode();
			
			for (int j = start; j <= end; j++) {
				if (year == j) {
					if (!stuId.contains(newCourse.getStudentId())) {
						Total[j-start][semester-1]++;
						stuId.add(newCourse.getStudentId());
						search.put(newCourse.getStudentId(), semester);
						search2.put(newCourse.getStudentId(), year);
					
						if (coursecode.equals(courseCode)) {
							coursename = newCourse.getCourseName();
							Taken[j-start][semester-1]++;
						}
					}
					
					else {
						if (semester != search.get(newCourse.getStudentId())) {
							Total[j-start][semester-1]++;
							stuId.add(newCourse.getStudentId());
							search.put(newCourse.getStudentId(), semester);
							
						
							if (coursecode.equals(courseCode)) {
								coursename = newCourse.getCourseName();
								Taken[j-start][semester-1]++;
							}
						}
						
						else if (j != search2.get(newCourse.getStudentId())) {
							Total[j-start][semester-1]++;
							stuId.add(newCourse.getStudentId());
							search2.put(newCourse.getStudentId(), year);
							
						
							if (coursecode.equals(courseCode)) {
								coursename = newCourse.getCourseName();
								Taken[j-start][semester-1]++;
							}
						}
					}
				}
				
			}
		}
		
		for (int i = start; i <= end; i++) {
			for (int j = 0; j < 4; j++) {
				rate = String.format("%.1f%%", (double)Taken[i-start][j]/Total[i-start][j]*100);
				newLine = i + "," +
						(j+1) + "," +
						courseCode + "," +
						coursename + "," +
						Total[i-start][j] + "," +
						Taken[i-start][j] + "," +
						rate;
				
				if (Total[i-start][j] != 0)
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