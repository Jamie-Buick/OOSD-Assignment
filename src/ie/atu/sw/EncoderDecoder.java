package ie.atu.sw;

// Decoding and Encoding class 
public class EncoderDecoder {



	public static String[] encode(String word) {
		String[] encodedWords = new String[2];
		int counter = 0;

		String fullMatch = null;
		String fullMatchEncoding = null;

		String nextPrefixMatch = null;
		String matchedPrefixEncoding = null;

		String nextSuffixMatch = null;
		String matchedSuffixEncoding = null;
		
		String match = null;
		String encoded = null;

		// Get full match where word is an exact match
		for (int rows = 0; rows < EncodingFileProcessor.getEncodings().length; rows++) {

			match = EncodingFileProcessor.getEncodings()[rows][0];
			encoded = EncodingFileProcessor.getEncodings()[rows][1];

			if (EncodingFileProcessor.getEncodings()[rows][0].equals(word)) 
			{
				fullMatch = match;
				fullMatchEncoding = encoded;
				
				break;
			}
			
		}	
		
		// Get best match for first part of word
		for (int i = 0; i < EncodingFileProcessor.getEncodings().length; i++) 
		{
			if (word.startsWith(EncodingFileProcessor.getEncodings()[i][0])) 
			{
				match = EncodingFileProcessor.getEncodings()[i][0];
				encoded = EncodingFileProcessor.getEncodings()[i][1];
				
				if (nextPrefixMatch == null || match.length() > nextPrefixMatch.length()) 
				{
					nextPrefixMatch = match;
					matchedPrefixEncoding = encoded;
				}
			}
		}
			
		for (int j = 0; j < EncodingFileProcessor.getEncodings().length; j++) {

			match = EncodingFileProcessor.getEncodings()[j][0];
			encoded = EncodingFileProcessor.getEncodings()[j][1];

			if (EncodingFileProcessor.getEncodings()[j][0].startsWith("@@")) 
			{
				String prefixStrip = EncodingFileProcessor.getEncodings()[j][0].replace("@@", "").trim();
				String concatFullWord = nextPrefixMatch.concat(prefixStrip).trim();

				if (word.endsWith(prefixStrip) && concatFullWord.equals(word.trim())) 
				{

					if (nextSuffixMatch == null || match.length() > nextSuffixMatch.length()) 
					{
						nextSuffixMatch = match;
						matchedSuffixEncoding = encoded;
					}
					
					break;
				}
			}
		}
			
	
		if (fullMatch != null) {
			encodedWords[counter] = fullMatchEncoding;
			counter++;
		} else {
			if (nextPrefixMatch != null) {
				// System.out.println(nextPrefixMatch + "" + matchedPrefixEncoding);
				encodedWords[counter] = matchedPrefixEncoding;
				// System.out.println(EncodingFileProcessor.encodings[rows][1]);
				counter++;
			}

			if (nextSuffixMatch != null) {
				// System.out.println(nextSuffixMatch + "" + matchedSuffixEncoding);
				encodedWords[counter] = matchedSuffixEncoding;
				// System.out.println(EncodingFileProcessor.encodings[rows][1]);
				counter++;
			}
		}

		/*
		for (int i = 0; i < counter; i++)
		{
			System.out.println(encodedWords[i]);
		}
		*/
		return encodedWords; // its fine here
		
	}




	public static String[] decode(String encodedWord) {
		
		String[] decodedWords = new String[1];
	
		String match = null;
		String encoded = null;
		String fullMatch = null;
		
		// Find a match for 
		for (int rows = 0; rows < EncodingFileProcessor.getEncodings().length; rows++) {

			match = EncodingFileProcessor.getEncodings()[rows][0];
			encoded = EncodingFileProcessor.getEncodings()[rows][1];

			if (EncodingFileProcessor.getEncodings()[rows][1].equals(encodedWord)) 
			{
				fullMatch = match;
				break;
			}
		}	
		
		if (fullMatch != null) {
			decodedWords[0] = fullMatch;
		}
		

		/*
		for (int i = 0; i < 1; i++)
		{
			System.out.println(decodedWords[i]);
		}
		*/
		return decodedWords;
		
	}

}
