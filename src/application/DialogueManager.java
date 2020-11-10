package application;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

import application.media.Book;
import application.media.LendableMedia;
import application.media.MediaCopy;
import application.media.Movie;

public class DialogueManager {
	private boolean pendingStorageUpdate;
	private boolean pendingExit;

	DialogueManager() {
		this.pendingStorageUpdate = false;
		this.pendingExit = false;
	}

	/**
	 * First thing seen when loading the application
	 */
	public void welcome() {
		System.out.println();
		// "Splashscreen" with cheesy slogan
		System.out.println("Java Console Library\u2122 - Together, We can Learn Something");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println();
		System.out.println(
				"-Warning this application is not GDPR compliant. Please do not run Java Console Library inside the EEA.- ");
		System.out.println();
		System.out.println(CommunicationsUtility.MSG_WELCOME);
		checkforExpiredDueDate();
	}

	public void checkforExpiredDueDate() {
		Iterator<LendableMedia> storedMedia = LibraryController.lib.getStoredMediaIterator();
		ArrayList<MediaCopy> expiredCopies;
		LendableMedia media;
		System.out.println();
		System.out.println("The following loans are due to be returned by now: ");
		while (storedMedia.hasNext()) {
			media = storedMedia.next();
			expiredCopies = media.getExpiredCopies();
			if (expiredCopies.size() > 0) {
				for (MediaCopy copy : expiredCopies) {		
					System.out.print(media.getTitle() + " - Borrowed by: " + copy.getBorrower().getName() + " Their phonenumber: " + copy.getBorrower().getPhoneNr()+ " Loan due by: " + copy.getDueDate());	
				}
			}
		}
		System.out.println();
	}

	/**
	 * Takes user input and looks for commands within with the help of
	 * CommandInterpreter. Then directs to command handlers or error handlers. This
	 * is sort of the hub of the application.
	 */
	public void queryUserAction() {
		System.out.print(CommunicationsUtility.QUERY_PROMPT);
		CommandInterpreter cmnd = new CommandInterpreter(CommunicationsUtility.getStringInput());
		String argument = cmnd.getArgument();
		// first check in any errors
		if (cmnd.getError() != Error.NO_ERROR) {
			handleError(cmnd);
		} // check if the argument is registered articlenr for commands that requires it
		else if ((cmnd.getCommand() == Command.CHECKIN || cmnd.getCommand() == Command.CHECKOUT
				|| cmnd.getCommand() == Command.INFO || cmnd.getCommand() == Command.DEREGISTER)
				&& !LibraryController.lib.isExistingArticleNumber(argument)) {
			handleBadArticleNumber();
		} // cant lend an already lended media
		else if (cmnd.getCommand() == Command.CHECKOUT && !LibraryController.lib.getMedia(argument).isInStock()) {
			handleOutOfStockCheckOut();
		} else if (cmnd.getCommand() == Command.CHECKIN && !LibraryController.lib.getMedia(argument).isLended()) {
			checkInNotLended();
		} else if (argument != null) { // commands with arguments
			handleCommand(cmnd.getCommand(), argument);
		} else { // commands without arguments
			handleCommand(cmnd.getCommand());
		}
	}

	/**
	 * For now only gives an error message corresponding to the Error instance
	 * variable of cmnnd.
	 * 
	 * @param cmnd Contains all information from user input with command and error
	 *             interpretations.
	 */
	private void handleError(CommandInterpreter cmnd) {
		switch (cmnd.getError()) {
		case UNKNOWN_COMMAND:
			System.out.println(
					CommunicationsUtility.MSG_ERROR_UNKNOWNCOMMAND.replace("*", cmnd.getUnrecognizedCommand())); // TODO
			break;
		case INVALID_ARGUMENT:
			System.out.println(CommunicationsUtility.MSG_ERROR_SYNTAX);
			break;
		case LIST_ARGUMENT:
			System.out.println(CommunicationsUtility.MSG_ERROR_LIST);
			break;
		case CHECKOUT_NO_ARGUMENT:
			System.out.println(CommunicationsUtility.MSG_ERROR_CHECKOUT);
			break;
		case CHECKIN_NO_ARGUMENT:
			System.out.println(CommunicationsUtility.MSG_ERROR_CHECKIN);
			break;
		case REGISTER_ARGUMENT:
			System.out.println(CommunicationsUtility.MSG_ERROR_REGISTER);
			break;
		case DEREGISTER_NO_ARGUMENT:
			System.out.println(CommunicationsUtility.MSG_ERROR_DEREGISTER);
			break;
		case INFO_NO_ARGUMENT:
			System.out.println(CommunicationsUtility.MSG_ERROR_INFO);
			break;
		case QUIT_ARGUMENT:
			System.out.println(CommunicationsUtility.MSG_ERROR_QUIT);
			break;
		case HELP_ARGUMENT:
			System.out.println(CommunicationsUtility.MSG_ERROR_HELP);
			break;
		case LOANS_ARGUMENT:
			System.out.println(CommunicationsUtility.MSG_ERROR_LOANS);
			break;
		}
	}

