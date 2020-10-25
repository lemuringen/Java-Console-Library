package application;

import java.io.File;
import java.util.regex.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.EnumSet;
import java.util.Scanner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import application.CommandInterpreter.Command;

public class LibraryController {
	private Library lib;
	private boolean isRunning;
	private String libPath; // directory path to our Library storage, might be better to let the library
							// hold this info for itself
	// Dialogue
	private final String CHECK_IN_ERROR = "Syntax error: If you mean to check in media. Type \"checkin [Article Number]\" without quotation marks and with \"[Article Number]\" replaced with the article number corresponding to the media you want to check in. ";
	private final String CHECK_OUT_ERROR = "Syntax error: If you mean to check out media. Type \"checkout [Article Number]\" without quotation marks and with \"[Article Number]\" replaced with the article number corresponding to the media you want to check out. ";
	private final String DEREGISTER_ERROR = "Syntax error: If you mean to deregister media from library database. Type \"deregister [Article Number]\" without quotation marks and with \"[Article Number]\" replaced with the article number corresponding to the media you want to deregister. ";
	private final String REGISTER_ERROR = "Syntax error: If you mean to register media. Type \"register\" without quotation marks. ";
	private final String INFO_ERROR = "Syntax error: If you mean to view extended information about a media. Type \"info [Article Number]\" without quotation marks and with \"[Article Number]\" replaced with the article number corresponding to the media you want to read more about. ";
	private final String LIST_ERROR = "Syntax error: Type \"list\" without quotation marks if you mean to view library contents. ";
	private final String QUIT_ERROR = "Syntax error: Type \"quit\" without quotation marks if you mean to quit. ";
	private final String HELP_ERROR = "Syntax error: Type help followed by a command to get usage information about the command. ";
	private final String SYNTAX_ERROR = "Syntax error: Give a command followed by only one argument and keep in mind that article numbers can only contain integers. ";
	private final String PROMPT = "> ";
	private final String BAD_ARTICLE_NUMBER = "No media with the given article number can be found in the library. ";
	private final String DOUBLE_REGISTER = "A media with the given article number already exists in the library. ";
	private final String DOUBLE_CHECKOUT = "The media corresponding to the given article number is already checked out. ";
	private final String DOUBLE_CHECKIN = "The media corresponding to the given article number is already checked in. ";
	private final String UNKNOWN_COMMAND = "Java Console Library can't recognise any command in the given input. ";
	private final String QUERY_REGISTER_TYPE = "Would you like to register a book or a movie? ";
	private final String INVALID_INPUT = "Invalid input. ";
	private final String WELCOME = "Hello and welcome to the Java Console Library! If this is the first time using this application you are recommended to type \"help\", this will give you the available commands. ";
	private final String HELP = "These are the available commands: "; // Incomplete, needs to be completed in help()
	private final String HELP_CHECKIN = "The checkin command is used to check in a lended item such as a book or a movie into the library. Checked in items are ready to be lended out again. ";
	private final String HELP_CHECKOUT = "The checkout command is used to check out an item such as a book or a movie from the library. Checked out items can't be lended again until they are returned and checked in. ";
	private final String HELP_INFO = "The info command is used to view extended information about a particular item in the library database. ";
	private final String HELP_REGISTER = "The register command is used to register new items into the library database. ";
	private final String HELP_DEREGISTER = "The deregister command is used to remove an item from the library database. ";
	private final String HELP_QUIT = "The quit command is used to exit the Java Console Library. All library contents are stored until the application is started again. ";
	private final String HELP_LIST = "The list command is used to view a list of all items stored at the library. The list is ordered by article number. ";
	private final String HELP_HELP = "Oh boy, you really need help don't you? ";																// method!

	public LibraryController(Library lib) {
		this.setLib(lib);
		this.libPath = ".\\bin\\application\\Library Contents\\lib.csv";
	}

	public void start() { // start the application and gives welcome message
		setRunning(true);
		System.out.println(WELCOME);
	}

	public void queryUserCommand() { // TODO needs to be split up and made more readable!
		System.out.print(PROMPT);
		CommandInterpreter cmnd = new CommandInterpreter(getInput());
		String argument = cmnd.getArgument();
		if (cmnd.getError() != CommandInterpreter.Error.NO_ERROR) { // if we have error
			handleError(cmnd);
		}else if(cmnd.getCommand() == CommandInterpreter.Command.HELP && argument != null) {
			describeCommand(cmnd.getCommand(), cmnd.getArgument());
		}else if (argument != null && isExistingArticleNumber(Integer.decode(argument))) { // if no error and we have
																							// argument and argument is
																						// valid article number
			if (isLended(Integer.decode(argument)) && cmnd.getCommand() == CommandInterpreter.Command.CHECKOUT) {
				handleDoubleCheckOut();
			} else if (!isLended(Integer.decode(argument)) && cmnd.getCommand() == CommandInterpreter.Command.CHECKIN) {
				handleDoubleCheckIn();
			} else {
				handleCommand(cmnd.getCommand(), Integer.decode(argument)); // should it be possible to deregister a
																			// lended media?
			}
		} else if (argument != null && !isExistingArticleNumber(Integer.decode(argument))) { // if no error and we have
																								// argument and argument
																								// is invalid article
																								// number
				handleBadArticleNumber(cmnd);
			
		} else {
			handleCommand(cmnd.getCommand()); // if we have command without errors and no arguments
		}
	}

