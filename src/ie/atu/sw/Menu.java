package ie.atu.sw;

import java.util.Scanner;
import java.io.File;
import static java.lang.System.out;

public class Menu {
	
	// Used for user input in the menu.
	private Scanner s;
	
	// Keeps the menu running on a loop.
	private boolean keepRunning = true;
	
	// True = encode, false = decode.
	private boolean encode;
	
	// Indicated that the reading of input file is complete.
	private boolean readFinished;

	// Handles reading, storing and writing text file data.
	private TextFileProcessor textFileProcessor = new TextFileProcessor();
	
	// Handles reading the encoding map CSV.
	private ReadEncodingsFile readEncodingsFile = new ReadEncodingsFile();

	// Strings for encoding map, input file & output file paths.
	String filePathMap;
	String filePathInput;
	String filePathOutput;
	
	/**
	 * Constructs a new {@code Menu} and initializes the Scanner for user input
	 */
	public Menu() {
		s = new Scanner(System.in);
	}

	
	
	/**
	 * Starts and handles selections from the console menu.
	 * 
	 * This method starts a console menu, this menu will run until exited by the user.
	 * The user input must be a numerical value between 1-6. This user input will be used
	 * to run different functions of the program.
	 * 
	 */
	public void start() {
		// Loop until keepRunning is false
		while (keepRunning) {
			showOptions();
			
			String input = s.next();
			s.nextLine(); 
			
			// If the user input is not a digit, provide error message.
			if (!input.matches("\\d+")) {
				out.println("[Error] Please enter a valid number.");
				continue;
			}
			
			int menuSelection = Integer.parseInt(input);
			
			switch(menuSelection)
			{
				case 1 -> mapFile();					// Set the file path for encoding mapping CSV.
				case 2 -> textFile();					// Set the file path for input file.
				case 3 -> outputFile();					// Set the file path for output file.
				case 4 -> encode();						// Encode the input file.
				case 5 -> decode();						// Decode the input file.
				case 6 -> keepRunning = false;			// Exit the menu loop.
				default -> out.println("[Error] Invalid Selection");
			}
		}
	}
	
	
	
	/**
	 * Displays console menu and available options
	 * 
	 * This method displays a console menu and prompts the user to select a valid option.
	 * 
	 */
	private void showOptions()  {
		System.out.println(ConsoleColour.WHITE);
		System.out.println("************************************************************");
		System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
		System.out.println("*                                                          *");
		System.out.println("*              Encoding Words with Suffixes                *");
		System.out.println("*                                                          *");
		System.out.println("*              		By Jamie Buick                	       *");
		System.out.println("************************************************************");
		System.out.println("(1) Specify Mapping File (.csv)");
		System.out.println("(2) Specify Text File to Encode or Decode (.txt)");
		System.out.println("(3) Specify Output File (.txt)");
		System.out.println("(4) Encode Text File");
		System.out.println("(5) Decode Text File");
		System.out.println("(6) Quit");

		// Output a menu of options and solicit text from the user
		System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
		System.out.print("Select Option [1-6]>");
		System.out.println();

		/*
		// Probably add this into encoding / decoding sections where something is actually done
		// You may want to include a progress meter in you assignment!
		System.out.print(ConsoleColour.YELLOW); // Change the colour of the console text
		int size = 100; // The size of the meter. 100 equates to 100%
		for (int i = 0; i < size; i++) { // The loop equates to a sequence of processing steps
			printProgress(i + 1, size); // After each (some) steps, update the progress meter
			//Thread.sleep(10); // Slows things down so the animation is visible
		}
		*/
	}
	
	
	