	/**
	 * Gives info about each command. Is invoked when user types [help] [command]
	 * 
	 * @param argument Argumented command.
	 */
	private void describeCommand(String argument) {
		switch (argument) {
		case "CHECKIN":
			System.out.println(CommunicationsUtility.INFO_CHECKIN);
			break;
		case "CHECKOUT":
			System.out.println(CommunicationsUtility.INFO_CHECKOUT);
			break;
		case "INFO":
			System.out.println(CommunicationsUtility.INFO_INFO);
			break;
		case "DEREGISTER":
			System.out.println(CommunicationsUtility.INFO_DEREGISTER);
			break;
		case "REGISTER":
			System.out.println(CommunicationsUtility.INFO_REGISTER);
			break;
		case "QUIT":
			System.out.println(CommunicationsUtility.INFO_QUIT);
			break;
		case "LIST":
			System.out.println(CommunicationsUtility.INFO_LIST);
			break;
		case "HELP":
			System.out.println(CommunicationsUtility.INFO_HELP);
			break;
		case "LOANS":
			System.out.println(CommunicationsUtility.INFO_LOANS);
			break;
		}
	}

	/**
	 * handles commands with argument, should already be determined that argument is
	 * fit for the command
	 * 
	 * @param command
	 * @param argument
	 */
	private void handleCommand(Command command, String argument) {
		switch (command) {
		case CHECKIN:
			checkIn(argument);
			break;
		case CHECKOUT:
			checkout(argument);
			break;
		case DEREGISTER:
			deregister(argument);
			break;
		case INFO:
			giveProductInfo(argument);
			break;
		case HELP:
			describeCommand(argument);
			break;
		case LOANS:
			if(argument.equalsIgnoreCase("-expired")) {
				checkforExpiredDueDate();
			}else {
			showPersonalLoans(argument);
			}
			break;
		}
	}

	private void handleCommand(Command command) {
		switch (command) {
		case QUIT:
			setPendingExit(true);
			break;
		case LIST:
			listLibraryContents(LibraryController.lib);
			break;
		case REGISTER:
			registerMedia();
			break;
		case HELP:
			System.out.println(CommunicationsUtility.INFO_GENERAL_ORIENTATION);
			listUserCommands();
			break;
		case LOANS:
			showAllLoans();
			break;
		}
	}

	/**
	 * When trying to check in an item that has no borrowers.
	 */
	private void checkInNotLended() {
		System.out.println(CommunicationsUtility.MSG_ERROR_NOTLENDED);
	}

	/**
	 * When trying to check out an item that is out of stock.
	 */
	private void handleOutOfStockCheckOut() {
		System.out.println(CommunicationsUtility.MSG_ERROR_CHECKOUT_OUTOFSTOCK);
	}

	/**
	 * When commands that needs a registered article number gets a non registered
	 * article number as argument
	 */
	private void handleBadArticleNumber() {
		System.out.println(CommunicationsUtility.MSG_ERROR_ARTICLENUMBER);
	}

//TODO
	private void checkout(String articleNumber) {
		String name = queryName();
		Person borrower = LibraryController.lib.getBorrower(name);
		if (borrower != null) {
			LibraryController.lib.getMedia(articleNumber).setCopyToLent(borrower);
			System.out.println("Another book lended by " + name + ". ");
		} else {
			LibraryController.lib.getMedia(articleNumber).setCopyToLent(new Person(name, queryPhoneNumber()));
			System.out.println("Book lended by " + name + ". ");
		}
		setPendingStorageUpdate(true);
	}

