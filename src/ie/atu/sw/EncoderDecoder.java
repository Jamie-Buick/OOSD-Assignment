package ie.atu.sw;

// Decoding and Encoding class 
public class EncoderDecoder {

	// Notes:
	// - for punct word, I think we strip the punct and base word and then pass the punct through a punct encoder and pass base word 
	// matchfullword
	
	public static String[] encode(String word) {
		
		// This returns the multiple parts or single encoding. I have made this a max 3 here to account for prefix suffix and punc word
		String[] encodedWords = new String[3];
		String punc = null;
		String puncEncoded = null;
		String fullMatchEncoded = null;
		String prefixWord = null;
		String prefixEncoded = null;
		String suffixEncoded = null;
		Boolean newLine = null;
		

		int counter = 0;
		
		
		// New line
		if(newLine(word)) 
		{
			newLine = true;
		}
		
		if(endsWithPunctuation(word)) {
			punc = getPunctuation(word);
			word = stripPunctuation(word);
			
			puncEncoded = matchPunctuation(punc);
		}

		fullMatchEncoded = matchFullWord(word);


		// If a full match is not found then we check for a prefix - suffix match
		if(fullMatchEncoded == null)
		{
			String[] prefixResult = matchPrefix(word); 
			
			if (prefixResult[0] != null && prefixResult[1] != null)
			{
				prefixWord = prefixResult[0];
				prefixEncoded = prefixResult[1];
				
				suffixEncoded = matchSuffix(word, prefixWord); 
				
				if(suffixEncoded == null)
				{
					prefixEncoded = "0";
				}
			}
			else
			{
				fullMatchEncoded = "0"; // the code for a missing word.
			}
		}
		
		// Adding the encoded words into the encoded words/ punct array
		if (fullMatchEncoded != null) 
		{
			encodedWords[counter] = fullMatchEncoded;
			counter++;
			
			if (puncEncoded != null) 
			{
				encodedWords[counter] = puncEncoded;
				counter++;
			}
		} 
		else if (newLine != null) 
		{
			encodedWords[counter] = "\n";
			counter++;
			
			if (puncEncoded != null) 
			{
				encodedWords[counter] = puncEncoded;
				counter++;
			}
		}
		else 
		{
			if (prefixEncoded != null) 
			{
				encodedWords[counter] = prefixEncoded;
				counter++;
			}
		
			if (suffixEncoded != null) 
			{
				encodedWords[counter] = suffixEncoded;
				counter++;
			}
		
			if (puncEncoded != null) 
			{
				encodedWords[counter] = puncEncoded;
				counter++;
			}
		}

		return encodedWords; 
	}


	
	public static String[] decode(String encodedWord) {
		
		String[] decodedWords = new String[1];
	
		String match = null;
		String encoded = null;
		String fullMatch = null;
		
		// New line
		if(newLine(encodedWord)) 
		{
			decodedWords[0] = "\n";
		}
		else {
			// Find a match for 
			for (int rows = 0; rows < ReadEncodingsFile.getEncodings().length; rows++) {
	
				match = ReadEncodingsFile.getEncodings()[rows][0];
				encoded = ReadEncodingsFile.getEncodings()[rows][1];
	
				if (ReadEncodingsFile.getEncodings()[rows][1].equals(encodedWord)) 
				{
					fullMatch = match;
					break;
				}
			}	
		}
		
		if (fullMatch != null) {
			decodedWords[0] = fullMatch;
		}

		
		return decodedWords;
	}

	private static String matchFullWord(String word) {
		String match = "";
		String encoded = "";
		
		String fullWordMatch = null;
		String fullEncodingMatch = null;
		
		// Get full match where word is an exact match, no punctuation
		for (int rows = 0; rows < ReadEncodingsFile.getEncodings().length; rows++) {

			match = ReadEncodingsFile.getEncodings()[rows][0];
			encoded = ReadEncodingsFile.getEncodings()[rows][1];

			if (ReadEncodingsFile.getEncodings()[rows][0].equals(word)) 
			{
				fullWordMatch = match;
				fullEncodingMatch = encoded;
				
				break;
				
			}
		}
		//System.out.println(fullWordMatch);
		
		return fullEncodingMatch;
	}
	
	

	private static String[] matchPrefix(String word) { 
		// Get best match for first part of word
		String match = "";
		String encoded = "";
		
		String nextPrefixMatch = null;
		String matchedPrefixEncoding = null;
		
		for (int i = 0; i < ReadEncodingsFile.getEncodings().length; i++) 
		{
			if (word.startsWith(ReadEncodingsFile.getEncodings()[i][0]) && !ReadEncodingsFile.getEncodings()[i][0].equals(word))
			{
				match = ReadEncodingsFile.getEncodings()[i][0];
				encoded = ReadEncodingsFile.getEncodings()[i][1];
				
				if (nextPrefixMatch == null || match.length() > nextPrefixMatch.length()) 
				{
					nextPrefixMatch = match;
					matchedPrefixEncoding = encoded;
				}
			}
		}
		
		//System.out.println(matchedPrefixEncoding);
		return new String[] {nextPrefixMatch, matchedPrefixEncoding};
	}
	

	
	private static String matchSuffix(String word, String prefixWord) {
		String match = "";
		String encoded = "";
		
		String nextSuffixMatch = null;
		String matchedSuffixEncoding = null;
		
		// We will need to consider punct somewhere here
		for (int j = 0; j < ReadEncodingsFile.getEncodings().length; j++) {

			match = ReadEncodingsFile.getEncodings()[j][0];
			encoded = ReadEncodingsFile.getEncodings()[j][1];
			
			if (ReadEncodingsFile.getEncodings()[j][0].startsWith("@@")) 
			{
				String suffixStrip = ReadEncodingsFile.getEncodings()[j][0].replace("@@", "").trim();
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
		//System.out.println(matchedSuffixEncoding);
		return matchedSuffixEncoding;
	}
	
	
	
	private static boolean newLine(String word) {
		
		if(word.trim().equals("@@newline"))
		{
			return true;	
		}
		
		return false;
		
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
		for (int rows = 0; rows < ReadEncodingsFile.getEncodings().length; rows++) {

			match = ReadEncodingsFile.getEncodings()[rows][0];
			encoded = ReadEncodingsFile.getEncodings()[rows][1];

			if (ReadEncodingsFile.getEncodings()[rows][0].equals(punc)) 
			{
				puncMatch = match;
				puncEncodingMatch = encoded;
				
				break;
			}
		}

		return puncEncodingMatch;
	}
	


}
