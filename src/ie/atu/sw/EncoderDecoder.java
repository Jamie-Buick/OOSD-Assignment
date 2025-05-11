package ie.atu.sw;

// Decoding and Encoding class 
public class EncoderDecoder {
	
	public static String[] encodedWords = new String[10];
	
	public static void encode(String s) {
		int counter = 0;
		
	   
		for(int rows = 0; rows < EncodingFileProcessor.encodings.length; rows++) {
			
			if(EncodingFileProcessor.encodings[rows][0].equals(s)) 
			{
				
				encodedWords[counter] = EncodingFileProcessor.encodings[rows][1];
				//System.out.println(EncodingFileProcessor.encodings[rows][1]);
				counter++;
			}
			
		
		}
		
		
		
		
		for (int i = 0; i < encodedWords.length; i++)
		{
			System.out.println(encodedWords[i]);
		}
		
		/*
		for (int cols = 0; cols < 2; cols++) {
			
			System.out.print(EncodingFileProcessor.encodings[rows][cols] + "		" );
		}
		System.out.println("");
		
		 */		
		//return "hello";
	
	}
	
	
	
	
	public String decode(String s) {
		return null;
	
	}
	
	
}
