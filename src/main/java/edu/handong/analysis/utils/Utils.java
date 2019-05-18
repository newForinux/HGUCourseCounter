package edu.handong.analysis.utils;

import java.io.*;
import java.util.*;

public class Utils {

	public static ArrayList<String> getLines (String file, boolean removeHeader) {
		Scanner inputStream = new Scanner(file);
		ArrayList<String> result = new ArrayList<String>();
		String line = new String();
		
		while (inputStream.hasNextLine())
		{
			line = inputStream.nextLine();
			result.add(line);
		}
		
		if (removeHeader)
			result.remove(0);
		
		inputStream.close();
		return result;
	}
	
	public static void writeAFile (ArrayList<String> lines, String targetFileName) {
		
		PrintWriter outputStream = null;
		
		try
		{
			outputStream = new PrintWriter(new FileOutputStream(targetFileName));
			
		}catch (FileNotFoundException e)
		{
			File mkdir_path = new File(targetFileName);
			mkdir_path.getParentFile().mkdirs();
		}
		
		for (String line:lines) {
			outputStream.println(line);
		}
		outputStream.close();
	}
}
