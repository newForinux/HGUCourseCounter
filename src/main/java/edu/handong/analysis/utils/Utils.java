package edu.handong.analysis.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import edu.handong.analysis.datamodel.Course;

public class Utils {

	public static ArrayList<Course> getLines (String file, boolean removeHeader) {
		ArrayList<Course> result = new ArrayList<Course>();
		
		try {
			
			Reader reader = new FileReader (file);
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
			
			for (CSVRecord record : records) {
				if (record.getRecordNumber() == 1)
					continue;
				
				Course course = new Course();
				
				course.setStudentId(record.get(0).trim());
				course.setYearMonthGraduated(record.get(1).trim());
				course.setFirstMajor(record.get(2).trim());
				course.setSecondMajor(record.get(3).trim());
				course.setCourseCode(record.get(4).trim());
				course.setCourseName(record.get(5).trim());
				course.setCourseCredit(record.get(6).trim());
				course.setYearTaken(Integer.parseInt(record.get(7).trim()));
				course.setSemesterCourseTaken(Integer.parseInt(record.get(8).trim()));
				
				result.add(course);
			}
			
		} catch (IOException e) {
			new NotEnoughArgumentException("The file path does not exist. Please check your CLI argument!");
			System.exit(0);
		}
		
		return result;
	}
	
	public static void writeAFile (ArrayList<String> lines, String targetFileName) {
		
		PrintWriter outputStream = null;
		
		while (outputStream == null) {
			try
			{
				outputStream = new PrintWriter(new FileOutputStream(targetFileName));
			
			}catch (FileNotFoundException e)
			{
				File mkdir_path = new File(targetFileName);
				mkdir_path.getParentFile().mkdirs();
			}
		}
		
		for (String line:lines) {
			outputStream.println(line);
		}
		outputStream.close();
	}
}
