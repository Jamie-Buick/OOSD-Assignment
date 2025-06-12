package ie.atu.sw;

import java.io.*;

// Processing of input files
public class TextFileProcessor {
	
	private String[] encoderDecoderResult; 
	private int counterArr;
	
	public TextFileProcessor(){
		encoderDecoderResult = new String[10];
		counterArr = 0;
	}
	
	
	
	
	public boolean readFile(String filePath, Boolean encode) {
		String line = null;
		Boolean readFinished = false;
		String[] encoderDecoderReturn;

		

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

			while ((line = br.readLine()) != null) {
				
				String[] words = line.split(" ");
				//System.out.println(words.length);
				
				for (String word : words) 
				{
					// This prints the line as empty.
					if (word.isEmpty()) {
						word = "\n";
					}
					
					
					if (!word.isEmpty()) 
					{
						System.out.println(word);
						if (encode) 
						{
							word = word.trim().toLowerCase();
							encoderDecoderReturn = EncoderDecoder.encode(word);

						}
						else 
						{
							word = word.strip().replaceAll("[^0-9 ]", "");
							encoderDecoderReturn = EncoderDecoder.decode(word);
						}

						// Copy results into the main array
						for (int i = 0; i < encoderDecoderReturn.length; i++) 
						{
							if (encoderDecoderReturn[i] != null) 
							{
								
								if (counterArr >= encoderDecoderResult.length)
								{
									expandArray();
								}
								
								encoderDecoderResult[counterArr] = encoderDecoderReturn[i];
								counterArr++;
							}
						}
					}
					
				}
			}

			br.close();
			readFinished = true;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		return readFinished;
		
	}
	
	public boolean writeFile(String filePath, Boolean encode) {

		// encoding format to text file
		if (encode) {
			for (int i = 0; i < encoderDecoderResult.length; i++) 
			{
				if (encoderDecoderResult[i] != null) 
				{
					writeToText(encoderDecoderResult[i], filePath);
				}
			}
		}

		// decoding format to text file
		else 
		{
			// pass full encoderDecoder array to this method that will return a new array
			// with the decoded text built into readable text
			String buildPartialWords[] = buildPrefixSuffix(encoderDecoderResult);
			String removeNullVals[] = removeNulls(buildPartialWords);
			String buildPunctuation[] = buildPunct(removeNullVals);

			for (int i = 0; i < buildPunctuation.length; i++) 
			{
				if (buildPunctuation[i] != null) 
				{
					writeToText(buildPunctuation[i], filePath);

				}
			}
		}

		cleanArray();

		return true;
	}
			
			
		
	
	private static boolean writeToText(String word, String filePath) { 
		Boolean writeFinished = false;
		
		try 
		{
			BufferedWriter output = new BufferedWriter(new FileWriter(filePath, true));
			
			output.write(word + " ");

			output.close();
			writeFinished = true;
		}
		catch(Exception e)
		{
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
	                temp = inputWords[i + 1].trim();
	                joinedPunct[i] = inputWords[i] + temp;
	                i++; // Skip the punctuation
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
	
	
	private void expandArray() {
		String[] tempArr = new String[encoderDecoderResult.length*2];
		
		for (int i = 0; i < counterArr; i++)
		{
			tempArr[i] = encoderDecoderResult[i];
		}
		
		encoderDecoderResult = tempArr;
		
	}
	
	private void cleanArray() {
		for (int i = 0; i < encoderDecoderResult.length; i++)
		{
			encoderDecoderResult[i] = null;
		}

	}
	
	
	
	
}

