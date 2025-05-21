package ie.atu.sw;

// Decoding and Encoding class 
public class EncoderDecoder {

	// Notes:
	// - for punct word, I think we strip the punct and base word and then pass the punct through a punct encoder and pass base word 
	// matchfullword
	
	
	private static String matchFullWord(String word) {
		String match = "";
		String encoded = "";
		
		String fullWordMatch = "";
		String fullEncodingMatch = "";
		
		// Get full match where word is an exact match, no punctuation
		for (int rows = 0; rows < EncodingFileProcessor.getEncodings().length; rows++) {

			match = EncodingFileProcessor.getEncodings()[rows][0];
			encoded = EncodingFileProcessor.getEncodings()[rows][1];

			if (EncodingFileProcessor.getEncodings()[rows][0].equals(word)) 
			{
				fullWordMatch = match;
				fullEncodingMatch = encoded;
				
				break;
			}
		}
		//System.out.println(fullWordMatch);.
		
		return fullEncodingMatch;
	}
	
	private static String matchPrefix(String word) { 
		// Get best match for first part of word
		String match = "";
		String encoded = "";
		
		String nextPrefixMatch = null;
		String matchedPrefixEncoding = null;
		
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
		
		return matchedPrefixEncoding;
	}
	
	private static String matchSuffix(String word, String prefixWord) {
		String match = "";
		String encoded = "";
		
		String nextSuffixMatch = null;
		String matchedSuffixEncoding = null;
		
		// We will need to consider punct somewhere here
		for (int j = 0; j < EncodingFileProcessor.getEncodings().length; j++) {

			match = EncodingFileProcessor.getEncodings()[j][0];
			encoded = EncodingFileProcessor.getEncodings()[j][1];
			

			if (EncodingFileProcessor.getEncodings()[j][0].startsWith("@@")) 
			{
				String suffixStrip = EncodingFileProcessor.getEncodings()[j][0].replace("@@", "").trim();
				String concatFullWord = prefixWord.concat(suffixStrip).trim();
		

				if (word.endsWith(suffixStrip) && concatFullWord.equals(word.trim())) 
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
		return matchedSuffixEncoding;
	}
	
	
	
	
	
	private static boolean endsWithPunctuation(String word) {
		boolean endsWithPunctuation = false;
		char lastChar = word.charAt(word.length() - 1);
		
		if((String.valueOf(lastChar).matches("\\p{Punct}")))
		{
			 endsWithPunctuation = true;
		}
		
		return endsWithPunctuation;
		
	}
	
	
	private static String stripPunctuation(String word) { 
		  return word.substring(0, word.length() - 1).trim();
	}
	
	
	private static String getPunctuation(String word) {  
		char lastChar = word.charAt(word.length() - 1);
		
		return String.valueOf(lastChar).trim();
		
	}

	

	private static String matchPunctuation(String punc) {  
		String match = "";
		String encoded = "";
		
		String puncMatch = "";
		String puncEncodingMatch = "";
		
		// Get full match where word is an exact match, no punctuation
		for (int rows = 0; rows < EncodingFileProcessor.getEncodings().length; rows++) {

			match = EncodingFileProcessor.getEncodings()[rows][0];
			encoded = EncodingFileProcessor.getEncodings()[rows][1];

			if (EncodingFileProcessor.getEncodings()[rows][0].equals(punc)) 
			{
				puncMatch = match;
				puncEncodingMatch = encoded;
				
				break;
			}
		}
		//System.out.println(fullWordMatch);.
		
		return puncEncodingMatch;
	}
	
	

	public static String[] encode(String word) {
		String[] encodedWords = new String[2];
		int counter = 0;

		
	
		/*

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
		
			if (nextPuncMatch != null) {
				// System.out.println(nextSuffixMatch + "" + matchedSuffixEncoding);
				encodedWords[counter] = matchedPuncEncoding;
				// System.out.println(EncodingFileProcessor.encodings[rows][1]);
				counter++;
			}

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
