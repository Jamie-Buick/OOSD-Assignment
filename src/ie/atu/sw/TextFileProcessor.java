package ie.atu.sw;

import java.io.*;

// Processing of input files
public class TextFileProcessor {
	
	//private EncoderDecoder EncoderDecoder;
	
	
	String[] finalResults = new String[25]; // Choose a size big enough for your use case
	
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
				                finalResults[counter] = encoderDecoderReturn[i];
				                System.out.println(finalResults[i]);
				                counter++;
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
		// I can do all over the processing for writing the files here, where the finalResults array just needs made into a file.
		Boolean writeFinished = false;
		 // Print results
		
		try 
		{
			BufferedWriter output = new BufferedWriter(new FileWriter(filePath));
			for (int i = 0; i < finalResults.length; i++) {
				//System.out.println(finalResults[i]);
			    if (finalResults[i] != null) {
			    	//System.out.println(finalResults[i]);
			    	
			    	// encoding format to text file
			    	if(encode) 
			    	{
			    		output.write(finalResults[i] + " ");
			    	}
			    	// decoding format to text file
			    	else
			    	{
			    		
			    		// I possibly need two loops? one to be one step ahead and checking the next value?
			    		if(finalResults[i].startsWith("@@"))
			    		{
			    			String suffix = finalResults[i].replace("@@", "").trim();
			    			output.write(suffix + " ");
			    		}
			    		else
			    		{
			    			if (finalResults[i].startsWith("@@")) // this gives a nullpointerexception
			    			{
			    				output.write(finalResults[i]);
			    			}
			    			else
			    			{
			    				output.write(finalResults[i] + " ");
			    			}
			    			
			    		}
			    	}
			    	
			    	
			    	//output.write(finalResults[i] + " ");
			   
			    }
			}
			
			output.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
		

		return writeFinished;
	}
	
}
