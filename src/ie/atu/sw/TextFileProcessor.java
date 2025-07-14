package ie.atu.sw;

import java.io.*;

// Processing of input files
public class TextFileProcessor {
	

	private String[] encoderDecoderInput; 
	private String[] encoderDecoderReturn;
	private int counterInputArr;
	
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

			while ((line = br.readLine()) != null) {

				line = line + " @@newline";

				String[] words = line.split(" ");

				for (String word : words) 
				{

					if  (word != null && !word.trim().isEmpty()) 
					{

						if (counterInputArr >= encoderDecoderInput.length)
						{
							expandInputArray();
						}

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
			System.out.println(e.getMessage()); 
			e.printStackTrace();
		}

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
	 * Writes the contents of {@code encoderDecoderReturn} to a text file at the specified path.
	 * 
	 * This method takes a file path and the contents of {@code encoderDecoderReturn} and passes
	 * it to another method {@code writeToText}. After this, the {@code encoderDecoderReturn} is reset using
	 * {@code resetArray()}.
	 * 
	 *
	 * @param filePath A string containing the file path of the file to write to.

	 */
	
	public void writeFile(String filePath) {
		
			writeToText(encoderDecoderReturn, filePath);
			resetArray();
	
	}

	
	

	private static boolean writeToText(String[] input, String filePath) { 
		Boolean writeFinished = false;
		
		try 
		{
			BufferedWriter output = new BufferedWriter(new FileWriter(filePath, true));
			
			for (int i = 0; i < input.length; i++) 
			{
				if (input[i] != null && !input[i].isEmpty())

				{
					
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
			output.close();
			writeFinished = true;
		}
		catch(Exception e)
		{
			System.out.println("Error writing to file: " + e.getMessage()); 
			e.printStackTrace();
		}

		return writeFinished;
	}
		


	private void expandInputArray() {
		String[] tempArr = new String[encoderDecoderInput.length*2];
		
		for (int i = 0; i < counterInputArr; i++)
		{
			tempArr[i] = encoderDecoderInput[i];
		}
		
		encoderDecoderInput = tempArr;
	}
	
	
	
	private void resetArray() {
		for (int i = 0; i < encoderDecoderInput.length; i++)
		{
			encoderDecoderInput[i] = null;
		}
		
		
		for (int i = 0; i < encoderDecoderReturn.length; i++)
		{
			encoderDecoderReturn[i] = null;
		}
		counterInputArr = 0;

	}
	

}

