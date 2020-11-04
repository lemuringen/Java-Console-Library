package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class LibraryStorageManager {
	private String path;
	private String fileName;
	private Library lib;

	public LibraryStorageManager(String path, String fileName, Library lib) {
		this.path = path;
		this.fileName = fileName;
		this.lib = lib;
	}
//
//	public void init() {
//		File contents = new File(path + "/" + fileName);
//
//		try {
//			// TODO should be possible to make more effective
//			makeStorageFolder();
//			if (!contents.exists() | contents.isDirectory()) {
//				saveLibrary();
//			}
//			loadLibrary();
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.out.println("Unknown error. Shutting down. "); // TODO
//			System.exit(1);
//		}
//	}

	public void makeStorageFolder() {
		if (!Files.isDirectory(Paths.get(this.path))) {
			new File(this.path).mkdirs();
		}
	}

//	public void saveLibrary() throws IOException {
//		FileWriter writer = new FileWriter(path + "/" + fileName); //
//		CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
//		printer.printRecord("Type", "ArticleNr", "Title", "Value", "Pages/Length", // TODO
//				"Author/IMDB Rating", "Borrower", "Phonenumber");
//		for (LendableMedia media : lib.getStoredMedia()) {
//			if (media instanceof Book) {
//				saveBookToCSV(printer, (Book) media);
//			} else if (media instanceof Movie) {
//				saveMovieToCSV(printer, (Movie) media);
//			}
//		}
//		printer.close();
//
//	}

//	private void saveBookToCSV(CSVPrinter printer, Book book) throws IOException {
//		if (book.isBorrowed()) {
//			printer.printRecord("BOOK", book.getArticleNr(), book.getTitle(), book.getValue(), book.getPages(),
//					book.getAuthor(), book.getBorrower().getName(), book.getBorrower().getPhoneNr(), book.getDueDate().getTime());
//		} else {
//			printer.printRecord("BOOK", book.getArticleNr(), book.getTitle(), book.getValue(), book.getPages(),
//					book.getAuthor());
//		}
//	}
//
//	private void saveMovieToCSV(CSVPrinter printer, Movie movie) throws IOException {
//		if (movie.isBorrowed()) {
//			printer.printRecord("MOVIE", movie.getArticleNr(), movie.getTitle(), movie.getValue(), movie.getLength(),
//					movie.getIMDBRating(), movie.getBorrower().getName(), movie.getBorrower().getPhoneNr(), movie.getDueDate().getTime());
//		} else {
//			printer.printRecord("MOVIE", movie.getArticleNr(), movie.getTitle(), movie.getValue(), movie.getLength(),
//					movie.getIMDBRating());
//		}
//	}
/**
 * Parses the file reached by the path and filename instance variables of this class. When finding records of Book or Movie type it directs to loadBookToLibrary respectively loadMovieToLibrary.
 * @throws IOException
 */
//	public void loadLibrary() throws IOException { 
//		//do we make new file or mend old one?
//		File csvData = new File(path + "/" + fileName);
//		CSVParser parser;
//		try {
//			parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.RFC4180);
//			for (CSVRecord csvRecord : parser) {
//				if (csvRecord.get(0).equals("BOOK")) {
//					loadBookToLibrary(csvRecord);
//
//				} else if (csvRecord.get(0).equals("MOVIE")) {
//					loadMovieToLibrary(csvRecord);
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	/**
	 * Loads a csv record, assumed to contain information about a Book object, into a list in object Library.
	 * @param bookRecord
	 * @throws IOException
	 */
//	private void loadBookToLibrary(CSVRecord bookRecord) throws IOException {
//		if (bookRecord.size() == 9) {
//			lib.addLendableMedia(new Book(bookRecord.get(1),
//					bookRecord.get(2),
//					Integer.valueOf(bookRecord.get(3)),
//					Integer.valueOf(bookRecord.get(4)),
//					bookRecord.get(5),
//					new Person(bookRecord.get(6), bookRecord.get(7)),
//					new Date(Long.valueOf(bookRecord.get(8)))));
//		} else if (bookRecord.size() == 6) {
//			lib.addLendableMedia(new Book(bookRecord.get(1),
//					bookRecord.get(2),
//					Integer.valueOf(bookRecord.get(3)),
//					Integer.valueOf(bookRecord.get(4)),
//					bookRecord.get(5)));
//		}
//	}
/**
 * Loads a csv record, assumed to contain information about a Movie object, into a list in object Library.
 * @param movieRecord
 * @throws IOException
 */
//	private void loadMovieToLibrary(CSVRecord movieRecord) throws IOException {
//		if (movieRecord.size() == 9) {
//			lib.addLendableMedia(new Movie(movieRecord.get(1),
//					movieRecord.get(2),
//					Integer.valueOf(movieRecord.get(3)),
//					Integer.valueOf(movieRecord.get(4)),
//					Double.valueOf(movieRecord.get(5)),
//					new Person(movieRecord.get(6), movieRecord.get(7)), 
//					new Date(Long.valueOf(movieRecord.get(8)))));
//		} else if (movieRecord.size() == 6) {
//			lib.addLendableMedia(new Movie(movieRecord.get(1),
//					movieRecord.get(2),
//					Integer.valueOf(movieRecord.get(3)),
//					Integer.valueOf(movieRecord.get(4)),
//					Double.valueOf(movieRecord.get(5))));
//		}
//	}
}
