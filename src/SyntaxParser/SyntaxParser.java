// Matthew LePage 2021

package SyntaxParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SyntaxParser {
	public static void main(String[] args) {
		Scanner input = new Scanner (System.in); // Scans input from user.
		Scanner read; // Scans the file
		java.io.File file; // The file to read from

		// This will make the user select a file from a repository.
		// If the file is in the project folder, it can straightly read from there just by typing the file name when prompted.
		System.out.println("Please select a file:");
		while (true) {
			String select = input.nextLine();
			try {
				file = new File(select);
				read = new Scanner(file);
				break;
			} catch (FileNotFoundException ex) {
				System.out.println("File not found, please try again:");
			}	
		}
		System.out.println(); //This line is for style
		
		if (read.hasNext()) {
			boolean error = SyntaxAnalyzer.codeBreaker(read, 1, 0);
			if (error)
				System.out.println("There are syntax errors in the code...");
			else
				System.out.println("There are no syntax errors in the code!");
		} else
			System.out.println("This code has no code.");
		
		input.close();
	}

}
