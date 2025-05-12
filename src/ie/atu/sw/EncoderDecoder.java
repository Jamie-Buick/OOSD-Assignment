package ie.atu.sw;

// Decoding and Encoding class 
public class EncoderDecoder {

	private static String[] encodedWords = new String[15];

	public static void encode(String s) {
		int counter = 0;

		String nextMatch = null;
		String matchedEncoding = null;

		for(int rows = 0; rows < EncodingFileProcessor.getEncodings().length; rows++) {
			
			String match = EncodingFileProcessor.getEncodings()[rows][0];
			String encoded = EncodingFileProcessor.getEncodings()[rows][1];

			if(EncodingFileProcessor.getEncodings()[rows][0].equals(s)) 
			{
				encodedWords[counter] = encoded;
				//System.out.println(EncodingFileProcessor.encodings[rows][1]);
				counter++;

				break;
			}

			if (s.startsWith(EncodingFileProcessor.getEncodings()[rows][0]))
			{
				if(nextMatch == null || match.length() > nextMatch.length())
				{
					nextMatch = match;
					matchedEncoding = encoded;

				}


			}
		}

		if(nextMatch != null)
		{
			encodedWords[counter] = matchedEncoding;
			//System.out.println(EncodingFileProcessor.encodings[rows][1]);
			counter++;
		}


		/*

				if (EncodingFileProcessor.getEncodings()[rows][0].startsWith("@@"))    // && isStringLengthEqual(EncodingFileProcessor.getEncodings()[rows][0], s) ))
				{
					String prefixStrip = EncodingFileProcessor.getEncodings()[rows][0].replace("@@", "");

					if (s.endsWith(prefixStrip))
					{
						encodedWords[counter] = EncodingFileProcessor.getEncodings()[rows][1];
						//System.out.println(EncodingFileProcessor.encodings[rows][1]);
						counter++;

					}	

				}
		 */





		for (int i = 0; i < counter; i++)
		{
			System.out.println(encodedWords[i]);
		}

		/*
		for (int cols = 0; cols < 2; cols++) {

			System.out.print(EncodingFileProcessor.encodings[rows][cols] + "		" );
		}
		System.out.println("");

		 */		
		//return "hello";

	}





	public String decode(String s) {
		return null;

	}


}
