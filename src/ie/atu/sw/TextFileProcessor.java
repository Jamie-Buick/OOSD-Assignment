package ie.atu.sw;

import java.io.*;

// Processing of input files
public class TextFileProcessor {
	
	private String[] encoderDecoderResult; 
	private String[] encoderDecoderInput; 
	private String[] encoderDecoderReturn;
	private int counterInputArr;
	
	public TextFileProcessor(){
		encoderDecoderResult = new String[10];
		encoderDecoderInput = new String[20];
	}
	
	

	public boolean readFile(String filePath, Boolean encode) {
		String line = null;
		Boolean readFinished = false;
		//resetArray();  
		

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
			
			/*
			for (int i = 0; i < encoderDecoderReturn.length; i++)
			{
				System.out.println(encoderDecoderReturn[i]);
			}
			*/
			
		}
		

	
		return readFinished;

	}
	
	
	
	
	public void writeFile(String filePath, Boolean encode) {

		// encoding format to text file
		if (encode) 
		{

			writeToText(encoderDecoderReturn, filePath);
			resetArray();

		}

		// decoding format to text file
		else 
		{
			writeToText(encoderDecoderReturn, filePath);
			resetArray();
		}

	
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
		for (int i = 0; i < encoderDecoderReturn.length; i++)
		{
			encoderDecoderReturn[i] = null;
		}
		counterInputArr = 0;

	}
	
	
	
	
}

