package ie.atu.sw;

import java.util.Scanner;
import java.io.File;
import static java.lang.System.out;

public class Menu {
	private Scanner s;
	private boolean keepRunning = true;
	private int userInput;
	private InputTextFileProcessor InputFileProcessor = new InputTextFileProcessor();
	private EncodingFileProcessor EncodingFileProcessor = new EncodingFileProcessor();

	String filePathMap;
	String filePathText;
	
	
	public Menu() {

		s = new Scanner(System.in);
	}

	public void start() {
		while (keepRunning) {
			showOptions();

			int userInput = Integer.parseInt(s.next());
			
			switch(userInput)
			{
				case 1 -> mapFile();
				case 2 -> textFile();
				case 3 -> outputFile();
				case 4 -> configureOptions();
				case 5 -> encode();
				case 6 -> decode();
				case 7 -> extras();
				case 8 -> keepRunning = false;
				default -> out.println("[Error] Invalid Selection");
			}
		}
	}

	private void showOptions()  {
		System.out.println(ConsoleColour.WHITE);
		System.out.println("************************************************************");
		System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
		System.out.println("*                                                          *");
		System.out.println("*              Encoding Words with Suffixes                *");
		System.out.println("*                                                          *");
		System.out.println("************************************************************");
		System.out.println("(1) Specify Mapping File");
		System.out.println("(2) Specify Text File to Encode");
		System.out.println("(3) Specify Output File (default: ./out.txt)");
		System.out.println("(4) Configure Options");
		System.out.println("(5) Encode Text File");
		System.out.println("(6) Decode Text File");
		System.out.println("(?) Optional Extras...");
		System.out.println("(?) Quit");

		// Output a menu of options and solicit text from the user
		System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
		System.out.print("Select Option [1-?]>");
		System.out.println();

		
		/* Probably add this into encoding / decoding sections where something is actually done
		// You may want to include a progress meter in you assignment!
		System.out.print(ConsoleColour.YELLOW); // Change the colour of the console text
		int size = 100; // The size of the meter. 100 equates to 100%
		for (int i = 0; i < size; i++) { // The loop equates to a sequence of processing steps
			printProgress(i + 1, size); // After each (some) steps, update the progress meter
			Thread.sleep(10); // Slows things down so the animation is visible
		}
		*/
	}
	
	
	// Allows the user to enter a file path for options 1-2, it is then checked that it exists using the validateFilePath method
	private String changeFilePath() {

			String filePath = s.nextLine(); 
			
			while(validateFilePath(filePath) != true ) 
			{
				System.out.println("Enter a valid the file path>");
				filePath = s.nextLine(); 
			
			}
	
		return filePath;
	}
	
	
	// Checks that the file path actually exists 
	private boolean validateFilePath(String s) {
		File fp = new File(s);
		
		if (fp.exists()) {
			return true;
		}
		return false;
	}
	
	
	
	private void mapFile() {
		filePathMap = changeFilePath();
		EncodingFileProcessor.parseEncoding(filePathMap);
		System.out.println(filePathMap);
	}
	
	
	private void textFile() {
		filePathText = changeFilePath();
		//InputFileProcessor.readFile(filePathText);
		System.out.println(filePathText);	
	}
		
	private void outputFile() {
		System.out.println("out");
	}
	
	private void configureOptions() {
		System.out.println("config");
	}
	
	private void encode() {
		// I will pass this a bool true / false
		InputFileProcessor.readFile(filePathText);
		System.out.println("encode");
	}
	
	private void decode() {
		// I will pass this a bool true / false
		InputFileProcessor.readFile(filePathText);
		System.out.println("decode");
	}
	
	private void extras() {
		System.out.println("extra");
	}


	
	

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
