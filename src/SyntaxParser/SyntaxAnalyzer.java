// Matthew LePage 2021

package SyntaxParser;

import java.util.Scanner;

public class SyntaxAnalyzer {
	public static boolean codeBreaker (Scanner read, int lineNum, int cC) {
		// If this goes on, the code is not pure.
											System.out.println("Cycle " + lineNum);
		boolean error = false;
		boolean nE = false;
		int code = -1;
		// This is what I work with
		String[] line = stringBreaker(read.nextLine());
		
		if (line.length == 0) {
			if (read.hasNext()) {
				if (cC >= 3)
					cC++;
				nE = codeBreaker (read, lineNum + 1, cC);
			} else {
				System.out.println("This code does not conclude with 'end'. codeEND");
				error = true;
			}
			if (nE)
				error = true;
			return error;
		}
		
		// Only runs on lineNum 1 and 2
		if (lineNum == 1) {
			// program initialized correctly
			if (line.length != 0 && codeCase(line[0]) == 1) {
				if (read.hasNext()) {
					line = stringBreaker(read.nextLine());
					lineNum++;
					// begin initialized correctly
					if (line.length != 0 && codeCase(line[0]) == 2) {
						if (read.hasNext()) {
							line = stringBreaker(read.nextLine());
							lineNum++;
						} else {
							System.out.println("This code is not long enough to be code. codeSHORT");
							error = true;
						}}
					// begin not initialized correctly
					else {
						System.out.println("Code does not contain the 'begin' initializer. code2");
						error = true;
					}} else {
					System.out.println("This code is not long enough to be code. codeSHORT");
					error = true;
				}}
			// program not initialized correctly
			else {
				System.out.println("Code does not contain the 'program' initializer. code1");
				error = true;
				// code contains begin
				if (line.length != 0 && codeCase(line[0]) == 2)
					if (read.hasNext()) {
						line = stringBreaker(read.nextLine());
						lineNum++;
					}
					else
						System.out.println("This code is not long enough to be code. codeSHORT");
				// code does not contain begin
				else
					System.out.println("Code does not contin the 'begin' initializer. code2");
				}}
		
		if (line.length == 0) {
			if (read.hasNext()) {
				nE = codeBreaker (read, lineNum + 1, cC);
			} else {
				System.out.println("This code does not conclude with 'end'. codeEND");
				error = true;
			}
			if (nE)
				error = true;
			return error;
		}
		
		// Figures out if this line can exist, 
		switch (codeCase(line[0])) {
		case 0: code = 0; // Variable Case
			break;
		case 1:			System.out.println("'program' should not be initialized at line " + lineNum + ". code1b"); error = true;
			break;
		case 2: 		System.out.println("'begin' should not be initialized at line " + lineNum + ". code2b"); error = true;
			break;
		case 3: code = 3; // End Case
			break;
		case 4: code = 4; // If Case
			break;
		case 5: 		System.out.println("Keyword 'then' is used incorrectly at line " + lineNum + ". code5"); error = true;
			break;
		case 6: code = 6; // Loop Case
			break;
		case 7: 		System.out.println("( is used incorrectly at line " + lineNum + ". code7"); error = true;
			break;
		case 8: 		System.out.println(") is used incorrectly at line " + lineNum + ". code8"); error = true;
			break;
		case 9: 		System.out.println("Operation is used incorrectly at line " + lineNum + ". code9"); error = true;
			break;
		case 10: 		System.out.println("= is used incorrectly at line " + lineNum + ". code10"); error = true;
			break;
		case 11: 		System.out.println("Symbol is used incorrectly at line " + lineNum + ". code11"); error = true;
			break;
		case 12: 		System.out.println("; is used incorrectly at line " + lineNum + ". code12"); error = true;
			break;
		case 13: 		System.out.println("! is used incorrectly at line " + lineNum + ". code13"); error = true;
			break;
		}
		
		switch (code) {
		case 0:
			if (!varCheck(line)) {
				System.out.println("There is a syntax error on line " + lineNum + ". codeVAR");
				error = true;
			}
			if (codeCase(line[line.length-1]) == 12)
				cC = 1;
			else
				cC = 3;
			if (read.hasNext())
				nE = codeBreaker (read, lineNum + 1, cC);
			else {
				System.out.println("This code does not conclude with 'end'. codeEND");
				error = true;
			}
			if (nE)
				error = true;
			return error;
		case 3:
			if (cC == 1 || cC == 2) {
				System.out.println("There is a forbidden semicolon on line " + (lineNum - cC) + ". codeFSEMI");
				error = true;
			} else if (cC == 0) {
				System.out.println("Before end was declared, the code ended on an IF or LOOP statement.");
				error = true;
			}
			return error;
		case 4:
			if (!ifCheck(line)) {
				System.out.println("There is a syntax error on line " + lineNum + ". codeIF");
				error = true;
			}
			if (cC >= 2) {
				System.out.println("There is a missing semicolon on line " + (lineNum - cC + 2) + ". codeSEMI");
				error = true;
			}
			break;
		case 6:
			if (!loopCheck(line)) {
				System.out.println("There is a syntax error on line " + lineNum + ". codeLOOP");
				error = true;
			}
			if (cC >= 2) {
				System.out.println("There is a missing semicolon on line " + (lineNum - cC + 2) + ". codeSEMI");
				error = true;
			}
		}
		
		// Note to self: Don't touch this.
		if (read.hasNext()) {
			nE = codeBreaker (read, lineNum + 1, 0);
		} else {
			System.out.println("This code does not conclude with 'end'. codeEND");
			error = true;
		}
		if (nE)
			error = true;
		return error;
	}
	
