package application;

import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Pattern;

import application.media.Book;
import application.media.LendableMedia;
import application.media.MediaCopy;
import application.media.Movie;

/**
 * Utility methods and dialogue.
 * 
 */
public final class CommunicationsUtility {

// Dialogue
	public final static String MSG_ERROR_CHECKIN = "Syntax error: If you mean to check in an item. Type [checkin] followed by the article number corresponding to the item you want to check in. Typ [list] to see article numbers.";
	public final static String MSG_ERROR_CHECKOUT = "Syntax error: If you mean to check out an item. Type [checkout] followed by the article number corresponding to the item you want to check out. Typ [list] to see article numbers.";
	public final static String MSG_ERROR_DEREGISTER = "Syntax error: If you mean to [deregister] an item from the library database. Type [deregister] followed by the article number corresponding to the item you want to [deregister]. Typ [list] to see article numbers. ";
	public final static String MSG_ERROR_REGISTER = "Syntax error: If you mean to [register] an item, type [register] without any arguments. ";
	public final static String MSG_ERROR_INFO = "Syntax error: If you mean to view extended information about a item. Type [info] followed by the article number corresponding to the item you want to read more about. Typ [list] to see article numbers.";
	public final static String MSG_ERROR_LIST = "Syntax error: Type [list] if you mean to view library contents. ";
	public final static String MSG_ERROR_QUIT = "Syntax error: Type [quit] without any arguments if you mean to [quit] the application. ";
	public final static String MSG_ERROR_HELP = "Syntax error: Type [help] followed by a command to get usage information about the command. Or simply type [help] to see available commands. ";
	public final static String MSG_ERROR_LOANS = "Syntax error: Type [loans] without quotation marks if you mean to view all currently lended items and corresponding borrowers. ";
	public final static String MSG_ERROR_SYNTAX = "Syntax error: Give a command followed by only one argument and keep in mind that article numbers can only contain integers. ";
	public final static String MSG_ERROR_ARTICLENUMBER = "No item with the given article number can be found in the library. Typ [list] to see article numbers.";
	//private final static String MSG_ERROR_DOUBLEREGISTER = "An item with the given article number already exists in the library. ";
	public final static String MSG_ERROR_CHECKOUT_OUTOFSTOCK = "All copies of the given article are already lended. ";
	public final static String MSG_ERROR_NOTLENDED = "The item you are to check in is currently not lended out to anyone. ";
	public final static String MSG_ERROR_DOUBLECHECKIN = "The item corresponding to the given article number is already checked in. ";
	public final static String MSG_ERROR_LOANS_NONREGISTERED = " is not currently borrowing anything from Java Console Library. Type [loans] if you want to see all current loans. Returning to main menu. ";
	// asterisk is meant to be replaced TODO printf
	public final static String MSG_ERROR_UNKNOWNCOMMAND = "Java Console Library doesn't recognise \"*\" as a command. Type [help] to get [list] of available commands. "; 
	public final static String MSG_ERROR_INVALIDINPUT = "Invalid input. ";
	public final static String MSG_ERROR_UNEXPECTED_IOEXCEPTION = "Unexpected I/O exception. Try deleting the library folder and see to it that Java Console Library is executed in a place it has read/write permissions in. Shutting down... ";
	//asterisk is meant to be replaced
	public final static String MSG_ERROR_DEREGISTER_CHECKEDOUT = "Copies need to be checked in before you can deregister *. Returning to the main menu. ";
	
	public final static String QUERY_CHECKIN_SN = "Which copy would you like to check in? Answer with the serial number noted first of the line on every listed person in the following list: ";
	public final static String QUERY_PROMPT = "> ";
	public final static String QUERY_REGISTER_TYPE = "Would you like to register a [book] or a [movie]? ";
	public final static String QUERY_NAME = "Name of the person borrowing: ";
	public final static String QUERY_PHONENUMBER = "Phonenumber of the person borrowing: ";
	public static final String QUERY_ARTICLENUMBER = "Article number: ";
	public static final String QUERY_TITLE = "Title: ";
	public static final String QUERY_VALUE = "Value: ";
	public static final String QUERY_PAGES = "How many pages: ";
	public static final String QUERY_AUTHOR = "Author: ";
	public static final String QUERY_LENGTH = "How many minutes: ";
	public static final String QUERY_RATING = "What is the IMDB rating: ";
	// asterisk is meant to be replaced
	public final static String QUERY_REGISTER_NEWCOPY = "There is already a * registered with that article number. Would you like to register a new copy?. ";		
	