	/**
	 * Gives a dialogue tree that can lead to removal of a MediaCopy or a
	 * LendableMedia from the library. Does not work if any underlying copy is
	 * lended out(borrower!=null).
	 * 
	 * @param articleNumber
	 */
	public void deregister(String articleNumber) { // TODO
		LendableMedia media = LibraryController.lib.getMedia(articleNumber);
		boolean validInput = false;
		System.out.println("Would you like to deregister an individual [copy] or [all] copies of *? ".replace("*",
				media.getTitle()));
		do {
			System.out.print(CommunicationsUtility.QUERY_PROMPT);
			String answer = CommunicationsUtility.getStringInput();
			if (answer.equalsIgnoreCase("copy")) {
				validInput = true;
				deregisterCopy(media);
			} else if (answer.equalsIgnoreCase("all")) {
				deregisterMedia(media);
				validInput = true;
			} else {
				System.out.print("Answer either [copy] or [all].");
			}
		} while (!validInput);

	}

	/**
	 * Deregister a whole media article. Is not possible when any copy is currently
	 * checked out.
	 * 
	 * @param media
	 */
	private void deregisterMedia(LendableMedia media) {
		if (media.getNumberOfLendedCopies() > 0) {
			System.out.println(CommunicationsUtility.MSG_ERROR_DEREGISTER_CHECKEDOUT.replace("*", "a media"));
		} else {
			LibraryController.lib.removeMedia(media.getArticleNr()); // TODO should dissallow if currently lended
			System.out.println(CommunicationsUtility.MSG_DEREGISTER_SUCCESSFUL);
			setPendingStorageUpdate(true);
		}
	}

	/**
	 * Lists all copies of a media showing their serial numbers, borrowers and due
	 * dates.
	 * 
	 * @param media
	 */
	private void listCopies(LendableMedia media) {
		Iterator<MediaCopy> copyIterator = media.getCopyIterator();
		while (copyIterator.hasNext()) {
			MediaCopy copy = copyIterator.next();
			System.out.println(copy.getSerialNumber() + ". "
					+ CommunicationsUtility.upperCaseInitials(copy.getBorrower().getName()) + ", Due to be returned: "
					+ copy.getDueDate());
		}
	}

	/**
	 * Deregister a specific copy using its serial number.
	 * 
	 * @param media
	 */
	private void deregisterCopy(LendableMedia media) {
		listCopies(media); // list all copies with their serialnumbers
		int sn = -1; // (to init)should be impossible to get this serialnumber
		do {
			System.out.print(CommunicationsUtility.QUERY_PROMPT);
			String input = CommunicationsUtility.getStringInput();
			// we need to check if it's a number before converting
			if (CommunicationsUtility.isNumber(input)) {
				sn = Integer.valueOf(input);
			}
		} while (!media.isExistingCopy(sn));
		if (!media.getCopy(sn).isBorrowed()) {
			media.removeCopy(sn);
			System.out.println(CommunicationsUtility.MSG_DEREGISTER_SUCCESSFUL);
			setPendingStorageUpdate(true);
		} else {
			System.out.println(CommunicationsUtility.MSG_ERROR_DEREGISTER_CHECKEDOUT.replace("*", "the copy"));
		}
	}

	/**
	 * Prints detailed information about a LendableMedia as well printing its
	 * potential borrowers.
	 * 
	 * @param articleNumber
	 */
	public void giveProductInfo(String articleNumber) {
		LendableMedia media = LibraryController.lib.getMedia(articleNumber);
		ArrayList<MediaCopy> lendedCopies = media.getLendedCopies();
		System.out.println(media.detailedInfo());
		System.out.println("Currently in stock: " + media.getNumberOfAvailableCopies());
		System.out.println();
		System.out.println("Currently lended to: ");
		if (lendedCopies.size() > 0) {
			for (MediaCopy copy : lendedCopies) {
				// print borrowers info
				System.out.print("SN: " + copy.getSerialNumber() + " - Borrowed by: " + copy.getBorrower().getName()
						+ ", Phonenumber: " + copy.getBorrower().getPhoneNr() + ", Due to be returned: "
						+ copy.getDueDate());
				if (copy.isDue()) { // warns if copy is due to be returned
					System.out.println(" (Warning: return date has expired!) ");
				} else {
					System.out.println();
				}
			}
		} else {
			System.out.println("-No copies currently lended out. ");
		}
	}

