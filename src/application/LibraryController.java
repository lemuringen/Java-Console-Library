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

	public void queryUserCommand() { // should probably be split into mutliple functions
		System.out.println(PROMPT); // TODO need to figure out where to check article numbers
		String input = getInput();
		String[] splitInput = { input };
		if (input.contains(" "))
			splitInput = input.split("[ ]"); //separate command from argument if two words (not all commands uses arguments)
		
		if (input.contains(Command.CHECKIN.name())) {
			if (splitInput.length == 2 && splitInput[0].equals(Command.CHECKIN.name()) && isNumber(splitInput[1])) {
				checkIn();
			} else {
				System.out.println(CHECK_IN_ERROR);
				queryUserCommand();
			}
		} else if (input.contains(Command.CHECKOUT.name())) {
			if (splitInput.length == 2 && splitInput[0].equals(Command.CHECKOUT.name()) && isNumber(splitInput[1])) {
				checkOut();
			} else {
				System.out.println(CHECK_OUT_ERROR);
				queryUserCommand();
			}
		} else if (input.contains(Command.DEREGISTER.name())) {
			if (splitInput.length == 2 && splitInput[0].equals(Command.DEREGISTER.name()) && isNumber(splitInput[1])) {
				deregister();
			} else {
				System.out.println(DEREGISTER_ERROR);
				queryUserCommand();
			}
		} else if (input.contains(Command.REGISTER.name())) {
			if (input.equals(Command.REGISTER.name())) {
				register();
			} else {
				System.out.println(REGISTER_ERROR);
				queryUserCommand();
			}
		} else if (input.contains(Command.INFO.name())) {
			if (splitInput.length == 2 && splitInput[0].equals(Command.INFO.name()) && isNumber(splitInput[1])) {
				info();
			} else {
				System.out.println(INFO_ERROR);
				queryUserCommand();
			}
		} else if (input.contains(Command.LIST.name())) {
			if (input.equals(Command.LIST.name())) {
				printContents(); // TODO view all LendableMedia stored at library
			} else {
				System.out.println(LIST_ERROR);
				queryUserCommand();
			}
		} else if (input.contains(Command.QUIT.name())) {
			if (input.equals(Command.QUIT.name())) {
				exit();
			} else {
				System.out.println(QUIT_ERROR);
				queryUserCommand();
			}
		} else { // TODO check for unknown commands
			System.out.print("Syntax error");
			queryUserCommand();
		}
	}
	private boolean isNumber(String number) {
		if(number.length() == 0) return false; // without this check empty strings [""] will return true
		return Pattern.matches("\\d{" + number.length() + "}", number);
	}

	public void exit() { // exit application
		this.isRunning = false;
	}

	public void checkIn() { // checkin a lended book back to the library

	}

	public void checkOut() { // checkout a book to a new lender(person object)

	}

	public void printContents() { // view a list of all the contents stored at the library

	}

	public void deregister() { // remove media from library

	}

	public void register() { // add a media to library

	}

	public void info() { // get in depth info about a media

	}

	public String getInput() {// any formating?
		Scanner scn = new Scanner(System.in);
		String input = scn.nextLine();
//		scn.close();
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
}
