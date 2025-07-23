# OOSD Assignment 2025 - Encoding Words with Suffixes 

**Author:** Jamie Buick   
**Version:** Java 21


## Description

This is a console based application that encodes and decodes text given two input files:
- A plain text file
- An encoding mappings CSV file. This contains the mapping between text and numeric codes


The encodings mapping file contains:
- punctuation 
- numbers
- words 
- word suffixes (e.g. *-ing*, *-ed*, *-ly*) 


Each item type above has it's own unique numeric value used for encoding and decoding the text. 

### Input file Processing
- The encodings CSV file is parsed into an array called **encodingMap**, this contains the text and its numerical equivalent. 
- The input text file is parsed line by line and split into words, the words are trimmed and converted to lowercase. 
- Line breaks are preserved by adding a special token *@@NEWLINE*. All words and tokens are added to the **encoderDecoderInput** array.
- When reading is complete, **encoderDecoderInput** is passed to either the encode **EncoderDecoder.encode** or **EncoderDecoder.decode** depending on the menu selection. 
- All file reading operations are completed using BufferedReader.

### Encoding Process

Encodes elements of **encoderDecoderInput** based on the **encodingMap**. The encoding process involve:
- Detecting newlines using the special token added during parsing - to preserve formatting.
- Detecting and separating lone, leading and trailing punctuation. 
- Attempting a punctuation encoding, e.g. ! → 1. 
- Attempting a 'full-match' encoding, e.g. love → 338.
- Attempting a prefix-suffix encoding, e.g., hello → prefix: hell = 1879 & suffix: @@o = 73, encoded as two parts.
- Detecting elements with no match. These are encoded with a 0. 
- Encoding punctuation and adding it to the **encodings** array relative to its associated word. Punctuation can be handled one its own, at the start of word or up to two characters at the end of a word. 
- Constructing a clean, trimmed array of encoded words and punctuation ready to be written to a .txt file.
- Dynamically increase the size of the **encodings** array for memory efficiency. 


### Decoding Process
Decodes elements of **encoderDecoderInput** based on the **encodingMap**. The decoding process involve:
- Detecting newlines using the special token added during parsing - to preserve formatting. 
- Find an encoding match from the **encodingMap** for the word or punctuation.
- Words with no match will have an encoding of 0. These are decoded as [???].
- Build prefix-suffix words and punctuation around words in the correct format.
- Construct a clean, trimmed array of correctly structured words and/or punctuation that can be written to a .txt file
- Dynamically increase the size of the **decodings** array for memory efficiency. 

### Output file Processing
- Writes only valid, non-null or not empty elements to a text file using BufferedWriter. 
- Each element is printed followed by a space unless it is a newline which is written as just "\n" to preserve original formatting.
- The **encoderDecoderInput** & **encoderDecoderReturn** are cleared after processing to ensure a clean state for next operations.


## Main Features
- User can input custom files.
- Encodes and decodes text file using a mapping CSV.
- Dynamic arrays for memory efficiency. 
- Handle line breaks to ensure formatting is preserved.
- Handles punctuation and formats correctly during decoding.
- Cleans up variables and arrays after execution to ensure the program runs consistently.
- Encodings and decoding summary printed for the user.