	public static boolean varCheck (String[] source) {
		int p = 0;
		int cCase;
		for (int i = 0; i != source.length; i++) {
			cCase = codeCase(source[i]);
			if (p == 0 && cCase == 0)
				p = 1;
			else if (p == 1 && cCase == 10)
				p = 2;
			else if (p == 2 && cCase == 7)
				p = 5;
			else if (p == 2 && cCase == 0)
				p = 3;
			else if (p == 3 && cCase == 9)
				p = 4;
			else if (p == 3 && cCase == 12)
				p = 9;
			else if (p == 4 && cCase == 0)
				p = 3;
			else if (p == 4 && cCase == 7)
				p = 5;
			else if (p == 5 && cCase == 0)
				p = 6;
			else if (p == 6 && cCase == 9)
				p = 7;
			else if (p == 6 && cCase == 8)
				p = 8;
			else if (p == 7 && cCase == 0)
				p = 6;
			else if (p == 8 && cCase == 9)
				p = 4;
			else if (p == 8 && cCase == 12)
				p = 9;
			else if (p == 9)
				p = 10;
			else
				break;
		}
		if (p == 3 || p == 8 || p == 9)
			return true;
		return false;
	}
	
	public static int codeCase (String word) {
		switch (word) {
		case "program": 	return 1;
		case "begin": 		return 2;
		case "end": 		return 3;
		case "if":			return 4;
		case "then": 		return 5;
		case "loop": 		return 6;
		case "(": 			return 7;
		case ")": 			return 8;
		case "+": case "-": case "*": case "/": case "%":
							return 9;
		case "=": case "+=": case "-=": case "*=": case "/=": case "%=":
							return 10;
		case "<": case ">": case "<=": case ">=": case "==": case "!=":
							return 11;
		case ";": 			return 12;
		case "!": 			return 13; // I don't know why this is an option when it isn't used.
		default: 			return 0;
		}
	}
	