	public void init() { // stuff that needs to be done before running
		loadLibarary();
	}

	public void handleDoubleCheckIn() {
		System.out.println(DOUBLE_CHECKIN);
	}

	public void handleDoubleCheckOut() {

		System.out.println(DOUBLE_CHECKOUT);
	}

	public void handleBadArticleNumber(CommandInterpreter cmnd) {
		System.out.println(BAD_ARTICLE_NUMBER);
	}

	public void handleError(CommandInterpreter cmnd) {
		switch (cmnd.getError()) {
		case UNKNOWN_COMMAND:
			System.out.println(this.UNKNOWN_COMMAND); // TODO should probably have separate name from the Error constant
			break;
		case INVALID_ARGUMENT:
			System.out.println(SYNTAX_ERROR);
			break;
		case LIST_ARGUMENT:
			System.out.println(LIST_ERROR);
			break;
		case CHECKOUT_NO_ARGUMENT:
			System.out.println(CHECK_OUT_ERROR);
			break;
		case CHECKIN_NO_ARGUMENT:
			System.out.println(CHECK_IN_ERROR);
			break;
		case REGISTER_ARGUMENT:
			System.out.println(REGISTER_ERROR);
			break;
		case DEREGISTER_NO_ARGUMENT:
			System.out.println(DEREGISTER_ERROR);
			break;
		case INFO_NO_ARGUMENT:
			System.out.print(INFO_ERROR);
			break;
		case QUIT_ARGUMENT:
			System.out.println(QUIT_ERROR);
			break;
		case HELP_ARGUMENT:
			System.out.println(this.HELP_ERROR);

		}
	}

	public boolean isLended(Integer articleNumber) {
		// this.lib.getMedia(articleNumber)
		return false;
	}

	public void describeCommand(CommandInterpreter.Command command, String argument) {
		switch (argument) {
		case "CHECKIN":
			System.out.println(HELP_CHECKIN);
			break;
		case "CHECKOUT":
			System.out.println(HELP_CHECKOUT);
			break;
		case "INFO":
			System.out.println(HELP_INFO);
			break;
		case "DEREGISTER":
			System.out.println(HELP_DEREGISTER);
			break;
		case "REGISTER":
			System.out.println(HELP_REGISTER);
			break;
		case "QUIT":
			System.out.println(HELP_QUIT);
			break;
		case "LIST":
			System.out.println(HELP_LIST);
			break;
		case "HELP":
			System.out.println(HELP_HELP);
			break;
		}
	}

	public void handleCommand(CommandInterpreter.Command command, Integer articleNumber) {
		switch (command) {
		case CHECKIN:
			break;
		case CHECKOUT:
			break;
		case DEREGISTER:
			break;
		case INFO:
			break;

		}
	}

	public void handleCommand(CommandInterpreter.Command command) {
		switch (command) {
		case QUIT:
			setRunning(false);
			break;
		case LIST:
			// print all stored contents in library
			break;
		case REGISTER:
			registerNewMedia();
			break;
		case HELP:
			System.out.println(this.HELP);
			listUserCommands();
			break;
		}
	}

	public void listUserCommands() {
		EnumSet<Command> commands = EnumSet.allOf(Command.class);
		commands.remove(CommandInterpreter.Command.UNKNOWN_COMMAND); // Command.UNKNOWN_COMMAND is not meant to be used
																		// directly by user
		commands.forEach(command -> System.out.println(command));
	}

	public void registerNewMedia() { // TODO might be able to set avoid hardcoding the LendableMedia types
		System.out.println(QUERY_REGISTER_TYPE);
		String input = getInput();
		if (input.equals("BOOK")) {
			registerNewBook();
		} else if (input.equals("MOVIE")) {
			registerNewMovie();
		} else {
			System.out.println(INVALID_INPUT);
			registerNewMedia();
		}
	}

	public void registerNewBook() {
		// TODO Book needs to be implemented first
	}

	public void registerNewMovie() {
		// TODO Movie needs to be implemented first
	}

	public boolean isExistingArticleNumber(Integer articleNumber) { // TODO tmo
		return false;
	}

	public String getInput() {// any formating?
		Scanner scn = new Scanner(System.in);
		String input = scn.nextLine();
		if (input.length() == 0) // no empty strings allowed, no dialogue needed
			return getInput();
		input = input.toUpperCase();
		return input;
	}

	public void saveLibrary() { // tmp implementation until all needed classes are implemented, should throw
								// exception
		try {
			FileWriter writer = new FileWriter(libPath); //
			CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
			printer.printRecord("ArticleNr", "Name", "Value", "Borrower", "Pages/Length", "Publisher/IMDB Rating");
//			for (LendableMedia media : lib.getStoredMedia()) {
//				
//			}
			printer.close();
		} catch (IOException e) {
		}
	}

	public void loadLibarary() { // tmp implementation until all needed classes are implemented, should throw
									// exception, should load to collection, do we make new file or mend old one?
		File csvData = new File(libPath);
		CSVParser parser;
		try {
			parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.RFC4180);
			boolean first = true;
			for (CSVRecord csvRecord : parser) {
				System.out.print(csvRecord.get(0) + " " + csvRecord.get(1) + " " + csvRecord.get(2) + " "
						+ csvRecord.get(3) + " " + csvRecord.get(4) + " ");
				System.out.println(csvRecord.get(5));
			}
		} catch (IOException e) {
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
