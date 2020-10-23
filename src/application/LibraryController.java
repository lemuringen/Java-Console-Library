package application;

import java.io.File;
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
	private String libPath; // directory path to our Library storage, might be better to let the library
							// hold this info for itself
	private boolean isRunning;

	public LibraryController(Library lib) {
		this.setLib(lib);
		this.libPath = ".\\bin\\application\\Library Contents\\lib.csv";
	}

	public void queryUserCommand() { // should probably be split into mutliple functions
		System.out.println("> "); // TODO make enums or String constants for dialogue, need to figure out where to check article numbers
		String input = getInput();
		String[] splitInput = {input};
		if(input.contains(" ")) splitInput = input.split("[ ]"); // maybe make better name
		if (input.startsWith(Command.CHECKIN.name())) {
			if (splitInput.length == 2) {
				checkIn();
			} else {
				System.out.println(
						"Syntax error: If you mean to check in media. Type \"checkin [Article Number]\" without quotation marks and with \"[Article Number]\" replaced with the article number corresponding to the media you want to check in.");
				queryUserCommand();
			}
		} else if (input.startsWith(Command.CHECKOUT.name())) {
			if (splitInput.length == 2) {
				checkOut();
			} else {
				System.out.println(
						"Syntax error: If you mean to check out media. Type \"checkout [Article Number]\" without quotation marks and with \"[Article Number]\" replaced with the article number corresponding to the media you want to check out.");
				queryUserCommand();
			}
		} else if (input.startsWith(Command.DEREGISTER.name())) {
			if (splitInput.length == 2) {
				deregister();
			} else {
				System.out.println(
						"Syntax error: If you mean to deregister media from library database. Type \"deregister [Article Number]\" without quotation marks and with \"[Article Number]\" replaced with the article number corresponding to the media you want to deregister.");
				queryUserCommand();
			}
		} else if (input.startsWith(Command.REGISTER.name())) {
			if (input.equals(Command.REGISTER.name())) {
				register();
			}else {
				System.out.println(
						"Syntax error: If you mean to register media. Type \"register\" without quotation marks.");
				queryUserCommand();
			}
		} else if (input.startsWith(Command.INFO.name())) {
			if (splitInput.length == 2) {
				info();
			} else {
				System.out.println(
						"Syntax error: If you mean to view extended information about a media. Type \"checkout [Article Number]\" without quotation marks and with \"[Article Number]\" replaced with the article number corresponding to the media you want to read more about.");
				queryUserCommand();
			}
		} else if (input.startsWith(Command.LIST.name())) {
			if (input.equals(Command.LIST.name())) {
				printContents(); // TODO view all LendableMedia stored at library
			} else {
				System.out.println(
						"Syntax error. Type \"list\" without quotation marks if you mean to view library contents.");
				queryUserCommand();
			}
		} else if (input.startsWith(Command.QUIT.name())) {
			if (input.equals(Command.QUIT.name())) {
				exit();
			} else {
				System.out.println("Syntax error. Type \"quit\" without quotation marks if you mean to quit.");
				queryUserCommand();
			}
		}else { // TODO check for unknown commands
			System.out.print("Syntax error");
			queryUserCommand();
		}
	}

	private void exit() { // exit application
		this.isRunning = false;
	}

	public void checkIn() { // checkin a lended book back to the library
		
	}

	public void checkOut() { //checkout a book to a new lender(person object)

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