	public static boolean ifCheck (String[] source) {
		boolean result = false;
		int p = 1;
		int cCase;
		for (int i = 0; i != source.length; i++) {
			cCase = codeCase(source[i]);
			if (p!= 1 && cCase <= 1 && cCase >= 6)
				break;
			else if (p == 1 && cCase == 4)
				p = 2;
			else if (p == 1 && cCase <=1 && cCase >= 6)
				break;
			else if (p == 2 && cCase == 7)
				p = 3;
			else if (p == 3 && cCase == 0)
				p = 4;
			else if (p == 4 && cCase == 9)
				p = 3;
			else if (p == 4 && cCase == 8)
				p = 7;
			else if (p == 4 && cCase == 11)
				p = 5;
			else if (p == 5 && cCase == 0)
				p = 6;
			else if (p == 6 && cCase == 9)
				p = 5;
			else if (p == 6 && cCase == 8)
				p = 7;
			else if (p == 7 && cCase == 5) {
				result = true;
				p = 8;
			} else if (p == 8) {
				result = false;
				break;
			} else
				break;
		}
		return result;
	}
	
	public static boolean loopCheck (String[] source) {
		boolean result = false;
		int p = 1;
		int cCase;
		for (int i = 0; i != source.length; i++) {
			cCase = codeCase(source[i]);
			if (p != 1 && cCase <= 1 && cCase >= 6)
				break;
			else if (p == 1 && cCase <= 1 && cCase >= 5)
				break;
			else if (p == 1 && cCase == 6)
				p = 2;
			else if (p == 2 && cCase == 7)
				p = 3;
			else if (p == 3 && cCase == 0)
				p = 4;
			else if (p == 4 && cCase == 9)
				p = 3;
			else if (p == 4 && cCase == 11)
				p = 5;
			else if (p == 5 && cCase == 0)
				p = 6;
			else if (p == 6 && cCase == 9)
				p = 5;
			else if (p == 6 && cCase == 8) {
				result = true;
				p = 7;
			} else if (p == 7) {
				result = false;
				break;
			}
			else break;
		}
		return result;
	}
	
	public static String[] stringBreaker (String source) {
		String[] result = new String[0];
		// Empty line return
		if (source.length() == 0)
			return result;
		String word = "";
		
		// Converts the source into an array.
		char[] cArray = new char[source.length()];
		for (int i = 0; i < source.length(); i++) { 
            cArray[i] = source.charAt(i); 
        } 
		
		for (int i = 0; i != cArray.length; i++) {
			if (i == 0) {
				if (cArray[0] != ' ' || cArray[0] != '	')
					word = ("" + cArray[i]);
			} else {
				switch (caseReturn(cArray[i-1],cArray[i])) {
				// ----------------------------------------------------------
				case 1:
					word = (word + cArray[i]);
					break;
				case 2:
					result = wordToArray(result,word);
					word = "" + cArray[i];
					break;
				case 3:
					result = wordToArray(result,word);
					word = "";
					break;
				// ----------------------------------------------------------
				case 4:
					result = wordToArray(result,word);
					word = "" + cArray[i];
					break;
				case 5:
					if (comboFind(cArray[i-1],cArray[i-2])) {
						word = (word + cArray[i]);
						result = wordToArray(result,word);
						word = "";
					} else {
						result = wordToArray(result,word);
						word = "" + cArray[i];
					}
					break;
				case 6:
					result = wordToArray(result,word);
					word = "";
					break;
				// ----------------------------------------------------------
				case 7: case 8: word = ("" + cArray[i]);
					break;
				case 9: // Nothing Happens
					break;
				// ----------------------------------------------------------
				}}
			if (i == cArray.length-1) {
				if (word != "") {
					switch (caseReturn(cArray[i-1],cArray[i])) {
					case 1: result = wordToArray(result,word);
						break;
					case 2: result = wordToArray(result,word);
					case 3: break;
					case 4: result = wordToArray(result,word);
						break;
					case 5: result = wordToArray(result,word);
					case 6: break;
					case 7: result = wordToArray(result,word);
						break;
					case 8: result = wordToArray(result,word);
					case 9: break;
					}}}}
		return result;
	}
	
