package ie.atu.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class EncodingFileProcessor {
	
	private String[][] encodings = new String[10000][2];

	public EncodingFileProcessor() {
		
	}

	public void parseEncoding(String fileName) {
		String line = null;
		int index = 0;
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File (fileName)))))
		{
			while((line = br.readLine()) != null) {
				
				String[] data = line.split(",");
				
				encodings[index][0] = data[0];
				encodings[index][1] = data[1];
			
				index++;
				
			}
			
		}
		catch(Exception e) 
		{
			System.out.println(e.getStackTrace());
		}
		
		// For in loop or for each loop. Doesn't have indexing 
		for(int rows = 0; rows < encodings.length; rows++) {
			for (int cols = 0; cols < 2; cols++) {
				System.out.println(encodings[rows][cols]);
			}
			
		}
		
		
		
	}
}
