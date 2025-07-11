package ie.atu.sw;

// Decoding and Encoding class 
public class EncoderDecoder {


	/**
	 * Encodes an array of words based on the encodings-10000.csv. Words are encoded by full matches, prefix/suffix matches 
	 * and punctuation matches.
	 * 
	 * The encoding process involves:
	 * 
	 * - Detecting newlines
	 * - Detecting and separating leading and trailing punctuation 
	 * - Attempting to a 'full match' encoding
	 * - Attempting a prefix-suffix encoding
	 * - Handle words/ punctuation with no match
	 * - Constructing a clean, trimmed array of encoded words/punctuation that can be written to a .txt file
	 *
	 * @param input An array of words to be encoded.
	 * @return A trimmed array of encoded words and punctuation.
	 */

	public static String[] encode(String[] input) {

		String word = null;
		String[] encodedWords = new String[5];
		String[] puncStart = new String[1];
		String[] puncEnd = new String[2];
		String puncStartEncoded = null;
		String puncEndEncoded1 = null;
		String puncEndEncoded2 = null;

		String fullMatchEncoded = null;
		String prefixWord = null;
		String prefixEncoded = null;
		String suffixEncoded = null;
		Boolean newLine = null;
		
 
        String[] encodings = new String[50];
        String[] result;
        int resultCounter = 0;

        for (int x = 0; x < input.length; x++)
		{

			int counter = 0;
			
		    puncStartEncoded = null;
		    puncEndEncoded1 = null;
		    puncEndEncoded2 = null;
		    fullMatchEncoded = null;
		    prefixEncoded = null;
		    suffixEncoded = null;
		    newLine = false;
		    
		    word = input[x];

		    if(word != null) {

				if(isNewLine(word)) 
				{
					newLine = true;
				}
				else
				{
					newLine = false;
				}
		
				if (startsWithPunctuation(word) && !word.startsWith("@@"))
				{
					puncStart = getPunctuation(word, true);
					puncStartEncoded = matchPunctuation(puncStart[0]);
	
					word = stripStartPunctuation(word);
	
				}
	
				if(endsWithPunctuation(word)) {
	
					puncEnd = getPunctuation(word, false);
					word = stripPunctuation(word);
	
					puncEndEncoded1 = matchPunctuation(puncEnd[0]);
	
					if (puncEnd[1] != null) 
					{
						puncEndEncoded2 = matchPunctuation(puncEnd[1]);
					}
	
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
		    }


			// Adding the encoded words into the encoded words/ punct array

			// Check if word starts with punctuation
			if(puncStartEncoded != null) 
			{
				encodedWords[counter] = puncStartEncoded;
				counter++;
			}


			// Full word encoding
			if (fullMatchEncoded != null) 
			{
				encodedWords[counter] = fullMatchEncoded;
				counter++;

				if (puncEndEncoded1 != null) 
				{
					encodedWords[counter] = puncEndEncoded1;
					counter++;
				}
				if (puncEndEncoded2 != null)
				{
					encodedWords[counter] = puncEndEncoded2;
					counter++;
				}
			} 


			else if (newLine) 
			{
				encodedWords[counter] = "\n";
				counter++;

				if (puncEndEncoded1 != null) 
				{
					encodedWords[counter] = puncEndEncoded1;
					counter++;
				}
				if (puncEndEncoded2 != null)
				{
					encodedWords[counter] = puncEndEncoded2;
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


				if (puncEndEncoded1 != null) 
				{
					encodedWords[counter] = puncEndEncoded1;
					counter++;
				}

				if (puncEndEncoded2 != null)
				{
					encodedWords[counter] = puncEndEncoded2;
					counter++;
				}
			}
		
				
            for (int i = 0; i < encodedWords.length; i++) 
            {
                if (encodedWords[i] != null) 
                {
                	
                  if (resultCounter >= encodings.length) 
                  {
                	  encodings = expandArray(encodings);
                  }
                	
		            encodings[resultCounter++] = encodedWords[i];
		            encodedWords[i] = null;
                }
            }
                  
		}
	
        result = trimArray(encodings);
         
		return result;
	}
	
	
	
	/**
	 * Decodes an array of words based on the encodings-10000.csv. Words / punctuation are decoded and added to an array.
	 * This array holds separate words and punctuation that may need formatted. Newlines are maintained throughout this, prefix-suffix 
	 * words are joined and any punctuation is added back to maintain as close as possible format to the original file. 
	 * 
	 * 
	 * The decoding process involves:
	 * 
	 * - Detecting newlines
	 * - Matching the 'encoding' to the word / punctuation 
	 * - Constructing a clean, trimmed array of correctly structured words/punctuation that can be written to a .txt file
	 *
	 * @param input An array of encodings to be decoded.
	 * @return A trimmed array of constructed words and punctuation.
	 */

	public static String[] decode(String[] input) {
		
		String[] decodedWords = new String[1];
		String[] decodings = new String[50];
		String[] result;
		
		String match = null;
		String encoded = null;
		String fullMatch = null;
		
		int resultCounter = 0;
		
	   for (int x = 0; x < input.length; x++)
	   {
		   if(input[x] != null) {
		  	// New line
				if(isNewLine(input[x])) 
				{
					fullMatch = "\n";
				}
				else {
					// Find a match for 
					for (int rows = 0; rows < ReadEncodingsFile.getEncodings().length; rows++) {
			
						match = ReadEncodingsFile.getEncodings()[rows][0];
						encoded = ReadEncodingsFile.getEncodings()[rows][1];
			
						if (ReadEncodingsFile.getEncodings()[rows][1].equals(input[x])) 
						{
							fullMatch = match;
							break;
						}
					}	
				}
		   }
			
			
           for (int i = 0; i < decodedWords.length; i++) 
           {
               if (fullMatch != null) 
               {
               	
                 if (resultCounter >= decodings.length) 
                 {
                	 decodings = expandArray(decodings);
                 }
               	
                 decodings[resultCounter++] = fullMatch;
                 fullMatch = null;
               }
           }
	     

		   }
	   
		// pass full encoderDecoder array to this method that will return a new array
		// with the decoded text built into readable text
		String buildPartialWords[] = buildPrefixSuffix(decodings);
		String removeNullVals[] = removeNulls(buildPartialWords);
		String buildPunctuation[] = buildPunct(removeNullVals);

		result = trimArray(buildPunctuation);
		
	   return result;
	}
	
	
	
	
	/*
	 * Below are methods specifically related to encoding the words.
	 */

	
	/**
	 * Searches the encoding map for a full-word match and returns its encoding.
	 *
	 * This method performs a linear search through the encoding list and returns the encoded value
	 * if the input word exactly matches a word in the encoding file.
	 *
	 * @param word The word or punctuation to be matched against the encoding map.
	 * @return The encoded word or punctuation, defaults to '0' if no match is found.
	 */
	
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
		
		return fullEncodingMatch;
	}
	
	
	
	/**
	 * Searches the encoding map for a prefix-word match and returns its encoding.
	 *
	 * This method performs a linear search through the encoding list and returns the encoded value
	 * if the input word matches a prefix word in the encoding file. 
	 *
	 * @param word The word or punctuation to be matched against the encoding map.
	 * @return String array of size 2
	 * 				- Index 1 - The prefix word
	 * 				- Index 2 - The encoding of the prefix word
	 */

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
		
		return new String[] {nextPrefixMatch, matchedPrefixEncoding};
	}
	
	
	
	
	/**
	 * Searches the encoding map for a suffix-word match and returns its encoding.
	 *
	 *  This method performs a linear search through the encoding list, checking for suffix entries 
	 *  with "@@" as a prefix. The "@@" is stripped and the {@code endsWith} function is used to find the 
	 *  best match. The prefix is concatenated with suffix to reconstruct the original word. If the reconstructed 
	 *  word matches the input word, the suffix and suffix encoding is returned.
	 *
	 * @param word The word or punctuation to be matched against the encoding map.
	 * @param prefixWord The prefix is used to ensure the suffix found matches the full word.
	 * @return Encoding of the matching suffix
	 */
	