	public static boolean specialFind (char c) {
		boolean r = false;
		switch (c) {
		case '(': case ')':
		case '+': case '-': case '*': case '/': case '%':
		case '<': case '>':
		case '=': case '!': case ';':
			r = true;
		default:
			return r;
		}
	}
	
	public static boolean comboFind (char c1, char c2) {
		String combo = (c1 + "" + c2);
		switch (combo) {
		case "==": case "!=":
		case "+=": case "-=":
		case "*=": case "/=":
		case ">=": case "<=":
			return true;
		default:
			return false;
		}
	}

	// Explanation for this method is located at bottom of code.
	public static int caseReturn (char c1, char c2) {
		int i;
		// ----------------------------------------------------------
		if (c1 == ' ' || c1 == '	')
			i = 7;
		else if (specialFind(c1))
			i = 4;
		else // A
			i = 1;
		// ----------------------------------------------------------
		if (c2 == ' ' || c2 == '	')
			i += 2;
		else if (specialFind(c2))
			i++;
		// ----------------------------------------------------------
		return i;
	}
	
	public static String[] wordToArray (String[] result, String word) {
		String[] temp = new String[result.length + 1];
		for (int i = 0; i != result.length; i++)
			temp[i] = result[i];
		temp[temp.length-1] = word;
		return temp;
	}
}

/* When making the string, what I need is to separate each part from each other.
 * This requires defining what parts of a String I want to separate, in this case there are 3.
 * The first is Alpha-numeric letters.
 * The second is special characters like (, ), and ;.
 * The third is a space.
 * How the caseReturn works is that it figures out if two characters next to each other mean a separation is required.
 * 1: 'A' -> 'A' The word is being built upon.
 * 2: 'A' -> '#' It is the end of the current word, and a new special character/combo is being made.
 * 3: 'A' -> ' ' It's the end of the current word.
 * 4: '#' -> 'A' It is the end of the current special character/combo, and a new word is being made.
 * 5: '#' -> '#' There is either an error, or a special combo is being made.
 * 6: '#' -> ' ' It's the end of the current special character/combo
 * 7: ' ' -> 'A' A new word is being made.
 * 8: ' ' -> '#' A new special character/combo is being made
 * 9: ' ' -> ' ' Two spaces are next to each other, nothing can be done.
 * 
 * Definitions:
 * Word - BEGIN, END, LOOP, var1, var2, etc
 * Special Character - +, -, *, /, (, <, =, etc
 * Special Combo - +=, -=, <=, ==, etc (probably not the right term, but this is meant broadly for two special characters together)
 * 
 * I'm definitely making a new object out of this code for myself to use later,
 * as this String separator works really well, and I'm very proud of it.
 * It is much better than the complex mess I had yesterday submitted.
 * 
 * I find that I can also have fun with optimizing code, but I hate having to write the badly optimized code only to trash it later.
 * I had to rewrite this code about 6 times to reach this point.
 * 
 * 
 * When making the varCheck, ifCheck, and loopCheck, there is a method within all the madness.
 * While it would take too long to explain how it works, try drawing every p you see in one of the checks.
 * Draw arrows from one p to another, with the relevant cCase attached to the line.
 * You'll see that the only way to traverse to the final p is if a logic is found on the lines to the goal.
 * It's like a game of leapfrog, and depending on a number, changes what lilypad to go to, or if you fall off.
 * 
 * 
 * For codeBreaker, what must be known is always the first String in a line, which tells me what logic to expect in the rest of
 * the line.
 * If the line is started by a variable, it becomes more complicated, as it can end in multiple ways, plus the whole shebackle of
 * the semicolon. Basically cC stands for colonCase, what this does is that 3 represents a missing semicolon on the end of this
 * line when there CAN be one, while 1 represents that there IS one. 0 serves the purpose of stating that the previous statement
 * did not require a semicolon to be placed.
 */