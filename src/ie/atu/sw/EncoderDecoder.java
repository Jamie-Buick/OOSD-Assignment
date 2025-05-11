package ie.atu.sw;

// Decoding and Encoding class 
public class EncoderDecoder {

	EncodingFileProcessor EncodingFileProcessor = new EncodingFileProcessor();
	String[][] matrix = new String[10000][2];
	
	public String encode(String s) {
		
	    matrix = EncodingFileProcessor.getEncodings();
	    
		for(int rows = 0; rows < matrix.length; rows++) {
			for (int cols = 0; cols < 2; cols++) {
				System.out.print(matrix[rows][cols] + "		" );
			}
			System.out.println("");
		}
		
		return null;
	
	}
	
	
	
	public String decode(String s) {
		return null;
	
	}
	
	
}