	private static String matchSuffix(String word, String prefixWord) {
		String match = "";
		String encoded = "";
		
		String nextSuffixMatch = null;
		String matchedSuffixEncoding = null;
		
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
	
		return matchedSuffixEncoding;
	}
	
	
	
	/**
	 * Checks whether the word starts with a punctuation character.
	 *
	 * This method returns {@code true} if the first character of the word is punctuation,
	 * based on Unicode punctuation character matching.
	 *
	 * @param word The input word to check.
	 * @return {@code true} if the word starts with punctuation; {@code false} otherwise.
	 */
	
	private static boolean startsWithPunctuation(String word) {
		
		char firstChar = word.charAt(0);
		
		if((String.valueOf(firstChar).matches("\\p{Punct}")))
		{
			 return true;
		}
		
		return false;
		
	}

	
	
	/**
	 * Checks whether the word ends with a punctuation character.
	 *
	 * This method returns {@code true} if the last character of the word is punctuation,
	 * based on Unicode punctuation character matching.
	 *
	 * @param word The input word to check.
	 * @return {@code true} if the word ends with punctuation; {@code false} otherwise.
	 */
	
	private static boolean endsWithPunctuation(String word) {
		
		char lastChar = word.charAt(word.length() - 1);
		
		if(String.valueOf(lastChar).matches("\\p{Punct}"))
		{
			return true;
		}
		
		return false;	
	}
	
	
	
