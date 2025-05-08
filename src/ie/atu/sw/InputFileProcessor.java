package ie.atu.sw;

import java.io.*;

// Processing of input files
public class InputFileProcessor {
	String file = "./PictureOfDorianGrayWilde.txt";
	String line = null;
	
	
	public void readFile() {


		try 
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
	
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
