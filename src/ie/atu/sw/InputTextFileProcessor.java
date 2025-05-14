package ie.atu.sw;

import java.io.*;

// Processing of input files
public class InputTextFileProcessor {
	
	//private EncoderDecoder EncoderDecoder;
	String line = null;
	

	
	public void readFile(String filePath, Boolean encode) {
		try 
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
	
			while((line = br.readLine()) != null) {
					
				String[] words = line.split(" ");
				
					for (String word : words) {
						if (encode)
						{
							word = word.strip().replaceAll("[^a-zA-Z ]", "");
							EncoderDecoder.encode(word);
						}
						else
						{
							word = word.strip().replaceAll("[^0-9 ]", "");
							EncoderDecoder.decode(word);
						}
					
						// I can now call the encodingdecoding class here because I have individual words
						//System.out.println(word);
					}
			}
			
			br.close();
	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		

	}	
	
}