	/**
	 * Returns the punctuation that has been found in the word 
	 *
	 * This method finds and returns punctuation found at the start or the end of the 
	 * word depending on the {@code isStart} flag. This method can account for 1 punctuation 
	 * character at the start of the word and 2 punctuation characters at the end of the word.
	 *
	 * @param word The input word to check.
	 * @param isStart if the punctuation is t the start of the word
	 * @return String array of size 1 or 2, depending if punctuation is at the start or end,
	 * 		   containing extracted punctuation. Unused positions will be {@code null}.
	 * 		   puncStart 		
	 * 				- Index 0 - punctuation
	 * 		   puncEnd
	 * 				- Index 0 - punctuation at word.length-1
	 * 				- Index 1 - punctuation at word.length-2
	 */

	private static String[] getPunctuation(String word, boolean isStart) {
		
		if (isStart) 
		{
			String[] puncStart = new String[1];
			
			puncStart[0] = String.valueOf(word.charAt(0));
			
			return puncStart;
		} 
		else 
		{
			String[] puncEnd = new String[2];
			int counter = 0;
			
			for (int i = word.length() - 2; i < word.length(); i++) { // starting at 1 because I will have already checked the first index above
				
				char c = word.charAt(i);
			
				if(String.valueOf(c).matches("\\p{Punct}"))
				{
					puncEnd[counter] = String.valueOf(c);
					 counter++;
				}
			}
			
		  return puncEnd;
		}
	}
	
	
	
	/**
	 * Strips the word of starting punctuation
	 *
	 * This method returns the word after stripping any starting punctuation. 
	 * This allows the 'clean' word to be processed further.
	 *
	 * @param word The input word strip punctuation from.
	 * @return {@code word.substring(1)}. The rest of the word except the first character.
	 */
	
	private static String stripStartPunctuation(String word) {  

	    return word.substring(1);	
	}
	
	
	
	/**
	 * Strips the word of ending punctuation
	 *
	 * This method returns the word after stripping any ending punctuation. 
	 * This allows the 'clean' word to be processed further.
	 *
	 * @param word The input word strip punctuation from.
	 * @return The word with all punctuation characters removed.
	 */

	private static String stripPunctuation(String word) { 
		StringBuilder noPunctuation = new StringBuilder();
	
		for (int i = 0; i < word.length(); i++) {
			
			char c = word.charAt(i);
			
			if(!String.valueOf(c).matches("\\p{Punct}"))
			{
				noPunctuation.append(c);
			}
		}
		
		  return noPunctuation.toString();
	}
	
	

