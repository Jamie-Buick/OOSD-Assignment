package ie.atu.sw;

// Decoding and Encoding class 
public class EncoderDecoder {

	private static String[] encodedWords = new String[15];

	public static void encode(String s) {
		int counter = 0;

		String fullMatch = null;
		String fullMatchEncoding = null;
		
		String nextPrefixMatch = null;
		String matchedPrefixEncoding = null;
		
		String nextSuffixMatch = null;
		String matchedSuffixEncoding = null;


		for(int rows = 0; rows < EncodingFileProcessor.getEncodings().length; rows++) {
			
			String match = EncodingFileProcessor.getEncodings()[rows][0];
			String encoded = EncodingFileProcessor.getEncodings()[rows][1];

		
			if(EncodingFileProcessor.getEncodings()[rows][0].equals(s)) 
			{
				fullMatch = match;
				fullMatchEncoding = encoded;
				//System.out.println(match + "" + encoded);
				//System.out.println(EncodingFileProcessor.encodings[rows][1]);
			
			
			}
			
				
			if (s.startsWith(EncodingFileProcessor.getEncodings()[rows][0]))
			{
				if(nextPrefixMatch == null || match.length() > nextPrefixMatch.length())
				{
					nextPrefixMatch = match;
					matchedPrefixEncoding = encoded;
	
				}
			}
		
				
				
			if (EncodingFileProcessor.getEncodings()[rows][0].startsWith("@@"))    // && isStringLengthEqual(EncodingFileProcessor.getEncodings()[rows][0], s) ))
			{
				String prefixStrip = EncodingFileProcessor.getEncodings()[rows][0].replace("@@", "");
				
				if (s.endsWith(prefixStrip))
				{
				
					if(nextSuffixMatch == null || match.length() > nextSuffixMatch.length()) 
					{
						nextSuffixMatch = match;
						matchedSuffixEncoding = encoded;
					}
				}
			}
			
		
		}
		
		if (fullMatch != null) 
		{
			encodedWords[counter] = fullMatchEncoding;
			counter++;
		}
		else
		{
			if(nextPrefixMatch != null)
			{
				//System.out.println(nextPrefixMatch + "" + matchedPrefixEncoding);
				encodedWords[counter] = matchedPrefixEncoding;
				//System.out.println(EncodingFileProcessor.encodings[rows][1]);
				counter++;
			}
			
			if(nextSuffixMatch != null)
			{
				//System.out.println(nextSuffixMatch + "" + matchedSuffixEncoding);
				encodedWords[counter] = matchedSuffixEncoding;
				//System.out.println(EncodingFileProcessor.encodings[rows][1]);
				counter++;
			}
		}

		


	

		for (int i = 0; i < counter; i++)
		{
			System.out.println(encodedWords[i]);
		}
		
	}





	public String decode(String s) {
		return null;

	}


}
