package ie.atu.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class EncodingFileProcessor {
	
	//private String[][] encodings = null;

	public EncodingFileProcessor() {
		
	}

	public void parseEncoding(String fileName) {
		String line = null;
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File (fileName)))))
		{
			while((line = br.readLine()) != null) {
				System.out.println(line);
			}
			
		}
		catch(Exception e) 
		{
			System.out.println(e.getStackTrace());
		}
	}
}
