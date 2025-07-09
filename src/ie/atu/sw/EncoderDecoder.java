package ie.atu.sw;

// Decoding and Encoding class 
public class EncoderDecoder {

	// Notes:
	// - for punct word, I think we strip the punct and base word and then pass the punct through a punct encoder and pass base word 
	// matchfullword


	
	public static String[] encode(String[] input) {

		// This returns the multiple parts or single encoding. I have made this a max 3 here to account for prefix suffix and punc word
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
	
		return matchedSuffixEncoding;
	}
	
	
	
	private static boolean isNewLine(String word) {
		
		if(word.trim().equals("@@newline"))
		{
			return true;	
		}
		
		return false;
	}
	
	
	private static boolean startsWithPunctuation(String word) {
		
		char firstChar = word.charAt(0);
		
		if((String.valueOf(firstChar).matches("\\p{Punct}")))
		{
			 return true;
		}
		
		return false;
		
	}
	
	
	private static boolean endsWithPunctuation(String word) {
		
		char lastChar = word.charAt(word.length() - 1);
		
		if(String.valueOf(lastChar).matches("\\p{Punct}"))
		{
			return true;
		}
		
		return false;	
	}
	
	
	
	
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
	
	

	
	private static String stripStartPunctuation(String word) {  

	    return word.substring(1);	
	}
	
	
	
	
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
	
	
	
	private static String[] removeNulls(String[] inputWords) {

		String[] cleanedArr = new String[inputWords.length];

		int counter = 0;
		for (int i = 0; i < inputWords.length; i++) 
		{
			if (!(inputWords[i] == null)) 
			{
				cleanedArr[counter] = inputWords[i];
				counter++;
			}
		}
		
		return cleanedArr;
	}
	
	
	
	
	
	
	
	
	
	
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
	
	
	
	// Expands an array manually using a loop
	private static String[] expandArray(String[] original) {
	    String[] bigger = new String[original.length * 2];
	    for (int i = 0; i < original.length; i++) {
	        bigger[i] = original[i];
	    }
	    return bigger;
	}
	   

	

	


}