	public final static String MSG_WELCOME = "Hello and welcome to the Java Console Library! If this is the first time using this application you are recommended to type [help], this will give you the available commands. ";
	public final static String MSG_REGISTER_SUCCESS = "Media successfully added to library database. Awaiting further commands: ";
	public static final String MSG_DEREGISTER_SUCCESSFUL = "Item successfully deregistered. ";
	public final static String MSG_REGISTER_CANCELLED = "Returning to main menu. No item registered. ";
	
	// Incomplete, need to completed in help() method!
	public final static String INFO_GENERAL_ORIENTATION = "If you want information about how to use a particular command. Type [help] followed by the command. These are the available commands: "; 
	public final static String INFO_LOANS = "The [loans] commands is used to view all currently lended items and information about the corresponding borrowers. It can be used alone or with a borrower as argument or with [-expired] as argument. Argumenting with [-expired] will give all expired loans. ";
	public final static String INFO_CHECKIN = "The [checkin] command is used to check in a lended item such as a book or a movie into the library. Checked in items are ready to be lended out again. ";
	public final static String INFO_CHECKOUT = "The [checkout] command is used to check out an item such as a book or a movie from the library. Checked out items can't be lended again until they are returned and checked in. ";
	public final static String INFO_INFO = "The [info] command is used to view extended information about a particular item in the library database. ";
	public final static String INFO_REGISTER = "The [register] command is used to [register] new items into the library database. ";
	public final static String INFO_DEREGISTER = "The [deregister] command is used to remove an item from the library database. ";
	public final static String INFO_QUIT = "The [quit] command is used to exit the Java Console Library. All library contents are stored until the application is started again. ";
	public final static String INFO_LIST = "The [list] command is used to view a [list] of all items stored at the library. The [list] is ordered by article number. ";
	public final static String INFO_HELP = "Oh boy, you really need [help] don't you? ";
	
	private final static Scanner scanner = new Scanner(System.in);
	private CommunicationsUtility() {
	}
	/**
	 * Determine whether a string consists of digits and nothing more.
	 * @param number
	 * @return True if only numbers, false otherwise.
	 */
	public static boolean isNumber(String number) {
			if (number.length() == 0 || number == null)
				return false; // without this check empty strings [""] will return true
			return Pattern.matches("^[\\d]+$", number);
		}
	/**
	 * Determine whether a string consists of letters ("\p{L}" categorised in unicode to letter category) and whitespace characters and nothing else.
	 * @param name
	 * @return True if only letters and whitespace characters, false otherwise.
	 */
	public static boolean onlyLetters(String letters) {
		if (letters.length() == 0 || letters == null)
			return false; 
		return Pattern.matches("^[\\p{L} ]+$", letters);
	}
	/**
	 * Return a string with capitalised initials and all other letters in lower case
	 * 
	 * @param name
	 * @return
	 */
	public static String upperCaseInitials(String name) {
		String[] names;
		String firstLetter;
		name = name.toLowerCase();
		names = name.split("[ ]");
		name = "";
		for (String word : names) {
			firstLetter = String.valueOf(word.charAt(0));
			name += " " + word.replaceFirst(firstLetter, firstLetter.toUpperCase());
		}
		return name.trim();
	}
	/**
	 * General purpose for getting strings.
	 * @return
	 */
	public static String getStringInput() {
		String input = scanner.nextLine();
		if (input.length() == 0) // no empty strings allowed, no dialogue needed
			return getStringInput();
		input = input.toUpperCase();// is this okay? or should it be done first when needed
		return input;
	}
}