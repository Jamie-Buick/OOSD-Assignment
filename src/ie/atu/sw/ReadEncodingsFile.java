package ie.atu.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


/**
 * Handles the parsing of a CSV file that contains the encoding mappings.
 * 
 * This class reads a CSV file containing word-encoding mappings and stores them in a 2D array.
 * It provides a method to parse the file and a static getter to access the encoding mapping.
 */
public class ReadEncodingsFile {
	
	
	/**
	 * A 2D Array that stores the word encoding mapping. This is parsed from the CSV file.
	 * The size of the 2D array is defined here as this is known and will not change.
	 * The 2D array contains two columns: [0] = word & [1] = encoding.
	 */
	private static String[][] encodings = new String[10000][2];
	
	
	
	/**
	 * Getter method to return encoding mapping.
	 * 
	 * @return encodings This is the mapping array for the words and corresponding encoding.
	 */
	public static String[][] getEncodings() {
		return encodings;
	}
	
	
	
	/**
	 * Parses a CSV file containing the encoding mappings.
	 * 
	 * This method parses the provided encodings CSV into the {@code encodings} 2D array. {@code BufferedReader} is 
	 * used to read the CSV file line by line, each line is then parsed into the 2D array.
	 * 
	 * @param fileName the path to the CSV file containing the encoding mappings.
	 */
	public void parseEncoding(String fileName) {
		String line = null;
		int row = 0;
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File (fileName)))))
		{
			// Read each line of the CSV file until the end
			while((line = br.readLine()) != null) {
				
				// Split the data by comma into two parts
				String[] data = line.split(",");
				
				// Store word & encoding in the array 
				encodings[row][0] = data[0];
				encodings[row][1] = data[1];
			
				// Increment to the next row
				row++;
			}
		}
		catch(Exception e) 
		{
			// Print any errors
			System.out.println(e.getMessage()); 
			e.printStackTrace();
		}
	}
}
