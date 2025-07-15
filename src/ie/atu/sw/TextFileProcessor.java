package ie.atu.sw;

import java.io.*;

/**
 * Handles reading from an input text file and passing its contents to encoder or decoder methods.
 * The resulting data is then written to an output text file.
 * 
 * This class reads and stores words from a text file into an array that is dynamically sized.
 * It passes the array to the {@code EncoderDecoder} class for processing. A processed array
 * is returned and can be written to a text file.
 * 
 * This class will reset the arrays after processing to ensure no old data can alter results of
 * future operations.
 * 
 * 
 */
public class TextFileProcessor {
	
	// String array that stores words to be encoded/encoded
	private String[] encoderDecoderInput; 
	
	// Counter used to track number of words in the encoderDecoderInput array
	private int counterInputArr;
	
	// String array that stores the encoded/decoded words
	private String[] encoderDecoderReturn;
	

	/**
	 * Constructs a new {@code TextFileProcessor} object and initializes the internal input array.
	 * 
	 * This constructor creates the {@code encoderDecoderInput} array with an initial size of 20 elements.
	 * The array is used to store individual words from a text file that will be either encoded or decoded.
	 * The size is later dynamically expanded as needed during file processing.
	 */
	public TextFileProcessor(){
		encoderDecoderInput = new String[20];
	}
	
	
	
	/**
	 * Reads a text file given a correct file path
	 * 
	 * This method reads the text file line by line using BufferedReader. Each line is contacted using a 
	 * special keyword '@@newline' to indicate the end of a line. The line is then split into individual 
	 * words which are trimmed, converted to lower case and added to an array. If the array exceeds its 
	 * capacity, it is dynamically expanded. After processing the file, the array is passed to either the
	 * encode or decode method depending on the value of the {@code encode} parameter.

	 *
	 * @param filePath A string containing the file path of the file to read.
	 * @param encode A boolean {@code true} to encode and {@code false} to decode. 
	 * @return readFinished {@code true} To indicate that the file was read successfully, otherwise {@code false}.
	 */
	public boolean readFile(String filePath, Boolean encode) {
		String line = null;
		Boolean readFinished = false;

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

			// Reads the text file until the end
			while ((line = br.readLine()) != null) {

				// Each line read is appended with this special token to preserve line breaks and layout 
				line = line + " @@newline";

				// The line is split into a string array by using a space as a delimiter
				String[] words = line.split(" ");

				// The words array is looped through to add it to the encoderDecoderInput array
				for (String word : words) 
				{

					// Ensure the word is not null or empty 
					if  (word != null && !word.trim().isEmpty()) 
					{

						/*
						 * When counterInputArr, which increments every time a valid word is added to the encoderDecoderInput array,
						 * is greater than or equal to the encoderDecoderInput array length it must be expanded.
						 * 
						 * The expandInputArray method is called which doubles the size of the encoderDecoderInput array.
						 */
						if (counterInputArr >= encoderDecoderInput.length)
						{
							expandInputArray();
						}

						/*
						 * Add the word to the encoderDecoderInput array at next index. The word is trimmed of white space and converted 
						 * to lower case. The counterInputArr is incremented.
						 */
						encoderDecoderInput[counterInputArr] = word.trim().toLowerCase();
						counterInputArr++;
					}
				}
			}

			br.close();
			readFinished = true;
		} 
		catch (Exception e) 
		{
			// Print any error messages
			System.out.println(e.getMessage()); 
			e.printStackTrace();
		}

		/*
		 * If the encode flag is true, pass encoderDecoderInput to EncoderDecoder.encode().
		 * Otherwise, pass it to EncoderDecoder.decode().
		 */
		if (encode) 
		{
			encoderDecoderReturn = EncoderDecoder.encode(encoderDecoderInput);
		}
		else 
		{
			encoderDecoderReturn = EncoderDecoder.decode(encoderDecoderInput);
		}

		return readFinished;
	}
	
	
	
	/**
	 * Delegates the writing of the contents of {@code encoderDecoderReturn} to a text file at the specified path.
	 * 
	 * This method takes a file path and the contents of {@code encoderDecoderReturn} and passes
	 * it to another method {@code writeToText}. After this, the {@code encoderDecoderReturn} is reset using
	 * {@code resetArray()}.
	 * 
	 * @param filePath A string containing the file path of the file to write to.
	 */
	public void writeFile(String filePath) {
		
			writeToText(encoderDecoderReturn, filePath);
			resetArray();
	}

	
	
	/**
	 * Writes the contents of {@code encoderDecoderReturn} to a text file at the specified path.
	 * 
	 * This method takes a file path and the contents of {@code encoderDecoderReturn} and writes
	 * to the text file using BufferedWriter. The array is stepped through and the contents of 
	 * each element are written as long as they or not null or empty. Line breaks are maintained 
	 * through the "\n" newline character. Correct spacing is maintained by concatenating " " 
	 * after each word.
	 * 
	 *
	 * @param input An array of words to be written directly to the text file.
	 * @param filePath A string containing the file path of the file to write to.
	 * @return writeFinished {@code true} To indicate that the file was written to successfully, otherwise {@code false}.
	 */
	private static boolean writeToText(String[] input, String filePath) { 
		Boolean writeFinished = false;
		
		try 
		{
			BufferedWriter output = new BufferedWriter(new FileWriter(filePath, true));
			
			// Loops over the input array.
			for (int i = 0; i < input.length; i++) 
			{
				// Ensures the element contains valid data
				if (input[i] != null && !input[i].isEmpty())
				{
					/*
					 *  If the element is a newline character, print the element contents and concatenate with a space.
					 *  Otherwise, print the element contents.
					 */
					if(!(input[i].equals("\n"))) 
					{
						output.write(input[i] + " ");
					}
					else 
					{
						output.write(input[i]);
					}
				}
			}
			// Close the write when finished.
			output.close();
			writeFinished = true;
		}
		catch(Exception e)
		{
			// Prints any errors.
			System.out.println("Error writing to file: " + e.getMessage()); 
			e.printStackTrace();
		}

		return writeFinished;
	}
		

	
	/**
	 * Expands the {@code encoderDecoderInput} array dynamically.
	 * 
	 * This method creates a new array with double the size of {@code encoderDecoderInput}, 
	 * copies the existing contents into it and then replaces {@code encoderDecoderInput} 
	 * with the new expanded array.
	 * 
	 */
	private void expandInputArray() {
		// Create a new array that x2 larger than the input array
		String[] tempArr = new String[encoderDecoderInput.length*2];
		
		// Copy existing elements into the new array.
		for (int i = 0; i < counterInputArr; i++)
		{
			tempArr[i] = encoderDecoderInput[i];
		}
		
		// Replace the old array with the new larger array.
		encoderDecoderInput = tempArr;
	}
	
	
	
	/**
	 * Resets the {@code encoderDecoderInput} array.
	 * 
	 * This method replaces the contents of the {@code encoderDecoderInput} and {@code encoderDecoderReturn}
	 * arrays with null. It also resets the {@code counterInputArr} to zero. This is to ensure clean arrays before 
	 * processing new data.
	 * 
	 */
	private void resetArray() {
		// Loop the encoderDecoderInput array.
		for (int i = 0; i < encoderDecoderInput.length; i++)
		{
			// Set all elements to null
			encoderDecoderInput[i] = null;
		}
		
		// Loop the encoderDecoderReturn array.
		for (int i = 0; i < encoderDecoderReturn.length; i++)
		{
			// Set all elements to null
			encoderDecoderReturn[i] = null;
		}
		
		// Reset counterInputArr to start from the beginning.
		counterInputArr = 0;
	}
}

