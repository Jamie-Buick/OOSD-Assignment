package ie.atu.sw;

import java.io.*;

// Processing of input files
public class InputFileProcessor {
	String line = null;
	
	
	public void readFile(String filePath) {
		try 
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
	
			while((line = br.readLine()) != null) {
				
					System.out.println(line);	
			}
			
			br.close();
	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		

	}	
	
}
