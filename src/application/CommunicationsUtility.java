package application;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Somewhat arbitrary split between CommunicationsUtility and LibraryController,
 * but methods contained here are supposed to be not directly related to
 * commands and error. All text is stored here.
 * 
 * @author Lemu
 *
 */
public final class CommunicationsUtility {

// Dialogue
	public final static String MSG_ERROR_CHECKIN = "Syntax error: If you mean to check in media. Type \"checkin [Article Number]\" without quotation marks and with \"[Article Number]\" replaced with the article number corresponding to the media you want to check in. ";
	public final static String MSG_ERROR_CHECKOUT = "Syntax error: If you mean to check out media. Type \"checkout [Article Number]\" without quotation marks and with \"[Article Number]\" replaced with the article number corresponding to the media you want to check out. ";
	public final static String MSG_ERROR_DEREGISTER = "Syntax error: If you mean to deregister media from library database. Type \"deregister [Article Number]\" without quotation marks and with \"[Article Number]\" replaced with the article number corresponding to the media you want to deregister. ";
	public final static String MSG_ERROR_REGISTER = "Syntax error: If you mean to register media. Type \"register\" without quotation marks. ";
	public final static String MSG_ERROR_INFO = "Syntax error: If you mean to view extended information about a media. Type \"info [Article Number]\" without quotation marks and with \"[Article Number]\" replaced with the article number corresponding to the media you want to read more about. ";
	public final static String MSG_ERROR_LIST = "Syntax error: Type \"list\" without quotation marks if you mean to view library contents. ";
	public final static String MSG_ERROR_QUIT = "Syntax error: Type \"quit\" without quotation marks if you mean to quit. ";
	public final static String MSG_ERROR_HELP = "Syntax error: Type help followed by a command to get usage information about the command. Or simply write help to see available commands. ";
	public final static String MSG_ERROR_SYNTAX = "Syntax error: Give a command followed by only one argument and keep in mind that article numbers can only contain integers. ";
	public final static String MSG_ERROR_ARTICLENUMBER = "No media with the given article number can be found in the library. ";
	public final static String MSG_ERROR_DOUBLEREGISTER = "A media with the given article number already exists in the library. ";
	public final static String MSG_ERROR_DOUBLECHECKOUT = "The media corresponding to the given article number is already checked out. ";
	public final static String MSG_ERROR_DOUBLECHECKIN = "The media corresponding to the given article number is already checked in. ";
	public final static String MSG_ERROR_UNKNOWNCOMMAND = "Java Console Library can't recognise any command in the given input. ";
	public final static String MSG_ERROR_INVALIDINPUT = "Invalid input. ";

	public final static String QUERY_PROMPT = "> ";
	public final static String QUERY_REGISTER_TYPE = "Would you like to register a book or a movie? ";
	public final static String QUERY_NAME = "What is the name of the borrower? ";
	public final static String QUERY_PHONENUMBER = "What is the phonenumber of the borrower: ";
	public static final String QUERY_ARTICLENUMBER = "Article number: ";
	public static final String QUERY_TITLE = "Title: ";
	public static final String QUERY_VALUE = "Value: ";
	public static final String QUERY_PAGES = "How many pages: ";
	public static final String QUERY_AUTHOR = "Author: ";
	public static final String QUERY_LENGTH = "How many minutes: ";
	public static final String QUERY_RATING = "What is the IMDB rating: ";

	public final static String MSG_WELCOME = "Hello and welcome to the Java Console Library! If this is the first time using this application you are recommended to type \"help\", this will give you the available commands. ";
	public final static String MSG_REGISTER_SUCCESS = "Media successfully added to library database. Awaiting further commands: "; // be
	public static final String MSG_DEREGISTER_SUCCESSFUL = "Item successfully deregistered. ";

	public final static String INFO_GENERAL_ORIENTATION = "These are the available commands: "; // Incomplete, needs to
																								// completed in help()
																								// method!
	public final static String INFO_CHECKIN = "The checkin command is used to check in a lended item such as a book or a movie into the library. Checked in items are ready to be lended out again. ";
	public final static String INFO_CHECKOUT = "The checkout command is used to check out an item such as a book or a movie from the library. Checked out items can't be lended again until they are returned and checked in. ";
	public final static String INFO_INFO = "The info command is used to view extended information about a particular item in the library database. ";
	public final static String INFO_REGISTER = "The register command is used to register new items into the library database. ";
	public final static String INFO_DEREGISTER = "The deregister command is used to remove an item from the library database. ";
	public final static String INFO_QUIT = "The quit command is used to exit the Java Console Library. All library contents are stored until the application is started again. ";
	public final static String INFO_LIST = "The list command is used to view a list of all items stored at the library. The list is ordered by article number. ";
	public final static String INFO_HELP = "Oh boy, you really need help don't you? ";
	private final static Scanner scanner = new Scanner(System.in);

	private CommunicationsUtility() {
	}