	/**
	 * Shows all loans from the library in the argumented in name.
	 * 
	 * @param name
	 */
	public void showPersonalLoans(String name) {
		if (LibraryController.lib.isPersonBorrowing(name)) {
			Iterator<LendableMedia> loanedMedia = LibraryController.lib.getLoanedMedia(name);
			LendableMedia media;
			MediaCopy copy;
			while (loanedMedia.hasNext()) {
				media = loanedMedia.next();
				copy = LibraryController.lib.getLoanedCopy(name, media);
				System.out.print(media.getTitle() + " - Loan due by: " + copy.getDueDate());
				if (copy.isDue()) { // warns if copy is due to be returned
					System.out.println(" (Warning: return date has expired!) ");
				} else {
					System.out.println();
				}
			}
		} else {
			System.out.println(CommunicationsUtility.upperCaseInitials(name)
					+ CommunicationsUtility.MSG_ERROR_LOANS_NONREGISTERED);
		}
	}

	/**
	 * Gives a list of all currently lended articles and their borrowers.
	 */
	public void showAllLoans() {
		System.out.println("These are the currently lended items and their borrowers: ");
		System.out.println();
		boolean nothingLended = true;
		ArrayList<MediaCopy> lendedCopies;
		Iterator<LendableMedia> storedMedia = LibraryController.lib.getStoredMediaIterator();
		LendableMedia media;
		while (storedMedia.hasNext()) {
			media = storedMedia.next();
			lendedCopies = media.getLendedCopies();
			if (lendedCopies.size() > 0) {
				nothingLended = false;
				System.out.println("Article number: " + media.getArticleNr() + " Title: " + media.getTitle());
				for (MediaCopy copy : lendedCopies) {
					System.out.print("Serial number: " + copy.getSerialNumber() + ", Borrowed by: "
							+ CommunicationsUtility.upperCaseInitials(copy.getBorrower().getName())
							+ ", Their phonenumber: " + copy.getBorrower().getPhoneNr() + " Due to be returned: "
							+ copy.getDueDate());
					if (copy.isDue()) { // warns if copy is due to be returned
						System.out.println(" (Warning: return date has expired!) ");
					} else {
						System.out.println();
					}
				}

				System.out.println();
			}
		}
		if (nothingLended) {
			System.out.println("-Nothing currently lended out.");
		}
	}

	/**
	 * Checks in a borrowed media. It assumes that there are copies to check in.
	 * User is given a list of currently lent out examples of the specified article.
	 * 
	 * @param articleNumber
	 */

	public void checkIn(String articleNumber) {
		LendableMedia media = LibraryController.lib.getMedia(articleNumber);
		System.out.println(CommunicationsUtility.QUERY_CHECKIN_SN);
		Iterator<MediaCopy> copies = media.getCopyIterator();
		while (copies.hasNext()) {
			MediaCopy copy = copies.next();
			System.out.println(copy.getSerialNumber() + ". " + copy.getBorrower().getName() + copy.getDueDate());
		}
		int serialNumber = querySerialNumber(media);
		LibraryController.lib.checkInCopy(media, serialNumber);
		setPendingStorageUpdate(true);
	}

	private void listUserCommands() {
		EnumSet<Command> commands = EnumSet.allOf(Command.class);
		commands.remove(Command.UNKNOWN_COMMAND); // UNKNOWN_COMMAND is not meant to be used
													// directly by user
		commands.forEach(command -> System.out.println(command));
	}

	private void registerMedia() { // TODO might be able to set avoid hardcoding the LendableMedia types
		String articleNumber = queryArticleNumber(LibraryController.lib);
		if (LibraryController.lib.isExistingArticleNumber(articleNumber)) {
			registerOldMedia(articleNumber);
		} else {
			registerNewMedia(articleNumber);
		}
	}

	public String queryYesOrNo() {
		System.out.println("[yes] or [no]");
		System.out.print(CommunicationsUtility.QUERY_PROMPT);
		String input = CommunicationsUtility.getStringInput();
		if (input.equalsIgnoreCase("yes")) {
			return "yes";
		} else if (input.equalsIgnoreCase("no")) {
			return "yes";
		} else {
			System.out.println("Please answer [yes] or [no]. ");
			return queryYesOrNo();
		}
	}

	public int querySerialNumber(LendableMedia media) {

		System.out.print(CommunicationsUtility.QUERY_PROMPT);
		String input = CommunicationsUtility.getStringInput();
		int sn;
		if (CommunicationsUtility.isNumber(input)) {
			sn = Integer.valueOf(input);
			if (media.isExistingCopy(sn)) {
				return sn;
			}
		}
		System.out.println("Please insert a valid serial number. ");
		return querySerialNumber(media);
	}

