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
		

		String nextPuncMatch = null;
		String matchedPuncEncoding = null;
		
		String baseWord = word;
		String punctuation = "";
		char lastChar = word.charAt(word.length() - 1);
		
		
		String match = null;
		String encoded = null;
		

		
		// Check if word ends with punctuation first
		if (String.valueOf(lastChar).matches("\\p{Punct}")) {

		    baseWord = word.substring(0, word.length() - 1).trim();
		    punctuation = String.valueOf(lastChar).trim();
		    System.out.println(punctuation);

			// Loop once, match both base and punctuation
			for (int k = 0; k < EncodingFileProcessor.getEncodings().length; k++) {
			    match = EncodingFileProcessor.getEncodings()[k][0];
			    encoded = EncodingFileProcessor.getEncodings()[k][1];
		
			    // Match base word
			    if (match.equals(baseWord)) {
			        if (nextPrefixMatch == null || match.length() > nextPrefixMatch.length()) {
			            nextPrefixMatch = match;
			            matchedPrefixEncoding = encoded;
			        }
			    }
		
			    // Match punctuation (if applicable)
			    if (!punctuation.isEmpty() && match.equals(punctuation)) {
			        nextPuncMatch = match;
			        matchedPuncEncoding = encoded;
			    }
			}
		}
		else
		{
			// Get full match where word is an exact match, no punctuation
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
		
		// We will need to consider punct somewhere here
		for (int j = 0; j < EncodingFileProcessor.getEncodings().length; j++) {

			match = EncodingFileProcessor.getEncodings()[j][0];
			encoded = EncodingFileProcessor.getEncodings()[j][1];
			

			if (EncodingFileProcessor.getEncodings()[j][0].startsWith("@@")) 
			{
				String suffixStrip = EncodingFileProcessor.getEncodings()[j][0].replace("@@", "").trim();
				String concatFullWord = nextPrefixMatch.concat(suffixStrip).trim();
		

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
