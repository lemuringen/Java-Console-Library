package application;

import java.io.IOException;
import java.util.EnumSet;
import java.util.InputMismatchException;
import java.util.Scanner;

import application.CommandInterpreter.Command;

public class LibraryController {
	private Library lib;
	private boolean isRunning;
	private LibraryStorageManager storageManager;

	public LibraryController(Library lib) {
		this.setLib(lib);
		this.storageManager = new LibraryStorageManager("./bin/application/Library", "contents.csv", lib);
	}

	public void init() { // stuff that needs to be done before running
//		storageManager.init();
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
		if (cmnd.getError() != CommandInterpreter.Error.NO_ERROR) {
			handleError(cmnd);
		} else if (checkForDatabaseInconsistencies(cmnd.getCommand(), argument)) { // tmp should separate check from
																					// handling
		} else if (argument != null) { // commands with arguments
			handleCommand(cmnd.getCommand(), argument);
		} else { // commands without arguments
			handleCommand(cmnd.getCommand());
		}
	}

	public boolean checkForDatabaseInconsistencies(CommandInterpreter.Command cmnd, String argument) { // tmp
//		if ((cmnd == CommandInterpreter.Command.CHECKIN || cmnd == CommandInterpreter.Command.CHECKOUT
//				|| cmnd == CommandInterpreter.Command.INFO || cmnd == CommandInterpreter.Command.DEREGISTER)
//				&& !lib.isExistingArticleNumber(argument)) {
//			handleBadArticleNumber();
//			return true;
//		}
//		if (cmnd == CommandInterpreter.Command.CHECKOUT && isLended(argument)) {
//			handleDoubleCheckOut();
//			return true;
//		}
//		if (cmnd == CommandInterpreter.Command.CHECKIN && !isLended(argument)) {
//			handleDoubleCheckIn();
//			return true;
//		}
		return false;
	}

	public void handleDoubleCheckIn() {
		System.out.println(CommunicationsUtility.MSG_ERROR_DOUBLECHECKIN);
	}

	public void handleDoubleCheckOut() {

		System.out.println(CommunicationsUtility.MSG_ERROR_DOUBLECHECKOUT);
	}

	public void handleBadArticleNumber() {
		System.out.println(CommunicationsUtility.MSG_ERROR_ARTICLENUMBER);
	}

	public void handleError(CommandInterpreter cmnd) {
		switch (cmnd.getError()) {
		case UNKNOWN_COMMAND:
			System.out.println(CommunicationsUtility.MSG_ERROR_UNKNOWNCOMMAND);
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

		}
	}

	// gives info about each command
	public void describeCommand(CommandInterpreter.Command command, String argument) {
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
		}
	}

//register item as borrowed, register Person as borrower to item TODO separate to query methods in utility, check phone number
	public void checkout(String articleNumber) {
//		lib.getMediaFromArticleNumber(articleNumber).borrow(new Person(CommunicationsUtility.queryName(), CommunicationsUtility.queryPhoneNumber()));
//		updateStorage();
	}


	public void handleCommand(CommandInterpreter.Command command, String argument) {
		switch (command) {
		case CHECKIN:
//			lib.getMediaFromArticleNumber(argument).checkIn();
			break;
		case CHECKOUT:
			checkout(argument);
			break;
		case DEREGISTER:
//			lib.getStoredMedia().remove(lib.getMediaFromArticleNumber(argument));
//			System.out.println(CommunicationsUtility.MSG_DEREGISTER_SUCCESSFUL);
//			updateStorage();
			break;
		case INFO:
//			System.out.println(lib.getMediaFromArticleNumber(argument).generalInfo());
			break;
		case HELP:
			describeCommand(command, argument);
			break;
		}
	}

	public void handleCommand(CommandInterpreter.Command command) {
		switch (command) {
		case QUIT:
			setRunning(false);
			break;
		case LIST:
//			CommunicationsUtility.listLibraryContents(getLib());
			break;
		case REGISTER:
			registerNewMedia();
			break;
		case HELP:
			System.out.println(CommunicationsUtility.INFO_GENERAL_ORIENTATION);
			listUserCommands();
			break;
		}
	}

	public void listUserCommands() {
		EnumSet<Command> commands = EnumSet.allOf(Command.class);
		commands.remove(CommandInterpreter.Command.UNKNOWN_COMMAND); // UNKNOWN_COMMAND is not meant to be used
																		// directly by user
		commands.forEach(command -> System.out.println(command));
	}

	public void registerNewMedia() { // TODO might be able to set avoid hardcoding the LendableMedia types
		String input = CommunicationsUtility.queryType();
		if (input.equals("BOOK")) {
			registerNewBook();
		} else if (input.equals("MOVIE")) {
			registerNewMovie();
		}
	}

	public void registerNewBook() {
//		String articleNr = CommunicationsUtility.queryArticleNumber(getLib());
		String title = CommunicationsUtility.queryTitle();
		int value = CommunicationsUtility.queryValue();
		int pages = CommunicationsUtility.queryPages();
		String author = CommunicationsUtility.queryAuthor();
//		lib.addLendableMedia(new Book(articleNr, title, value, pages, author));
//		updateStorage();
		// maybe add verification of successful storage
		System.out.println(CommunicationsUtility.MSG_REGISTER_SUCCESS);
	}

	public void registerNewMovie() { // TODO catch NumberFormatException
//		String articleNr = CommunicationsUtility.queryArticleNumber(getLib());
		String title = CommunicationsUtility.queryTitle();
		int value = CommunicationsUtility.queryValue();
		int length = CommunicationsUtility.queryLength();
		double rating = CommunicationsUtility.queryRating();
//		lib.addLendableMedia(new Movie(articleNr, title, value, length, rating));
//		updateStorage();
		System.out.println(CommunicationsUtility.MSG_REGISTER_SUCCESS);
	}

//	public void updateStorage() { // TODO
//		try {
//			storageManager.saveLibrary();
//		} catch (IOException e) {
//			System.out.println("The storage file has been altered during runtime. ");
//			if (lib.getStoredMedia().size() > 0) {
//				System.out.println("Restoring from working memory... ");
//				storageManager.makeStorageFolder();
//				try {
//					storageManager.saveLibrary();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//					System.out.print("Unknown error. Shutting down.");
//					System.exit(1);
//				}
//			} else {
//				init();
//			}
//			e.printStackTrace();
//		}
//	}

	public boolean isLended(String articleNumber) {
//		LendableMedia media = lib.getMediaFromArticleNumber(articleNumber); // lib method returns null if not finding
//		if (media == null || !media.isBorrowed()) {
			return false;
//		}
//		return true;
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