	private void registerOldMedia(String articleNumber) {
		LendableMedia media = LibraryController.lib.getMedia(articleNumber);
		String type;
		if (media instanceof Book) {
			type = "book";
		} else if (media instanceof Movie) {
			type = "movie";
		} else {
			type = "_error_";
		}
		System.out.println(CommunicationsUtility.QUERY_REGISTER_NEWCOPY.replace("*", type));
		String answer = queryYesOrNo();
		if (answer.equalsIgnoreCase("yes")) {
			registerNewCopy(media);
		} else if (answer.equalsIgnoreCase("no")) {
			System.out.println(CommunicationsUtility.MSG_REGISTER_CANCELLED);
		}
	}

	private void registerNewMedia(String articleNumber) {
		String type = queryType();
		if (type.equals("BOOK")) {
			registerBook(articleNumber);
		} else if (type.equals("MOVIE")) {
			registerMovie(articleNumber);
		}
	}

	private void registerNewCopy(LendableMedia media) {
		// get a new copy and add it
		media.addCopy(media.getCopy());
		setPendingStorageUpdate(true);
		System.out.println("Added new copy of " + media.getTitle());
	}

	private void registerBook(String articleNumber) {
		Book book = Book.getBook(articleNumber, LibraryController.lib);
		String title = queryTitle();
		int value = queryValue();
		int pages = queryPages();
		String author = queryAuthor();
		book.setUpBook(title, value, pages, author);
		// automatically adds new copy... maybe not best practice
		book.addCopy(book.getCopy());
		LibraryController.lib.addLendableMedia(book);
		// TODO should have unique message that new book is registered
		setPendingStorageUpdate(true);
		// maybe add verification of successful storage
		System.out.println(CommunicationsUtility.MSG_REGISTER_SUCCESS);
	}

	private void registerMovie(String articleNumber) { // TODO catch NumberFormatException
		Movie movie = Movie.getMovie(articleNumber, LibraryController.lib);
		String title = queryTitle();
		int value = queryValue();
		int length = queryLength();
		double rating = queryRating();
		movie.setUpMovie(title, value, length, rating);
		// automatically adds new copy... maybe not best practice
		movie.addCopy(movie.getCopy());
		LibraryController.lib.addLendableMedia(movie);
		setPendingStorageUpdate(true);
		System.out.println(CommunicationsUtility.MSG_REGISTER_SUCCESS);

	}

	public static String queryType() {
		System.out.println(CommunicationsUtility.QUERY_REGISTER_TYPE);
		System.out.print(CommunicationsUtility.QUERY_PROMPT);
		String input = CommunicationsUtility.getStringInput();
		if (input.equals("BOOK") | input.equals("MOVIE")) {
			return input;
		} else {
			System.out.println(CommunicationsUtility.MSG_ERROR_INVALIDINPUT);
			return queryType();
		}
	}

	public static String queryPhoneNumber() {
		System.out.println(CommunicationsUtility.QUERY_PHONENUMBER);
		System.out.print(CommunicationsUtility.QUERY_PROMPT);
		String input = CommunicationsUtility.getStringInput();
		if (!CommunicationsUtility.isNumber(input)) {
			System.out.println(CommunicationsUtility.MSG_ERROR_INVALIDINPUT);
			return queryPhoneNumber();
		}
		return input;
	}

	public static String queryName() {
		System.out.println(CommunicationsUtility.QUERY_NAME);
		System.out.print(CommunicationsUtility.QUERY_PROMPT);
		String input = CommunicationsUtility.getStringInput();
		return input;
	}

	public static String queryArticleNumber(Library lib) {
		System.out.println(CommunicationsUtility.QUERY_ARTICLENUMBER);
		System.out.print(CommunicationsUtility.QUERY_PROMPT);
		String input = CommunicationsUtility.getStringInput();
		if (!CommunicationsUtility.isNumber(input)) {
			System.out.println(CommunicationsUtility.MSG_ERROR_INVALIDINPUT);
			return queryArticleNumber(lib);
		}
		return input;
	}

	public static String queryTitle() { // TODO should be first so that we can check if its already added
		System.out.println(CommunicationsUtility.QUERY_TITLE);
		System.out.print(CommunicationsUtility.QUERY_PROMPT);
		return CommunicationsUtility.getStringInput().trim();
	}

