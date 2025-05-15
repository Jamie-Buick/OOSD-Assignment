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
							word = word.strip().replaceAll("[^a-zA-Z ]", "");
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
				                //System.out.println(finalResults[i]);
				                counter++;
				            }
				        }
				  
					}	
			}
			
			 // Print results
	        for (int j = 0; j < finalResults.length; j++) {
	              System.out.println("Final:" + finalResults[j]);
	        }
			  
			br.close();
			readFinished = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return readFinished;

	}	
	
	public boolean writeFile(String filePath) {
		// I can do all over the processing for writing the files here, where the finalResults array just needs made into a file.
		Boolean writeFinished = false;
		 // Print results
		
      
		
		
		return writeFinished;
	}
	
}
