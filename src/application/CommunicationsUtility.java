package application;

import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

import application.media.Book;
import application.media.LendableMedia;
import application.media.MediaCopy;
import application.media.Movie;

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
	public final static String MSG_ERROR_CHECKIN = "Syntax error: If you mean to check in an item. Type [checkin] followed by the article number corresponding to the item you want to check in. ";
	public final static String MSG_ERROR_CHECKOUT = "Syntax error: If you mean to check out an item. Type [checkout] followed by the article number corresponding to the item you want to check out. ";
	public final static String MSG_ERROR_DEREGISTER = "Syntax error: If you mean to [deregister] an item from the library database. Type [deregister] followed by the article number corresponding to the item you want to [deregister]. ";
	public final static String MSG_ERROR_REGISTER = "Syntax error: If you mean to [register] an item, type [register] without any arguments. ";
	public final static String MSG_ERROR_INFO = "Syntax error: If you mean to view extended information about a item. Type [info] followed by the article number corresponding to the item you want to read more about. ";
	public final static String MSG_ERROR_LIST = "Syntax error: Type [list] if you mean to view library contents. ";
	public final static String MSG_ERROR_QUIT = "Syntax error: Type [quit] without any arguments if you mean to [quit] the application. ";
	public final static String MSG_ERROR_HELP = "Syntax error: Type [help] followed by a command to get usage information about the command. Or simply type [help] to see available commands. ";
	public final static String MSG_ERROR_LOANS = "Syntax error: Type [loans] without quotation marks if you mean to view all currently lended items and corresponding borrowers. ";
	public final static String MSG_ERROR_SYNTAX = "Syntax error: Give a command followed by only one argument and keep in mind that article numbers can only contain integers. ";
	public final static String MSG_ERROR_ARTICLENUMBER = "No item with the given article number can be found in the library. ";
	public final static String MSG_ERROR_DOUBLEREGISTER = "An item with the given article number already exists in the library. ";
	public final static String MSG_ERROR_DOUBLECHECKOUT = "The item corresponding to the given article number is already checked out. ";
	final static public String MSG_ERROR_NOTLENDED = "The item you are to check out is currently not lended out to anyone. ";
	public final static String MSG_ERROR_DOUBLECHECKIN = "The item corresponding to the given article number is already checked in. ";
	// asterisk is meant to be replaced
	public final static String MSG_ERROR_UNKNOWNCOMMAND = "Java Console Library doesn't recognise \"*\" as a command. Type [help] to get [list] of available commands. "; 
	public final static String MSG_ERROR_INVALIDINPUT = "Invalid input. ";

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
	public final static String QUERY_REGISTER_NEWCOPY = "There is already a * registered with that article number. Would you like to register a new copy? [yes] or [no]. ";		
	
	public final static String MSG_WELCOME = "Hello and welcome to the Java Console Library! If this is the first time using this application you are recommended to type [help], this will give you the available commands. ";
	public final static String MSG_REGISTER_SUCCESS = "Media successfully added to library database. Awaiting further commands: ";
	public static final String MSG_DEREGISTER_SUCCESSFUL = "Item successfully deregistered. ";
	public final static String MSG_REGISTER_CANCELLED = "Returning to main menu. No item registered. ";
	
	// Incomplete, need to completed in help() method!
	public final static String INFO_GENERAL_ORIENTATION = "If you want information about how to use a particular command. Type [help] followed by the command. These are the available commands: "; 
	public final static String INFO_LOANS = "The [loans] commands is used to view all currently lended items and information about the corresponding borrowers. ";
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

	public static void listLibraryContents(Library lib) { // TODO
		lib.sortMedia();
		/*
		 * We want to match the headers with the corresponding list items vertically. We
		 * have headers Type|Article number|Title|Author/IMDBRating, since Type won't
		 * point to any changing items(only Book and Movie it can be fixed. And since
		 * Author/IMDBRating is last we don't need to determine how much space is needed
		 * to line it up with next element. So we only have to check for longest
		 * articleNumber and longest title!
		 */
		int maxArticleNumber = 14; // initial value == length of "Article Number"
		int maxTitle = 5; // initial value == length of "Title"
		Iterator<LendableMedia> storedMedia = lib.getStoredMediaIterator();
		LendableMedia media;
		while (storedMedia.hasNext()) {
			media = storedMedia.next();
			if (media.getArticleNr().length() > maxArticleNumber)
				maxArticleNumber = media.getArticleNr().length();
			if (media.getTitle().length() > maxTitle)
				maxTitle = media.getTitle().length();
		}
		listHeaders(maxArticleNumber, maxTitle);
		if (lib.isLibraryEmpty()) {
			listEmptyList();
		} else {
			storedMedia = lib.getStoredMediaIterator();
			while (storedMedia.hasNext()) {
				media = storedMedia.next();
				if (media instanceof Book) {
					listBook(maxArticleNumber, maxTitle, (Book) media);
				} else if (media instanceof Movie) {
					listMovie(maxArticleNumber, maxTitle, (Movie) media);
				}
				listStock(media);
			}
		}
		System.out.println();
	}

	private static void listHeaders(int maxArticleNumber, int maxTitle) {
		System.out.println();
		System.out.printf("%-7s", "Type:");
		System.out.printf("%-" + (maxArticleNumber + 2) + "s", "Article number:");
		System.out.printf("%-" + (maxTitle + 2) + "s", "Title:");
		System.out.printf("%-20s", "Author/IMDBRating:");
	}

	private static void listEmptyList() {
		System.out.println();
		System.out.println("-Library is empty. ");
	}

	private static void listBook(int maxArticleNumber, int maxTitle, Book media) {
		System.out.println();
		System.out.printf("%-7s", "Book");
		System.out.printf("%-" + (maxArticleNumber + 2) + "s", media.getArticleNr());
		System.out.printf("%-" + (maxTitle + 2) + "s", media.getTitle());
		System.out.printf("%-20s", upperCaseInitials(media.getAuthor()));
	}

	private static void listMovie(int maxArticleNumber, int maxTitle, Movie media) {
		System.out.println();
		System.out.printf("%-7s", "Movie");
		System.out.printf("%-" + (maxArticleNumber + 2) + "s", media.getArticleNr());
		System.out.printf("%-" + (maxTitle + 2) + "s", media.getTitle());
		System.out.printf("%-20s", media.getIMDBRating());
	}

	private static void listBorrower(MediaCopy copy) {
		System.out.println();
		System.out.print("	(Borrowed by: " + upperCaseInitials(copy.getBorrower().getName()) + ", Phonenumber: "
				+ copy.getBorrower().getPhoneNr() + ", Due to be returned: " + copy.getDueDate() + ")");
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

	private static void listStock(LendableMedia media) {
		System.out.println();
		System.out.println("	-Total stock: (" + media.getNumberOfCopies() + ") Available: ("
				+ media.getNumberOfAvailableCopies() + ") Lent out: (" + media.getNumberOfLendedCopies()
				+ ") Expired loans: (" + media.getNumberOfExpiredLoans() + ") ");
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

	public static String queryArticleNumber(Library lib) {
		System.out.println(QUERY_ARTICLENUMBER);
		System.out.print(QUERY_PROMPT);
		String input = getStringInput();
		if (!CommandInterpreter.isNumber(input)) {
			System.out.println(MSG_ERROR_INVALIDINPUT);
			return queryArticleNumber(lib);
		}
		return input;
	}

	public static String queryTitle() { // TODO should be first so that we can check if its already added
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