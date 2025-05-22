package ie.atu.sw;

import java.io.*;

// Processing of input files
public class TextFileProcessor {
	

	String[] encoderDecoderResult = new String[100000]; 
	
	
	public boolean readFile(String filePath, Boolean encode) {
		String line = null;
		Boolean readFinished = false;
		String [] encoderDecoderReturn;
		int counter = 0;
		
		try 
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			
			while((line = br.readLine()) != null) {
					
				String[] words = line.split(" ");
				
					for (String word : words) {
						if(!word.isEmpty()) {
							
							if (encode)
							{
								word = word.trim();
								encoderDecoderReturn = EncoderDecoder.encode(word);
								
							}
							else
							{
								word = word.strip().replaceAll("[^0-9 ]", "");
								encoderDecoderReturn = EncoderDecoder.decode(word);
							}
							
							//not working or correct!
							
							 // Copy results into the main array
					        for (int i = 0; i < encoderDecoderReturn.length; i++) {
					            if (encoderDecoderReturn[i] != null) {
					                encoderDecoderResult[counter] = encoderDecoderReturn[i];
					                //System.out.println(finalResults[i]);
					                counter++;
					            }
					        }
						}
				  
					}	
			}
			
			/*
			// Print results
	        for (int j = 0; j < finalResults.length; j++) {
	              System.out.println("Final:" + finalResults[j]);
	        }
			  */
			br.close();
			readFinished = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return readFinished;

	}	
	
	public boolean writeFile(String filePath, Boolean encode) {

		    	// encoding format to text file
		    	if(encode) 
		    	{
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
		    		
		    		// pass full encoderDecoder array to this method that will return a new array with the decoded text built into readable text
		    		String res[] = buildPrefixSuffix(encoderDecoderResult);
		    		String res1[] = buildPunct(res);
		    		
		    		
		    		for (int i = 0; i < res1.length; i++) 
		    		{
		    			if (res1[i] != null) 
		    			{
		    				System.out.println(res1[i]);
		    				writeToText(res1[i], filePath);
		    				
		    			}
		    		}
		    	}
		    	
		    	
		    	//output.write(finalResults[i] + " ");
		   
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
		

		
	
	
	private static String[] buildPrefixSuffix(String finalResult[]) { 
		
		String temp;
		String[] tempArr = new String[100000]; 
		
	    for (int i = 0; i < finalResult.length; i++) 
	    {
	        if (finalResult[i] != null) 
	        {
	            if (!finalResult[i].startsWith("@@")) 
	            {
	                if (i + 1 < finalResult.length && finalResult[i + 1] != null && finalResult[i+1].startsWith("@@")) 
	                {
	                    temp = finalResult[i + 1].replace("@@", "").trim();
	                    tempArr[i] = finalResult[i] + temp;
	                    i++; 
	                } 
	                else 
	                {
	                    
	                    tempArr[i] = finalResult[i];
	                }
	            }
	
	        }
	    }
		
		 return tempArr;
	}
	
	
	
	private static String[] buildPunct(String[] finalResult) {
	    String temp;
	    String[] tempArr = new String[100000];
	    
	    for (int i = 0; i < finalResult.length; i++) 
	    {
	        if (finalResult[i] != null) 
	        {
	            if (i + 1 < finalResult.length && finalResult[i + 1] != null && finalResult[i + 1].matches("\\p{Punct}")) 
	            {
	                temp = finalResult[i + 1].trim();
	                tempArr[i] = finalResult[i] + temp;
	                i++; // Skip the punctuation
	            } 
	            else 
	            {
	                tempArr[i] = finalResult[i];
	            }
	        }
	    }
	    
	    return tempArr;
	}

	
	
	
}
