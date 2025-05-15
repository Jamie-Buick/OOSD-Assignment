package ie.atu.sw;

import java.io.*;

// Processing of input files
public class TextFileProcessor {
	
	//private EncoderDecoder EncoderDecoder;
	
	

	
	public void readFile(String filePath, Boolean encode) {
		String line = null;
		String [] encoderDecoderReturn;
		String[] finalResults = new String[1000]; // Choose a size big enough for your use case
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
						
						
						
						  // Copy results into the main array
				        for (int i = 0; i < encoderDecoderReturn.length; i++) {
				            if (encoderDecoderReturn[i] != null) {
				                finalResults[counter] = encoderDecoderReturn[i];
				                System.out.println(finalResults[counter]);
				            }
				        }
				        counter++;
					}
			}
			
			br.close();
	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		

	}	
	
	public void writeFile(String filePath, Boolean encode) {
		
	}
	
}
