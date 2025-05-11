package ie.atu.sw;

import java.io.*;

// Processing of input files
public class InputFileProcessor {
	
	private EncoderDecoder EncoderDecoder = new EncoderDecoder();
	String line = null;
	
	
	
	
	public void readFile(String filePath) {
		filePath = "./HappyPrinceWilde.txt";
		try 
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
	
			while((line = br.readLine()) != null) {
					
				String[] words = line.split(" ");
				
					for (String word : words) {
						word = word.strip().replaceAll("[^a-zA-Z ]", "");
						EncoderDecoder.encode(word);
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