	/**
	 * Allows the user to change the file path of the read text file
	 * 
	 * This method prompts the user to enter a file path, it is checked for validity.
	 * The user is prompted until a valid path is entered, as determined by the 
	 * {@code validateFilePath} method.
	 * 
	 * @return filePath A string that contains a valid file path.
	 */
	private String changeFilePath() {

			System.out.println("Enter a valid file path>");
			String filePath = s.nextLine(); 
			
			// Loop here while the entered path is invalid or does not exist.
			while (!validateFilePath(filePath)) 
			{
				System.out.println("Invalid file path. Enter a valid file path>");
				filePath = s.nextLine(); 
			}
	
		return filePath;
	}
	

	
	/**
	 * Checks if a file path exists
	 * 
	 * This method verifies whether the specified file path points to an existing file.
	 *
	 * @param s A string containing the file path to be checked.
	 * @return {@code true} To indicate that the file path is valid, otherwise {@code false}.
	 */
	private boolean validateFilePath(String s) {
		File fp = new File(s);
		
		// Checks if the file path exists
		if (fp.exists()) 
		{
			return true;
		}
		return false;
	}
	

	
	/**
	 * Gets and checks that the encodings map file path exists
	 * 
	 * This method verifies whether the specified encodings map file path points to an 
	 * existing file. It calls {@code parseEncoding} on the {@code readEncodingsFile}
	 * object to parse the contents into an array.
	 */
	private void mapFile() {
		filePathMap = changeFilePath();
		readEncodingsFile.parseEncoding(filePathMap);
	}
	
	
	
	/**
	 * Prompts the user to specify the input text file path.
	 * 
	 * This method gets the file path for the .txt file to be encoded or decoded.
	 * This file path is validated in the {@code changeFilePath} method.
	 */
	private void textFile() {
		filePathInput = changeFilePath();
	}
	
	
	
	/**
	 * Prompts the user to specify the output text file path.
	 * 
	 * This method asks the user to enter the file path where the output
	 * file should be created and saved.
	 */
	private void outputFile() {
	    System.out.println("Enter a valid the file path>");
	    filePathOutput = s.nextLine(); 
	}
	
	
	
	/**
	 * Coordinates the encoding process
	 * 
	 * This method sets the encoding mode flag to {@code true}, calls the {@code readFile} method 
	 * of {@code textFileProcessor} to read and encode the input file. If the read/ encode was 
	 * successful {@code writeFile} is called to write the result to the specified output file path.  
	 * 
	 */
	private void encode() {
		encode = true;
		readFinished = textFileProcessor.readFile(filePathInput, encode);
		
		if(readFinished)
		{
			textFileProcessor.writeFile(filePathOutput);
		} 
		readFinished = false;
	}
	
	
	
	/**
	 * Coordinates the decoding process
	 * 
	 * This method sets the encoding mode flag to {@code false}, calls the {@code readFile} method 
	 * of {@code textFileProcessor} to read and decode the input file. If the read/ decode was 
	 * successful {@code writeFile} is called to write the result to the specified output file path.  
	 * 
	 */
	private void decode() {
		encode = false;
		readFinished = textFileProcessor.readFile(filePathInput, encode);
		
		if(readFinished)
		{
			textFileProcessor.writeFile(filePathOutput);
		}
		readFinished = false;
	}
	

	/**
	 * Prints a progress bar
	 * 
	 * This method prints a progress bar when the program is in operation. This allows
	 * the user to see that the program is running.
	 * 
	 * Currently I do not use this.
	 * 
	 */
	public static void printProgress(int index, int total) {
		if (index > total)
			return; // Out of range
		int size = 50; // Must be less than console width
		char done = '█'; // Change to whatever you like.
		char todo = '░'; // Change to whatever you like.

		// Compute basic metrics for the meter
		int complete = (100 * index) / total;
		int completeLen = size * complete / 100;

		/*
		 * A StringBuilder should be used for string concatenation inside a loop.
		 * However, as the number of loop iterations is small, using the "+" operator
		 * may be more efficient as the instructions can be optimized by the compiler.
		 * Either way, the performance overhead will be marginal.
		 */
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < size; i++) {
			sb.append((i < completeLen) ? done : todo);
		}

		/*
		 * The line feed escape character "\r" returns the cursor to the start of the
		 * current line. Calling print(...) overwrites the existing line and creates the
		 * illusion of an animation.
		 */
		System.out.print("\r" + sb + "] " + complete + "%");

		// Once the meter reaches its max, move to a new line.
		if (done == total)
			System.out.println("\n");
	}

}