	public static String queryType() {
		System.out.println(QUERY_REGISTER_TYPE);
		System.out.print(QUERY_PROMPT);
		String input = getStringInput();
		if (input.equals("BOOK") | input.equals("MOVIE")) {
			return input;
		} else {
			System.out.println(MSG_ERROR_INVALIDINPUT);
			return queryType();
		}
	}

//public static void listLibraryContents(Library lib) { // TODO
////	lib.sortStoredMedia();// TODO use proper capitalisation!
//	System.out.println();
//	System.out.printf("%-20s", "Type:");
//	System.out.printf("%-20s", "Article number:");
//	System.out.printf("%-20s", "Title:");
//	System.out.printf("%-20s", "Author/IMDBRating");
////	if (lib.getStoredMedia().size() == 0) {
//		System.out.println();
//		System.out.println("-Library is empty. ");
//		return;
////	}
////	for (LendableMedia media : lib.getStoredMedia()) {
////		if (media instanceof Book) {
//			System.out.println();
//			System.out.printf("%-20s", "Book");
////			System.out.printf("%-20s", media.getArticleNr());
////			System.out.printf("%-20s", media.getTitle());
////			System.out.printf("%-20s", ((Book) media).getAuthor());
////		} else if (media instanceof Movie) {
//			System.out.println();
//			System.out.printf("%-20s", "Book");
////			System.out.printf("%-20s", media.getArticleNr());
////			System.out.printf("%-20s", media.getTitle());
////			System.out.printf("%-20s", ((Movie) media).getIMDBRating());
//		}
//		
////		if (media.isBorrowed()) {
//			System.out.println();
////			System.out.print("-Borrowed by: " + media.getBorrower().getName() + ", Phonenumber: " + media.getBorrower().getPhoneNr() + ", Due to be returned: " + media.getDueDate());
////		}
//	}
//	System.out.println();
//}

	public static String queryPhoneNumber() {
		System.out.println(QUERY_PHONENUMBER);
		System.out.print(QUERY_PROMPT);
		String input = getStringInput();
		if (!CommandInterpreter.isNumber(input)) {
			System.out.println(MSG_ERROR_INVALIDINPUT);
			return queryPhoneNumber();
		}
		return input;
	}

	public static String queryName() {
		System.out.println(QUERY_NAME);
		System.out.print(QUERY_PROMPT);
		String input = getStringInput();
		return input;
	}
//public static String queryArticleNumber(Library lib) {
//	System.out.println(QUERY_ARTICLENUMBER);
//	System.out.print(QUERY_PROMPT);
//	String input = getStringInput();
//	if (lib.isExistingArticleNumber(input)) {
//		System.out.println(MSG_ERROR_DOUBLEREGISTER);
//		return queryArticleNumber(lib);
//	}
//	if (!CommandInterpreter.isNumber(input)) {
//		System.out.println(MSG_ERROR_INVALIDINPUT);
//		return queryArticleNumber(lib);
//	}
//	return input;
//}

	public static String queryTitle() {
		System.out.println(QUERY_TITLE);
		System.out.print(QUERY_PROMPT);
		return getStringInput();
	}

	public static int queryValue() {
		boolean isValidInput;
		int value = 0;
		do {
			isValidInput = true;
			System.out.println(QUERY_VALUE);
			System.out.print(QUERY_PROMPT);
			try {
				value = scanner.nextInt();
			} catch (InputMismatchException e) {
				scanner.next(); // why is this needed?
				System.out.println(MSG_ERROR_INVALIDINPUT);
				isValidInput = false;
			}
		} while (!isValidInput);
		return value;
	}

	public static int queryLength() {
		boolean isValidInput;
		int length = 0;
		do {
			isValidInput = true;
			System.out.println(QUERY_LENGTH);
			System.out.print(QUERY_PROMPT);
			try {
				length = scanner.nextInt();
			} catch (InputMismatchException e) {
				scanner.next(); // why is this needed?
				System.out.println(MSG_ERROR_INVALIDINPUT);
				isValidInput = false;
			}
		} while (!isValidInput);
		return length;
	}

	public static double queryRating() { // TODO does not accept english format decimals ex: 9.1
		boolean isValidInput;
		double rating = 0;
		do {
			isValidInput = true;
			System.out.println(QUERY_RATING);
			System.out.print(QUERY_PROMPT);
			try {
				rating = scanner.nextDouble();
			} catch (InputMismatchException e) {
				scanner.next(); // why is this needed?
				System.out.println(MSG_ERROR_INVALIDINPUT);
				isValidInput = false;
			}
		} while (!isValidInput);
		return rating;
	}

	public static int queryPages() {
		boolean isValidInput;
		int pages = 0;
		do {
			isValidInput = true;
			System.out.println(QUERY_PAGES);
			System.out.print(QUERY_PROMPT);
			try {
				pages = scanner.nextInt();
			} catch (InputMismatchException e) {
				scanner.next();
				System.out.println(MSG_ERROR_INVALIDINPUT);
				isValidInput = false;
			}

		} while (!isValidInput);
		return pages;
	}

	public static String queryAuthor() {
		System.out.println(QUERY_AUTHOR);
		System.out.print(QUERY_PROMPT);
		return getStringInput();
	}

	public static String getStringInput() {// any formating?
		Scanner scn = new Scanner(System.in);
		String input = scn.nextLine();
		if (input.length() == 0) // no empty strings allowed, no dialogue needed
			return getStringInput();
		input = input.toUpperCase();// is this okay? or should it be done first when needed
		return input;
	}
}
