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
		counterInputArr = 0;
	}
	
	
	

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
					//System.out.println(word); // fine here
					
					if  (word != null && !word.trim().isEmpty()) 
					{

						if (counterInputArr >= encoderDecoderInput.length)
						{
							expandInputArray();
						}

						encoderDecoderInput[counterInputArr] = word.trim().toLowerCase();
						//System.out.println(counterInputArr + " " + word.trim().toLowerCase()); // fine too
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
		
		
		// Pass it to the encode / decode methods now?
		// remove nulls here
		if (encode) 
		{
			
			encoderDecoderReturn = EncoderDecoder.encode(encoderDecoderInput);
			
			for(int g = 0; g < encoderDecoderReturn.length; g++) 
			{
				System.out.println(encoderDecoderReturn[g]);
			}
			
			
		}
		else 
		{
			
			//encoderDecoderReturn = EncoderDecoder.decode(encoderDecoderInput);
		}
		

	
		return readFinished;

	}
	
	
	
	
	public boolean writeFile(String filePath, Boolean encode) {

		// encoding format to text file
		if (encode) 
		{

			writeToText(encoderDecoderReturn, filePath);

		}

		// decoding format to text file
		else 
		{
			// pass full encoderDecoder array to this method that will return a new array
			// with the decoded text built into readable text
			String buildPartialWords[] = buildPrefixSuffix(encoderDecoderResult);
			String removeNullVals[] = removeNulls(buildPartialWords);
			String buildPunctuation[] = buildPunct(removeNullVals);

			writeToText(buildPunctuation, filePath);

		}

		cleanArray();

		return true;
	}

	
	

	
	private static boolean writeToText(String[] encoderDecoderResult, String filePath) { 
		Boolean writeFinished = false;
		
		try 
		{
			BufferedWriter output = new BufferedWriter(new FileWriter(filePath, true));
			
			for (int i = 0; i < encoderDecoderResult.length; i++) 
			{
				if (encoderDecoderResult[i] != null && !encoderDecoderResult[i].isEmpty())

				{
					
					if(!(encoderDecoderResult[i].equals("\n"))) 
					{
						output.write(encoderDecoderResult[i] + " ");
					}
					else 
					{
						output.write(encoderDecoderResult[i]);
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
		


	private static String[] buildPrefixSuffix(String inputWords[]) { 
		
		String[] joinedWords = new String[inputWords.length]; 
		String temp;

	    for (int i = 0; i < inputWords.length; i++) 
	    {
	        if (inputWords[i] != null) 
	        {
	            if (!inputWords[i].startsWith("@@")) 
	            {
	                if (i + 1 < inputWords.length && inputWords[i + 1] != null && inputWords[i+1].startsWith("@@")) 
	                {
	                    temp = inputWords[i + 1].replace("@@", "").trim();
	                    joinedWords[i] = inputWords[i] + temp;
	                    i++; 
	                } 
	                else 
	                { 
	                	joinedWords[i] = inputWords[i];
	                }
	            }
	        }
	    }
	    
		 return joinedWords;
	}
	
	
	
	private static String[] buildPunct(String[] inputWords) {
	  
	    String[] joinedPunct = new String[inputWords.length];
	    String temp;
	    
	    
	    for (int i = 0; i < inputWords.length; i++) 
	    {
	        if (inputWords[i] != null) 
	        {
	            if (i + 1 < inputWords.length && inputWords[i + 1] != null && inputWords[i + 1].matches("\\p{Punct}")) 
	            {
	                // Check if next next word is also punctuation
	                if (i + 2 < inputWords.length && inputWords[i + 2] != null && inputWords[i + 2].matches("\\p{Punct}")) 
	                {
	                    // Two punctuation marks after the word
	                    joinedPunct[i] = inputWords[i] + inputWords[i + 1].trim() + inputWords[i + 2].trim();
	                    i += 2; // Skip both punctuation marks
	                }
	                else 
	                {
	                    // Only one punctuation mark after the word
	                    temp = inputWords[i + 1].trim();
	                    joinedPunct[i] = inputWords[i] + temp;
	                    i++; // Skip the one punctuation mark
	                }
	            } 
	            else 
	            {
	                joinedPunct[i] = inputWords[i];
	            }
	        }
	    }
	    
	    return joinedPunct;
	}
	
	
	
	private static String[] removeNulls(String[] inputWords) {

		String[] cleanedArr = new String[inputWords.length];

		int counter = 0;
		for (int i = 0; i < inputWords.length; i++) 
		{
			if (!(inputWords[i] == null)) 
			{
				cleanedArr[counter] = inputWords[i];
				counter++;
			}
		}
		
		return cleanedArr;
	}
	
	
	private void expandInputArray() {
		String[] tempArr = new String[encoderDecoderInput.length*2];
		
		for (int i = 0; i < counterInputArr; i++)
		{
			tempArr[i] = encoderDecoderInput[i];
		}
		
		encoderDecoderInput = tempArr;
		
	}
	
	private void cleanArray() {
		for (int i = 0; i < encoderDecoderResult.length; i++)
		{
			encoderDecoderResult[i] = null;
		}

	}
	
	
	
	
}

