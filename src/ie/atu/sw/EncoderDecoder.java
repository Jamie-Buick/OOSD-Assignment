package ie.atu.sw;

// Decoding and Encoding class 
public class EncoderDecoder {

	// Global variables used to track type of encodings across encoding methods 
	private static int fullMatchCount = 0;
	private static int prefixSuffixCount = 0;
	private static int punctuationCount = 0;
	
	

	/**
	 * Encodes an array of words based on the encodings-10000.csv. Words are encoded by full matches, prefix/suffix matches 
	 * and punctuation matches.
	 * 
	 * The encoding process involves:
	 * 
	 * - Detecting newlines
	 * - Detecting and separating lone, leading and trailing punctuation 
	 * - Attempting to a 'full match' encoding
	 * - Attempting a prefix-suffix encoding
	 * - Handle words/ punctuation with no match
	 * - Constructing a clean, trimmed array of encoded words/punctuation that can be written to a .txt file
	 *
	 * @param input An array of words to be encoded.
	 * @return A trimmed array of encoded words and punctuation.
	 */
	public static String[] encode(String[] input) {

		// Input
		String word = null;
		Boolean newLine = null;

		// String array to hold parts of words i.e. prefix, suffix, punctuation 
		String[] encodedWords = new String[5];

		// Punctuation handling
		String[] puncStart = new String[1];
		String[] puncEnd = new String[2];
		String puncStartEncoded = null;
		String puncEndEncoded1 = null;
		String puncEndEncoded2 = null;

		// Encoded words
		String fullMatchEncoded = null;
		String prefixWord = null;
		String prefixEncoded = null;
		String suffixEncoded = null;

		// Final Encodings & results
		String[] encodings = new String[50];
		String[] result;
		int resultCounter = 0;

		for (int x = 0; x < input.length; x++)
		{
			// Reset encoding variables
			int counter = 0;
			fullMatchEncoded = null;
			prefixEncoded = null;
			suffixEncoded = null;
			newLine = false;
			puncStartEncoded = null;
			puncEndEncoded1 = null;
			puncEndEncoded2 = null;

			
			word = input[x];
			//System.out.println(word);

			if(word != null) {

				// Checks for a new line
				newLine = isNewLine(word);

				// Handle 'lone' punctuation 
				if (isLonePunctuation(word) && !word.startsWith("@@")){
					puncStart = getPunctuation(word, true);
					word = puncStart[0];
				}
				
			
				// Handle punctuation at the start of word 
				if (!isLonePunctuation(word) && startsWithPunctuation(word) && !word.startsWith("@@"))
				{
					// true used for isStart inside getPunctuation 
					puncStart = getPunctuation(word, true);
					puncStartEncoded = matchPunctuation(puncStart[0]);

					word = stripStartPunctuation(word);
				}

				
				/*
				 * Handle punctuation at the end of word, this handles up to two punctuation 
				 * characters at the end of a word
				 */
				if(!isLonePunctuation(word) && !word.isEmpty() && endsWithPunctuation(word)) 
				{
					// false used for isStart inside getPunctuation 
					puncEnd = getPunctuation(word, false);
					word = stripPunctuation(word);
					
					puncEndEncoded1 = matchPunctuation(puncEnd[0]);

					if (puncEnd[1] != null) 
					{
						puncEndEncoded2 = matchPunctuation(puncEnd[1]);
					}
				}
				
				if (word != null && word.trim().isEmpty()) {
					word = null;
				}

				if (word != null) {
					// Check if a full match can be found first
					fullMatchEncoded = matchFullWord(word);
				
	
					// If a full match is not found then we check for a prefix - suffix match
					if(fullMatchEncoded == null)
					{
						// String array returning the prefix word & prefix encoding
						String[] prefixResult = matchPrefix(word); 
	
						/*
						 * If the prefixResults returned valid data, it is split into prefixWord & prefixEncoding
						 * the suffixEncoded is returned by providing the original word and prefixWord to the 
						 * matchSuffix method. If no suffix is found, prefixEncoded is set to "0".
						 * 
						 * Else fullMatchEncoded is given "0" which means a missing word.
						 */
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
			}

			// Check if word starts with punctuation.
			if(puncStartEncoded != null) 
			{
				// Add to the encodedWords array & increment the counter.
				encodedWords[counter] = puncStartEncoded;
				counter++;
				punctuationCount++;
			}

			// Check if word encoding was a full match.
			if (fullMatchEncoded != null) 
			{
				// Add to the encodedWords array & increment the counter.
				encodedWords[counter] = fullMatchEncoded;
				counter++;
				fullMatchCount++;
				// Check if word ends with 1 punctuation character.
				if (puncEndEncoded1 != null) 
				{
					// Add to the encodedWords array & increment the counter.
					encodedWords[counter] = puncEndEncoded1;
					counter++;
					punctuationCount++;
				}

				// Check if word ends with 2 punctuation character.
				if (puncEndEncoded2 != null)
				{
					// Add to the encodedWords array & increment the counter.
					encodedWords[counter] = puncEndEncoded2;
					counter++;
					punctuationCount++;
				}
			} 
			// Check if the newLine variable is true
			else if (newLine) 
			{
				// Add the actual new line character to the array & increment the counter.
				encodedWords[counter] = "\n";
				counter++;

				// Check if word ends with 1 punctuation character.
				if (puncEndEncoded1 != null) 
				{
					// Add to the encodedWords array & increment the counter.
					encodedWords[counter] = puncEndEncoded1;
					counter++;
					punctuationCount++;
				}

				// Check if word ends with 2 punctuation character.
				if (puncEndEncoded2 != null)
				{
					// Add to the encodedWords array & increment the counter.
					encodedWords[counter] = puncEndEncoded2;
					counter++;
					punctuationCount++;
				}
			}
			else 
			{
				
				// Before adding prefix/suffix encodings, increment prefixSuffixCount once if either exists
				if (prefixEncoded != null || suffixEncoded != null) {
				    prefixSuffixCount++;  // Increment once per prefix-suffix word
				}
				
				// Check if word encoding was a prefix match.
				if (prefixEncoded != null) 
				{
					// Add to the encodedWords array & increment the counter.
					encodedWords[counter] = prefixEncoded;
					counter++;
				}

				// Check if word encoding was a suffix match.
				if (suffixEncoded != null) 
				{
					// Add to the encodedWords array & increment the counter.
					encodedWords[counter] = suffixEncoded;
					counter++;
				}

				// Check if word ends with 1 punctuation character.
				if (puncEndEncoded1 != null) 
				{
					// Add to the encodedWords array & increment the counter.
					encodedWords[counter] = puncEndEncoded1;
					counter++;
					punctuationCount++;
				}

				// Check if word ends with 1 punctuation character.
				if (puncEndEncoded2 != null)
				{
					// Add to the encodedWords array & increment the counter.
					encodedWords[counter] = puncEndEncoded2;
					counter++;
					punctuationCount++;
				}
			}

			/*
			 * After the word, parts of word, punctuation or newline is added to the encodedWords array it is looped over
			 * so that non-null entries can be added to the encodings array. If the encodings array will also dynamically
			 * increase once the number of elements added reaches the length of the array. After copying, encodedWords is 
			 * reset (each used element set to null).
			 * 
			 */
			for (int i = 0; i < encodedWords.length; i++) 
			{
				if (encodedWords[i] != null) 
				{
					// If the number of elements added reaches the length of the encodings array, dynamically expand.
					if (resultCounter >= encodings.length) 
					{
						encodings = expandArray(encodings);
					}

					// Copy encoded word and reset element.
					encodings[resultCounter++] = encodedWords[i];
					encodedWords[i] = null;
				}
			}     
		}
		
		// Remove any null or empty elements from the array
		result = trimArray(encodings);
		
		// Prints some stats about the encoding
		encodeStats(result);

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

		// Arrays for holding decoded words
		String[] decodedWords = new String[1];
		String[] decodings = new String[50];
		String[] result;

		String match = null;
		String fullMatch = null;

		int resultCounter = 0;

		/*
		 * Loop through the input array ensuring the element is not null. If a new line is 
		 * detected - assign the newline character. Otherwise, search the encodings array for
		 * a match. If no match is found, variable fullMatch will remain null.
		 *
		 */
		for (int x = 0; x < input.length; x++)
		{
			if(input[x] != null) {

				if(isNewLine(input[x])) 
				{
					fullMatch = "\n";
				}
				else {
					// Find a match for 
					for (int rows = 0; rows < ReadEncodingsFile.getEncodings().length; rows++) {

						match = ReadEncodingsFile.getEncodings()[rows][0];

						if (ReadEncodingsFile.getEncodings()[rows][1].equals(input[x])) 
						{
							fullMatch = match;
							break;
						}
					}	
				}
			}

			/*
			 * If a fullMatch has been found (not null). Add the fullMatch to the decodings array at the next element.
			 * If the decodings array is full it will be dynamically expanded.
			 * After adding, reset fullMatch to null to prepare for the next input.
			 *
			 */
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



		// Build prefix-suffix words.
		String buildPartialWords[] = buildPrefixSuffix(decodings);
		
		// Removes any null elements created during buildPrefixSuffix function.
		String removeNullVals[] = removeNulls(buildPartialWords);
		
		// Build any punctuation to the start or end of words.
		String buildPunctuation[] = buildPunct(removeNullVals);

		// Remove any null or empty elements from the array
		result = trimArray(buildPunctuation);

		// Prints some stats about the decoding
		decodeStats(result);
		
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
		
		/*
		 * Loop through the encodings array from ReadEncodingsFile.getEncodings() and find a match
		 * for the input word. If a match is found, the corresponding encoding is returned.
		 */
		for (int rows = 0; rows < ReadEncodingsFile.getEncodings().length; rows++) {

			match = ReadEncodingsFile.getEncodings()[rows][0];
			encoded = ReadEncodingsFile.getEncodings()[rows][1];

			if (match.equals(word)) 
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
		
		/*
		 * Loop through the encodings array from ReadEncodingsFile.getEncodings() and find the best 
		 * prefix match for the input word. The best prefix match is the longest prefix that the input 
		 * word starts with, excluding exact matches. The loop updates the best prefix match and its 
		 * encoding whenever a longer prefix is found.
		 * 
		 */
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
			
			// @@ is used to indicate that the element contains a legitimate suffix. 
			if (match.startsWith("@@")) 
			{
				
				/*
				 * Firstly, remove the @@ to get the actual suffix then concatenate the given prefix with the potential suffix. 
				 * Check if the word endsWith the suffix AND combined prefix+suffix equals the input word, then it is a valid
				 * suffix match. 
				 * 
				 */
				String suffixStrip = match.replace("@@", "").trim();
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
	 * Checks whether the word is a lone punctuation.
	 *
	 * This method returns {@code true} if the word is punctuation,
	 * based on Unicode punctuation character matching.
	 *
	 * @param word The input word to check.
	 * @return {@code true} if the word is punctuation; {@code false} otherwise.
	 */
	private static boolean isLonePunctuation(String word) {
		return word != null && word.matches("\\p{Punct}");	
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
		
		// Get the character at position 0 of the word.
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
		
		// Get the character at the last position of the word.
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
		
		// If the punctuation of located at the start of the word, add it to first element of the punctStart array.
		if (isStart) 
		{
			String[] puncStart = new String[1];
			
			puncStart[0] = String.valueOf(word.charAt(0));
			
			return puncStart;
		} 
		else 
		{
	        // Check up to the last two characters for punctuation at the end of the word.
			String[] puncEnd = new String[2];
			int counter = 0;
			
			/*
			 * Starting at the 2nd last character of the word. It will loop until the end of the word.
			 * Checking for punctuation and adding to the puncEnd array. I will only account for 2 
			 * punctuation characters at the end of a word.
			 * 
			 */
			for (int i = word.length() - 2; i < word.length(); i++) { 
				
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
	 * Strips the word of punctuation
	 *
	 * This method returns the word after stripping any punctuation. 
	 * This allows the 'clean' word to be processed further.
	 *
	 * @param word The input word strip punctuation from.
	 * @return The word with all punctuation characters removed.
	 */
	private static String stripPunctuation(String word) { 
		StringBuilder noPunctuation = new StringBuilder();
	
		for (int i = 0; i < word.length(); i++) {
			
			char c = word.charAt(i);
			
			// Check character is not punctuation and use string builder to build a 'clean' word.
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
		
		/*
		 * Loop through the encodings array from ReadEncodingsFile.getEncodings() and find a match
		 * for the input punctuation. If a match is found, the corresponding encoding is returned.
		 */
		for (int rows = 0; rows < ReadEncodingsFile.getEncodings().length; rows++) {

			match = ReadEncodingsFile.getEncodings()[rows][0];
			encoded = ReadEncodingsFile.getEncodings()[rows][1];

			if (match.equals(punc)) 
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
	
	
	/**
	 * Prints a simple diagnostics from the encoding process.
	 *
	 * Allows the user to see how many items were encoding successfully vs how many were processed.
	 * An item is a word, prefix-suffix word or punctuation.
	 *
	 * @param input The input array to process.
	 */
	private static void encodeStats(String[] input) {
		
		int totalWordCount = 0;
		int unknownWordCount = 0;
		
		for (int o = 0; o < input.length; o++) 
		{
			// Count elements that are not null or have whitespace
			if (input[o] != null && !input[o].isBlank()) 
			{
				totalWordCount++;
			}
			
			// Count unknown elements
			if (input[o].equals("0"))
			{
				unknownWordCount++;
			}

		}
		
		System.out.println("\n--- Encoding Summary ---");
		System.out.println("Total full words encoded: " + fullMatchCount);
		System.out.println("Total prefix-suffix words encoded: " + prefixSuffixCount);
		System.out.println("Total punctuation encoded: " + punctuationCount);
		
		System.out.println("Total items processed: " + totalWordCount);
		System.out.println("Total known items encoded: " + (totalWordCount - unknownWordCount));
		System.out.println("Total unknown itmes encoded: " + unknownWordCount);
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

		// Loop through the inputWords array
	    for (int i = 0; i < inputWords.length; i++) 
	    {
	        if (inputWords[i] != null) 
	        {
	        	// If the current word does not start with @@
	            if (!inputWords[i].startsWith("@@")) 
	            {
	            	// Check if the next word starts with @@ (it is a suffix)
	                if (i + 1 < inputWords.length && inputWords[i + 1] != null && inputWords[i+1].startsWith("@@")) 
	                {
	                    temp = inputWords[i + 1].replace("@@", "").trim();  // Clean up the suffix by trimming and removing the @@
	                    joinedWords[i] = inputWords[i] + temp;				// Join the input word and the temp word
	                    i++; 												// Skip the temp word (skip the word that was just joined)
	                } 
	                else 
	                { 
	                	joinedWords[i] = inputWords[i];	// Just add the current word.
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
		int j = 0;  // Output index
		boolean quoteStart = false;
		
		for (int i = 0; i < inputWords.length; i++) {
			String current = inputWords[i];
			String type = classifyPunctuation(current);

			if (current != null) 
			{
				/*
				 *  Need to handle quotations here before I deal with any other punctuation. To ensure that the punctuation is formatted 
				 *  correctly, placed before a word or after a word, I need to track the opening and closing of quotation marks. I have
				 *  ignored " " as they are not included in the encoding table.
				 */
				if (current.equals("'") || current.equals("`") ) 
				{
					// If the start of a quote is not detected
			        if (!quoteStart) 
			        {
			        	// start a quote
			        	quoteStart = true;
			            // treat as opening: attach to next word
			            if (i + 1 < inputWords.length && inputWords[i + 1] != null) 
			            {
			                joinedPunct[j++] = current + inputWords[++i];
			            }
			            else 
			            {
			                joinedPunct[j++] = current;
			            }
			        } 
			        else 
			        {
			        	// end of quote
			        	quoteStart = false;
			            // treat as closing: append to previous word
			            if (j > 0) 
			            {
			                joinedPunct[j - 1] += current;
			            } 
			            else 
			            {
			                joinedPunct[j++] = current;
			            }
			        }
			    }
				else 
				{
					/*
					 * Handle the rest of the punctuation such as ( ) / ! .
					 * I still need to monitor if it is closing or opening punctuation but these types
					 * are easier as they are not mixed like seen in some texts with quotations.
					 * 
					 */
					if (!type.equals("none")) 
					{
						if (type.equals("open")) 
						{
							if (i + 1 < inputWords.length && inputWords[i + 1] != null) 
							{
								joinedPunct[j++] = current + inputWords[i + 1];
								i++; // skip next word because merged
							} 
							else 
							{
								joinedPunct[j++] = current;
							}
						} 
						else if (type.equals("close")) 
						{
							if (j > 0) 
							{
								joinedPunct[j - 1] += current; // Append to last added word
							} 
							else 
							{
								joinedPunct[j++] = current;
							}
						} 
						else 
						{
							joinedPunct[j++] = current;
						}
					} 
					else 
					{
						joinedPunct[j++] = current;
					}
				}
			}
		}

		return joinedPunct;
	}

	/**
	 * Classifies word as a type of punctuation 
	 * This method takes the word and classifies it as a type of punctuation.
	 * Opening, closing or misc punctuation. This helps when positioning the 
	 * punctuation during rebuild.
	 *
	 *
	 * @param word The word/ punctuation a string.
	 * @return Classification as "open", "close", "misc", or "none".
	 */
	private static String classifyPunctuation(String word) {

		// Classify the types of punctuation that may be found within the texts
		final String[] OPEN_PUNCT = {"(", "[", "{", "\"", "'"};
		final String[] CLOSE_PUNCT = {")", "]", "}", "\"", "'", "!", "?", ".", ",", ";", ":"};
		final String[] MISC_PUNCT = {"-", "—", "*", "~", "#"};

		for (String p : OPEN_PUNCT) {
			if (p.equals(word)) return "open";
		}
		for (String p : CLOSE_PUNCT) {
			if (p.equals(word)) return "close";
		}
		for (String p : MISC_PUNCT) {
			if (p.equals(word)) return "misc";
		}

		return "none";
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
		
		// Create a new array of same size as the original array.
		String[] cleanedArr = new String[original.length];

		int counter = 0;
		
		/*
		 * Add the contents of the original array into the cleaned array as long as the element is not null.
		 * This 'moves' the nulls to the end of the array and removes any 'gaps'.
		 */
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
	
	/**
	 * Prints a simple diagnostics from the decoding process.
	 *
	 * Allows the user to see how many words were decoded successfully vs how many were processed.
	 *
	 * @param input The input array to process.
	 */
	private static void decodeStats(String[] input) {
		
		int totalWordCount = 0;
		int unknownWordCount = 0;
		
		for (int o = 0; o < input.length; o++) 
		{
			// Count elements that are not null or have whitespace
			if (input[o] != null && !input[o].isBlank()) 
			{
				totalWordCount++;
			}
			
			// Count unknown elements
			if (input[o].equals("[???]"))
			{
				unknownWordCount++;
			}

		}
		
		System.out.println("\n--- Decoding Summary ---");
		System.out.println("Total words processed: " + totalWordCount);
		System.out.println("Total known words decoded: " + (totalWordCount - unknownWordCount));
		System.out.println("Total unknown words decoded: " + unknownWordCount);
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
		
		// If the word equals the special newline token, return true.
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

		// Count how many valid elements that do not have null values or are empty.
		for (int i = 0; i < original.length; i++)
		{
			
			if (original[i] != null && !original[i].equals("")) 
			{
				count++;
			}
		}

		// Create a new array using the count variable as the size.
	    String[] trimmedArray = new String[count];
	    int index = 0;
	
	    /*
	     *  Add the contents of the original array into the trimmed array as long as the element
	     *  is not null or empty.
	     */
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

		// Create a new array double the size of the input.
		String[] bigger = new String[original.length * 2];

		// Add contents of the input array into the new larger array.
		for (int i = 0; i < original.length; i++) 
		{
			bigger[i] = original[i];
		}

		return bigger;
	}
}
