package ie.atu.sw;

// Decoding and Encoding class 
public class EncoderDecoder {

	private static String[] encodedWords = new String[15];

	public static void encode(String word) {
		int counter = 0;
		
		
		String fullMatch = null;
		String fullMatchEncoding = null;
		
		String nextPrefixMatch = null;
		String matchedPrefixEncoding = null;
		
		String nextSuffixMatch = null;
		String matchedSuffixEncoding = null;

		// Get full match where word is an exact match
		for(int rows = 0; rows < EncodingFileProcessor.getEncodings().length; rows++) {
			
			String match = EncodingFileProcessor.getEncodings()[rows][0];
			String encoded = EncodingFileProcessor.getEncodings()[rows][1];

		
			if(EncodingFileProcessor.getEncodings()[rows][0].equals(word)) 
			{
				fullMatch = match;
				fullMatchEncoding = encoded;
				//System.out.println(match + "" + encoded);
				//System.out.println(EncodingFileProcessor.encodings[rows][1]);
				
				break;
			}
		
			
				
			
			if (word.startsWith(EncodingFileProcessor.getEncodings()[rows][0]))
			{
				if(nextPrefixMatch == null || match.length() > nextPrefixMatch.length())
				{
					nextPrefixMatch = match;
					matchedPrefixEncoding = encoded;
					//System.out.println(nextPrefixMatch);
	
				}
				
				
				for(int i = 0; i < EncodingFileProcessor.getEncodings().length; i++) {
					
					if(EncodingFileProcessor.getEncodings()[i][0].startsWith("@@"))
					{
						String prefixStrip = EncodingFileProcessor.getEncodings()[i][0].replace("@@", "").trim();
						
						String concatFullWord = nextPrefixMatch.concat(prefixStrip).trim();
						
			
						if (word.endsWith(prefixStrip) && concatFullWord.equals(word.trim()))
						{
							
							System.out.println("here!");
							if(nextSuffixMatch == null || match.length() > nextSuffixMatch.length()) 
							{
								nextSuffixMatch = match;
								matchedSuffixEncoding = encoded;
							}
						}
					}
				}
				
		
			}
			
			
			
			
			
		
	/*
			if (EncodingFileProcessor.getEncodings()[rows][0].startsWith("@@"))    // && isStringLengthEqual(EncodingFileProcessor.getEncodings()[rows][0], s) ))
			{
				String prefixStrip = EncodingFileProcessor.getEncodings()[rows][0].replace("@@", "").trim();
				//System.out.println(prefixStrip);
				String concatFullWord = nextPrefixMatch.concat(prefixStrip).trim();
				
				
				
				if (word.endsWith(prefixStrip) && concatFullWord.equals(word))
				{
					System.out.println(concatFullWord);
					if(nextSuffixMatch == null || match.length() > nextSuffixMatch.length()) 
					{
						nextSuffixMatch = match;
						matchedSuffixEncoding = encoded;
					}
				}
			}
	*/
			
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
