package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;

import application.media.Book;
import application.media.LendableMedia;
import application.media.MediaCopy;
import application.media.Movie;

public class LibraryController {
	private Library lib;
	private boolean isRunning;
	private LibraryStorageManager storageManager;

	public LibraryController(Library lib) {
		this.setLib(lib);
		this.storageManager = new LibraryStorageManager("./bin/application/Library", "contents.csv", lib);
	}

	public void init() { // stuff that needs to be done before running
		storageManager.init();
	}

	public void start() { // start the application and gives welcome message
		setRunning(true);
		System.out.println(CommunicationsUtility.MSG_WELCOME);
	}

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
				&& !lib.isExistingArticleNumber(argument)) {
			handleBadArticleNumber();
		} // cant lend an already lended media
		else if (cmnd.getCommand() == Command.CHECKOUT && !lib.getMedia(argument).isInStock()) {
			handleOutOfStockCheckOut();
		} else if (cmnd.getCommand() == Command.CHECKIN && !lib.getMedia(argument).isLended()) {
			handleNotLended();
		} else if (argument != null) { // commands with arguments

			handleCommand(cmnd.getCommand(), argument);
		} else { // commands without arguments
			handleCommand(cmnd.getCommand());
		}
	}

	private void handleNotLended() {
		System.out.println(CommunicationsUtility.MSG_ERROR_NOTLENDED);
	}

	private void handleOutOfStockCheckOut() {

		System.out.println(CommunicationsUtility.MSG_ERROR_DOUBLECHECKOUT); // TODO
	}

	private void handleBadArticleNumber() {
		System.out.println(CommunicationsUtility.MSG_ERROR_ARTICLENUMBER);
	}

	private void handleError(CommandInterpreter cmnd) {
		switch (cmnd.getError()) {
		case UNKNOWN_COMMAND:
			System.out.println(
					CommunicationsUtility.MSG_ERROR_UNKNOWNCOMMAND.replace("*", cmnd.getUnrecognizedCommand()));
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

	// gives info about each command
	private void describeCommand(Command command, String argument) {
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

//register item as borrowed, register Person as borrower to item TODO separate to query methods in utility, check phone number
	private void checkout(String articleNumber) {
		String name = CommunicationsUtility.queryName();
		if (lib.isPersonBorrowing(name)) {
			Person borrower = lib.getBorrower(name);
			lib.getMedia(articleNumber).setCopyToLent(borrower);
			System.out.println("Another book lended by " + name + ". ");
		} else {
			lib.getMedia(articleNumber).setCopyToLent(new Person(name, CommunicationsUtility.queryPhoneNumber()));
			System.out.println("Book lended by " + name + ". ");
		}
		updateStorage();
	}

//handles commands with argument, should already be determined that argument is fit for the command
	private void handleCommand(Command command, String argument) {
		switch (command) {
		case CHECKIN:
			// lib.getMediaFromArticleNumber(argument).checkIn(); // TODO should be possible
			// to checkin with name and sn
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
			describeCommand(command, argument);
			break;
		case LOANS:
			showPersonalLoans(argument);
		}
	}

	private void handleCommand(Command command) {
		switch (command) {
		case QUIT:
			setRunning(false);
			break;
		case LIST:
			CommunicationsUtility.listLibraryContents(getLib());
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

	public void deregister(String articleNumber) { // TODO
		LendableMedia media = lib.getMedia(articleNumber);
		boolean validInput = false;
		do {
			System.out.println("Would you like to deregister an individual [copy] or [all] copies of *? ".replace("*",
					media.getTitle()));
			String answer = CommunicationsUtility.getStringInput();
			if (answer.equalsIgnoreCase("copy")) {
				validInput = true;
				for (MediaCopy copy : media.getCopies().values()) {
					System.out
							.println(copy.getSerialNumber() + ". " + copy.getBorrower().getName() + copy.getDueDate());
				}
				int sn;
				do {
					sn = Integer.valueOf(CommunicationsUtility.getStringInput());
				} while (!media.getCopies().containsKey(sn));
				if (!media.getCopies().get(sn).isBorrowed()) {
					media.getCopies().remove(sn);
				} else {
					System.out.println(
							"The copy need to be checked in before you can deregister the book! Returning to the main menu. ");
					return;
				}
			} else if (answer.equalsIgnoreCase("all")) {
				if (media.getNumberOfLendedCopies() > 0) {
					System.out.println(
							"All copies need to be checked in before you can deregister the book! Returning to the main menu. ");
					return;
				} else {
					lib.removeMedia(articleNumber); // TODO should dissallow if currently lended
					validInput = true;
				}
			}
		} while (!validInput);
		System.out.println(CommunicationsUtility.MSG_DEREGISTER_SUCCESSFUL);
		updateStorage();
	}

	public void giveProductInfo(String articleNumber) {
		LendableMedia media = lib.getMedia(articleNumber);
		ArrayList<MediaCopy> lendedCopies = media.getLendedCopies();
		System.out.println(media.generalInfo());
		System.out.println("Currently in stock: " + media.getNumberOfAvailableCopies());
		System.out.println("Currently lended: ");
		System.out.println();
		if (lendedCopies.size() > 0) {
			for (MediaCopy copy : lendedCopies) {
				System.out.println("SN: " + copy.getSerialNumber() + " - Borrowed by: " + copy.getBorrower().getName()
						+ ", Phonenumber: " + copy.getBorrower().getPhoneNr() + " Due to be returned: "
						+ copy.getDueDate());
			}
		}
	}

	public void showPersonalLoans(String name) {
		if (lib.isPersonBorrowing(name)) { // TODO check out so its the right format of name
			Person person = lib.getBorrower(name);
			for (MediaCopy copy : person.getBorrowedCopy()) { // TODO need name of media or article number
				System.out.println("Loan due by: " + copy.getDueDate());
			}
		}
	}

	public void showAllLoans() {
		System.out.println("These are the currently lended items and their borrowers: ");
		System.out.println();
		boolean nothingLended = true;
		ArrayList<MediaCopy> lendedCopies;
		Iterator<LendableMedia> storedMedia = lib.getStoredMediaIterator();
		LendableMedia media;
		while (storedMedia.hasNext()) {
			media = storedMedia.next();
			lendedCopies = media.getLendedCopies();
			if (lendedCopies.size() > 0) {
				nothingLended = false;
				System.out.println("Article number: " + media.getArticleNr() + " Title: " + media.getTitle());
				for (MediaCopy copy : lendedCopies) {
					System.out.println("Serial number: " + copy.getSerialNumber() + ", Borrowed by: "
							+ CommunicationsUtility.upperCaseInitials(copy.getBorrower().getName()) + ", Their phonenumber: " + copy.getBorrower().getPhoneNr()
							+ " Due to be returned: " + copy.getDueDate());
				}
				System.out.println();
			}
		}
		if (nothingLended) {
			System.out.println("-Nothing currently lended out.");
		}
	}

	public void checkIn(String argument) { // TODO
		LendableMedia media = lib.getMedia(argument);
		if (media.getCopies().size() == 0) {
			System.out.println("No copies of the item is registered in the library database. ");
			return;
		}
		if (media.isNotLended()) {
			System.out.println("No copies currently lended out. ");
			return;
		}
		System.out.println(
				"Which copy would you like to check in? Answer with the serial number noted first of the line on every listed person in the following list: ");
		boolean validInput;

		for (MediaCopy copy : media.getCopies().values()) {
			System.out.println(copy.getSerialNumber() + ". " + copy.getBorrower().getName() + copy.getDueDate());
		}
		do {
			validInput = false;
			int sn = Integer.valueOf(CommunicationsUtility.getStringInput());
			if (media.getCopies().containsKey(sn)) {
				lib.checkInCopy(media, sn);
				validInput = true;
			}
		} while (!validInput);
		updateStorage();
	}

	private void listUserCommands() {
		EnumSet<Command> commands = EnumSet.allOf(Command.class);
		commands.remove(Command.UNKNOWN_COMMAND); // UNKNOWN_COMMAND is not meant to be used
													// directly by user
		commands.forEach(command -> System.out.println(command));
	}

	public String queryYesOrNo() {
		String input = CommunicationsUtility.getStringInput();
		System.out.println("[yes] or [no]");
		System.out.println(CommunicationsUtility.QUERY_PROMPT);
		if (input.equalsIgnoreCase("yes")) {
			return "yes";
		} else if (input.equalsIgnoreCase("no")) {
			return "yes";
		} else {
			System.out.println("Please answer [yes] or [no]. ");
			return queryYesOrNo();
		}
	}

	private void registerMedia() { // TODO might be able to set avoid hardcoding the LendableMedia types
		String articleNumber = CommunicationsUtility.queryArticleNumber(getLib());
		if (lib.isExistingArticleNumber(articleNumber)) {
			registerOldMedia(articleNumber);
		} else {
			registerNewMedia(articleNumber);
		}
	}

	private void registerOldMedia(String articleNumber) {
		LendableMedia media = lib.getMedia(articleNumber);
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
		String type = CommunicationsUtility.queryType();
		if (type.equals("BOOK")) {
			registerBook(articleNumber);
		} else if (type.equals("MOVIE")) {
			registerMovie(articleNumber);
		}
	}

	private void registerNewCopy(LendableMedia media) {
		// get a new copy and add it
		media.addCopy(media.getCopy());
		System.out.println("Added new copy of " + media.getTitle());
	}

	private void registerBook(String articleNumber) {
		Book book = Book.getBook(articleNumber, lib);
		String title = CommunicationsUtility.queryTitle();
		int value = CommunicationsUtility.queryValue();
		int pages = CommunicationsUtility.queryPages();
		String author = CommunicationsUtility.queryAuthor();
		book.setUpBook(title, value, pages, author);
		// automatically adds new copy... maybe not best practice
		book.addCopy(book.getCopy());
		lib.addLendableMedia(book);
		// TODO should have unique message that new book is registered
		updateStorage();
		// maybe add verification of successful storage
		System.out.println(CommunicationsUtility.MSG_REGISTER_SUCCESS);
	}

	private void registerMovie(String articleNumber) { // TODO catch NumberFormatException
		Movie movie = Movie.getMovie(articleNumber, lib);
		String title = CommunicationsUtility.queryTitle();
		int value = CommunicationsUtility.queryValue();
		int length = CommunicationsUtility.queryLength();
		double rating = CommunicationsUtility.queryRating();
		movie.setUpMovie(title, value, length, rating);
		// automatically adds new copy... maybe not best practice
		movie.addCopy(movie.getCopy());
		lib.addLendableMedia(movie);
		updateStorage();
		System.out.println(CommunicationsUtility.MSG_REGISTER_SUCCESS);

	}

	public void updateStorage() { // TODO
		try {
			storageManager.storeLibrary();
		} catch (IOException e) {
			System.out.println("The storage file has been altered during runtime. ");
			if (!lib.isLibraryEmpty()) {
				System.out.println("Restoring from working memory... ");
				storageManager.init(); // TODO
				try {
					storageManager.storeLibrary();
				} catch (IOException e1) {
					e1.printStackTrace();
					System.out.print("Unknown error. Shutting down.");
					System.exit(1);
				}
			} else {
				init();
			}
			e.printStackTrace();
		}
	}

	public Library getLib() {
		return lib;
	}

	public void setLib(Library lib) {
		this.lib = lib;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
}
