package ie.atu.sw;

import java.util.Scanner;
import java.io.File;
import static java.lang.System.out;

public class Menu {
	private Scanner s;
	private boolean keepRunning = true;
	private boolean encode;
	private boolean readFinished;

	private TextFileProcessor textFileProcessor = new TextFileProcessor();
	private ReadEncodingsFile readEncodingsFile = new ReadEncodingsFile();

	String filePathMap;
	String filePathInput;
	String filePathOutput;
	
	
	public Menu() {

		s = new Scanner(System.in);
	}

	public void start() {
		while (keepRunning) {
			showOptions();
			
			String input = s.next();
			s.nextLine(); 
			
			if (!input.matches("\\d+")) {
				out.println("[Error] Please enter a valid number.");
				continue;
			}
			
			int menuSelection = Integer.parseInt(input);
			
			switch(menuSelection)
			{
				case 1 -> mapFile();
				case 2 -> textFile();
				case 3 -> outputFile();
				case 4 -> encode();
				case 5 -> decode();
				case 6 -> keepRunning = false;
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
		System.out.println("*              		By Jamie Buick                	       *");
		System.out.println("************************************************************");
		System.out.println("(1) Specify Mapping File (.csv)");
		System.out.println("(2) Specify Text File to Encode (.txt)");
		System.out.println("(3) Specify Output File (.txt)");
		System.out.println("(4) Encode Text File");
		System.out.println("(5) Decode Text File");
		System.out.println("(6) Quit");

		// Output a menu of options and solicit text from the user
		System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
		System.out.print("Select Option [1-?]>");
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
	
	
	// Allows the user to enter a file path for options 1-2, it is then checked that it exists using the validateFilePath method
	private String changeFilePath() {

			System.out.println("Enter a valid file path>");
			String filePath = s.nextLine(); 

			while (!validateFilePath(filePath)) 
			{
				System.out.println("Invalid file path. Enter a valid file path>");
				filePath = s.nextLine(); 
			
			}
	
		return filePath;
	}
	

	
	
	// Checks the file path exists 
	private boolean validateFilePath(String s) {
		File fp = new File(s);
		
		if (fp.exists()) 
		{
			return true;
		}
		return false;
	}
	

	
	// Menu functions
	private void mapFile() {
		filePathMap = changeFilePath();
		
		readEncodingsFile.parseEncoding(filePathMap);
	}
	
	
	private void textFile() {
		filePathInput = changeFilePath();
	}
	

	private void outputFile() {
	    System.out.println("Enter a valid the file path>");
	    filePathOutput = s.nextLine(); // Now it will wait for actual input
	}
	
	
	private void encode() {
		encode = true;
		readFinished = textFileProcessor.readFile(filePathInput, encode);
		
		if(readFinished)
		{
			textFileProcessor.writeFile(filePathOutput,encode);
		} 
		readFinished = false;
	}
	
	
	
	private void decode() {
		encode = false;
		// I will pass this a bool true / false
		readFinished = textFileProcessor.readFile(filePathInput, encode);
		
		if(readFinished)
		{
			textFileProcessor.writeFile(filePathOutput, encode);
		}
		
		readFinished = false;
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
