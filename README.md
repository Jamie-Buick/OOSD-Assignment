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

The encodings CSV file is parsed into an array called **encodingMap**, this contains the text and its numerical equivalent. The input text file is parsed line by line and split into words, line breaks a preserved by adding a special token, that are added to an array called **encoderDecoderInput**. When reading is complete, **encoderDecoderInput** is passed to either the encode **EncoderDecoder.encode** or **EncoderDecoder.decode** depending on the menu selection. 

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


### Decoding Process
Decodes elements of **encoderDecoderInput** based on the **encodingMap**. The decoding process involve:


### Output file Processing



## Features