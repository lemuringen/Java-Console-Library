package application;

import java.io.File;
import java.util.regex.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
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
	private final String INFO_ERROR = "Syntax error: If you mean to view extended information about a media. Type \"checkout [Article Number]\" without quotation marks and with \"[Article Number]\" replaced with the article number corresponding to the media you want to read more about. ";
	private final String LIST_ERROR = "Syntax error. Type \"list\" without quotation marks if you mean to view library contents. ";
	private final String QUIT_ERROR = "Syntax error. Type \"quit\" without quotation marks if you mean to quit. ";
	private final String PROMPT = "> ";

	public LibraryController(Library lib) {
		this.setLib(lib);
		this.libPath = ".\\bin\\application\\Library Contents\\lib.csv";
	}

	public void queryUserCommand() {
		System.out.println(PROMPT);
		CommandInterpreter cmnd = new CommandInterpreter(getInput());
		cmnd.processCommand();
		String argument = cmnd.getArgument();
		if (cmnd.getError() != CommandInterpreter.Error.NO_ERROR) { // if we have error
			handleError(cmnd);
		} else if (argument != null && isExistingArticleNumber(Integer.decode(argument))) { // if no error and we have
																							// argument and argument is
																							// valid article number
			handleCommand(cmnd.getCommand(), Integer.decode(argument)); // TODO needs to make individual checks for many commands ie: islended, exists
		} else if (argument != null && !isExistingArticleNumber(Integer.decode(argument))) { // if no error and we have
																								// argument and argument
																								// is invalid article
																								// number
			handleBadArticleNumber(cmnd);
		} else {
			handleCommand(cmnd.getCommand()); // if we have command without errors and no arguments
		}
	}
	public void init() { //stuff that needs to be done before running
		loadLibarary();
	}

	public void handleError(CommandInterpreter cmnd) {

	}

	public void handleCommand(CommandInterpreter.Command command, Integer articleNumber) {
		switch(command) {
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
		switch(command) {
		case QUIT:
			setRunning(false);
			break;
		case LIST:
			break;
		case REGISTER:
			break;
		}
	}

	public boolean isExistingArticleNumber(Integer articleNumber) { // TODO tmo
		return false;
	}

	public void handleBadArticleNumber(CommandInterpreter cmnd) {

	}

	public String getInput() {// any formating?
		Scanner scn = new Scanner(System.in);
		String input = scn.nextLine();
		if (input.length() == 0)
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