	/**
	 * Matches a punctuation character against the encoding map and returns its encoding.
	 *
	 * This method performs a linear search through the encodings to find a punctuation
	 * character that exactly matches the input string {@code punc}. If a match is found,
	 * the corresponding encoding is returned. If no match is found, it returns "0".
	 *
	 * @param punc The punctuation character to be matched.
	 * @return The encoding of the punctuation if found; otherwise "0".
	 */

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
			else
			{
				puncEncodingMatch = "0";
			}
		}

		return puncEncodingMatch;
	}
	
	
	
	
	/*
	 * Below are methods specifically related to Decoding the words.
	 */
	
	/**
	 * Reconstructs full words from a prefix and a suffix encoding.
	 *
	 * This method processes the decoded words where suffixes are indicated by the "@@" prefix.
	 * It detects prefix-suffix pairs and concatenates them to form the original word.
	 * If no suffix follows a prefix, the prefix is added to the result as a normal word.
	 *
	 * Example input: {"walk", "@@ing"}
	 * Example output: {"walking"}
	 *
	 * @param inputWords The array of decoded strings (decoded words).
	 * @return A new array with prefix and suffix parts merged into complete words.
	 */

	private static String[] buildPrefixSuffix(String inputWords[]) { 
		
		String[] joinedWords = new String[inputWords.length]; 
		String temp;

	    for (int i = 0; i < inputWords.length; i++) 
	    {
	        if (inputWords[i] != null) 
	        {
	            if (!inputWords[i].startsWith("@@")) 
	            {
	                if (i + 1 < inputWords.length && inputWords[i + 1] != null && inputWords[i+1].startsWith("@@")) 
	                {
	                    temp = inputWords[i + 1].replace("@@", "").trim();
	                    joinedWords[i] = inputWords[i] + temp;
	                    i++; 
	                } 
	                else 
	                { 
	                	joinedWords[i] = inputWords[i];
	                }
	            }
	        }
	    }
	    
		 return joinedWords;
	}
	
	
	
	/**
	 * Reconstructs words with their punctuation from an input array.
	 *
	 * This method scans an array of strings (decoded words) and merges each word with up to two 
	 * punctuation characters that immediately follow it. It returns a new array where words and 
	 * their punctuation (e.g., "hello" + "," => "hello,") are joined properly. Remaining nulls from 
	 * the array are trimmed at the end.
	 *
	 *
	 * @param inputWords The array of strings containing decoded words and punctuation.
	 * @return A new array where punctuation has been joined to their corresponding words.
	 */
	
	private static String[] buildPunct(String[] inputWords) {
	  
	    String[] joinedPunct = new String[inputWords.length];
	    String temp;
	    
	    for (int i = 0; i < inputWords.length; i++) 
	    {
	        if (inputWords[i] != null) 
	        {
	            if (i + 1 < inputWords.length && inputWords[i + 1] != null && inputWords[i + 1].matches("\\p{Punct}")) 
	            {
	                // Check if next next word is also punctuation
	                if (i + 2 < inputWords.length && inputWords[i + 2] != null && inputWords[i + 2].matches("\\p{Punct}")) 
	                {
	                    // Two punctuation marks after the word
	                    joinedPunct[i] = inputWords[i] + inputWords[i + 1].trim() + inputWords[i + 2].trim();
	                    i += 2; // Skip both punctuation marks
	                }
	                else 
	                {
	                    // Only one punctuation mark after the word
	                    temp = inputWords[i + 1].trim();
	                    joinedPunct[i] = inputWords[i] + temp;
	                    i++; // Skip the one punctuation mark
	                }
	            } 
	            else 
	            {
	                joinedPunct[i] = inputWords[i];
	            }
	        }
	    }
	    
	    return joinedPunct;
	}
	
	
	
	/**
	 * Moves all non-null elements to the front of the array, pushing null elements to the end.
	 *
	 * This method creates a new array of the same size, copying all non-null values from 
	 * the input array in order. Any remaining positions will be {@code null}.
	 *
	 * @param original The input array to process.
	 * @return cleanedArr A new array with non-null elements at the front and nulls at the end.
	 */

	private static String[] removeNulls(String[] original) {

		String[] cleanedArr = new String[original.length];

		int counter = 0;
		for (int i = 0; i < original.length; i++) 
		{
			if (!(original[i] == null)) 
			{
				cleanedArr[counter] = original[i];
				counter++;
			}
		}
		
		return cleanedArr;
	}
	
	

	
	/*
	 * Below are methods used for Encoding and Decoding methods.
	 */

	/**
	 * Finds the @@newline token used to detect a new line
	 *
	 * This method returns true if the word is equal to newline token.
	 *
	 * @param word The input word to check.
	 * @return {@code true} if the word equals '@@newline'; {@code false} otherwise.
	 */
	
	private static boolean isNewLine(String word) {
		
		if(word.trim().equals("@@newline"))
		{
			return true;	
		}
		
		return false;
	}
	
	
	
	/**
	 * Trims nulls and empty elements from an array
	 *
	 * This method returns a new array that only has legitimate elements.
	 * all null valves and empty elements are removed.
	 *
	 * @param orignal The input array to trim.
	 * @return trimmedArray a trimmed array with valid data only.
	 */
	
	private static String[] trimArray(String [] original) {
		
		int count = 0;

		for (int i = 0; i < original.length; i++)
		{
			
			if (original[i] != null && !original[i].equals("")) 
			{
				count++;
			}
		}

	    String[] trimmedArray = new String[count];
	    int index = 0;
	
		for (int i = 0; i < original.length; i++)
		{
			
			if (original[i] != null && !original[i].equals("")) 
			{
				trimmedArray[index++] = original[i];
			}
			
		}
	
		return trimmedArray;
	}
	
	
	
	/**
	 * Expands the array dynamically
	 *
	 * This method returns a new array that is x2 larger than the input array.
	 * The output array contains the contents of the original array plus empty 
	 * elements.
	 *
	 * @param orignal The input array to expand.
	 * @return bigger an array double the size of the original.
	 */
	
	private static String[] expandArray(String[] original) {

		String[] bigger = new String[original.length * 2];

		for (int i = 0; i < original.length; i++) 
		{
			bigger[i] = original[i];
		}

		return bigger;
	}


}