	public static int queryValue() {
		Scanner scanner = new Scanner(System.in);
		boolean isValidInput;
		int value = 0;
		do {
			isValidInput = true;
			System.out.println(CommunicationsUtility.QUERY_VALUE);
			System.out.print(CommunicationsUtility.QUERY_PROMPT);
			try {
				value = scanner.nextInt();
			} catch (InputMismatchException e) {
				scanner.next(); // why is this needed?
				System.out.println(CommunicationsUtility.MSG_ERROR_INVALIDINPUT);
				isValidInput = false;
			}
		} while (!isValidInput);
		return value;
	}

	public static int queryLength() {
		Scanner scanner = new Scanner(System.in);
		boolean isValidInput;
		int length = 0;
		do {
			isValidInput = true;
			System.out.println(CommunicationsUtility.QUERY_LENGTH);
			System.out.print(CommunicationsUtility.QUERY_PROMPT);
			try {
				length = scanner.nextInt();
			} catch (InputMismatchException e) {
				scanner.next(); // why is this needed?
				System.out.println(CommunicationsUtility.MSG_ERROR_INVALIDINPUT);
				isValidInput = false;
			}
		} while (!isValidInput);
		return length;
	}

	public static double queryRating() { // TODO does not accept english format decimals ex: 9.1
		Scanner scanner = new Scanner(System.in);
		boolean isValidInput;
		double rating = 0;
		do {
			isValidInput = true;
			System.out.println(CommunicationsUtility.QUERY_RATING);
			System.out.print(CommunicationsUtility.QUERY_PROMPT);
			try {
				rating = scanner.nextDouble();
			} catch (InputMismatchException e) {
				scanner.next(); // why is this needed?
				System.out.println(CommunicationsUtility.MSG_ERROR_INVALIDINPUT);
				isValidInput = false;
			}
		} while (!isValidInput);
		return rating;
	}

	public static int queryPages() {
		Scanner scanner = new Scanner(System.in);
		boolean isValidInput;
		int pages = 0;
		do {
			isValidInput = true;
			System.out.println(CommunicationsUtility.QUERY_PAGES);
			System.out.print(CommunicationsUtility.QUERY_PROMPT);
			try {
				pages = scanner.nextInt();
			} catch (InputMismatchException e) {
				scanner.next();
				System.out.println(CommunicationsUtility.MSG_ERROR_INVALIDINPUT);
				isValidInput = false;
			}

		} while (!isValidInput);
		return pages;
	}

	public static String queryAuthor() {
		System.out.println(CommunicationsUtility.QUERY_AUTHOR);
		System.out.print(CommunicationsUtility.QUERY_PROMPT);
		return CommunicationsUtility.getStringInput();
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
		System.out.printf("%-9s", "Type:");
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
		System.out.printf("%-9s", "{Book}");
		System.out.printf("%-" + (maxArticleNumber + 2) + "s", media.getArticleNr());
		System.out.printf("%-" + (maxTitle + 2) + "s", media.getTitle());
		System.out.printf("%-20s", CommunicationsUtility.upperCaseInitials(media.getAuthor()));
	}

	private static void listMovie(int maxArticleNumber, int maxTitle, Movie media) {
		System.out.println();
		System.out.printf("%-9s", "{Movie}");
		System.out.printf("%-" + (maxArticleNumber + 2) + "s", media.getArticleNr());
		System.out.printf("%-" + (maxTitle + 2) + "s", media.getTitle());
		System.out.printf("%-20s", media.getIMDBRating());
	}

	private static void listBorrower(MediaCopy copy) {
		System.out.println();
		System.out.print("	(Borrowed by: " + CommunicationsUtility.upperCaseInitials(copy.getBorrower().getName())
				+ ", Phonenumber: " + copy.getBorrower().getPhoneNr() + ", Due to be returned: " + copy.getDueDate()
				+ ")");
	}

	private static void listStock(LendableMedia media) {
		System.out.println();
		System.out.println("	-Total stock: (" + media.getNumberOfCopies() + ") Available: ("
				+ media.getNumberOfAvailableCopies() + ") Lent out: (" + media.getNumberOfLendedCopies()
				+ ") Expired loans: (" + media.getNumberOfExpiredLoans() + ") ");
	}

	public boolean isPendingStorageUpdate() {
		return pendingStorageUpdate;
	}

	public void setPendingStorageUpdate(boolean pendingStorageUpdate) {
		this.pendingStorageUpdate = pendingStorageUpdate;
	}

	public boolean isPendingExit() {
		return pendingExit;
	}

	public void setPendingExit(boolean pendingExit) {
		this.pendingExit = pendingExit;
	}
}
