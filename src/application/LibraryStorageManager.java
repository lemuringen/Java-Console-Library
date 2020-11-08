package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;

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

	/**
	 * If no folder with the path specified in the path instance variable exists,
	 * init() makes one. It then makes a csv file to store data in if one does not
	 * already exists.
	 */
	public void init() {
		File contents = new File(path + "/" + fileName);
		try {
			makeStorageFolder();
			if (!contents.exists() | contents.isDirectory()) {
				storeLibrary();
			}
			loadLibrary();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(
					"Unexpected I/O exception. Try deleting the library folder and see to it that Java Console Library is installed in a place it has read/write permissions in. Shutting down... "); // TODO
			System.exit(1);
		}
	}

	/**
	 * Creates a storage folder if one does not already exists otherwise does
	 * nothing.
	 */
	private void makeStorageFolder() {
		if (!Files.isDirectory(Paths.get(this.path))) {
			new File(this.path).mkdirs();
		}
	}

	/**
	 * Saves the LendableMedia objects stored in the Library object to a .csv file.
	 * 
	 * @throws IOException
	 */
	public void storeLibrary() throws IOException {
		FileWriter writer = new FileWriter(path + "/" + fileName); //
		CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
		printer.printRecord("Type", "ArticleNumber", "Title", "Value", "Pages/Length", // TODO
				"Author/IMDB Rating");
		for (LendableMedia media : lib.getStoredMedia()) {
			if (media instanceof Book) {
				saveBookToCSV(printer, (Book) media);
			} else if (media instanceof Movie) {
				saveMovieToCSV(printer, (Movie) media);
			}
		}
		printer.printRecord("End of records"); // TODO
		printer.close();

	}

	private void saveBookToCSV(CSVPrinter printer, Book book) throws IOException {
		// saves book
		printer.printRecord("BOOK", book.getArticleNr(), book.getTitle(), book.getValue(), book.getPages(),
				book.getAuthor());
		if (book.getCopies().size() > 0) {
			// saves the individual copies of the book
			for (MediaCopy copy : book.getCopies().values()) {
				// is the book borrowed? then we print Person and Date object to .csv
				if (copy.isBorrowed()) {
					printer.printRecord("COPY", copy.getSerialNumber(), copy.getBorrower().getName(),
							copy.getBorrower().getPhoneNr(), copy.getDueDate().getTime());
					// else we print NOT BORROWED instead
				} else {
					printer.printRecord("COPY", copy.getSerialNumber(), "NOT BORROWED");
				}
			}
		}
	}

//follows exact same structure as saveBookToCSV
	private void saveMovieToCSV(CSVPrinter printer, Movie movie) throws IOException {
		printer.printRecord("MOVIE", movie.getArticleNr(), movie.getTitle(), movie.getValue(), movie.getLength(),
				movie.getIMDBRating());
		if (movie.getCopies().size() > 0) {
			for (MediaCopy copy : movie.getCopies().values()) {
				if (copy.isBorrowed()) {
					printer.printRecord("COPY", copy.getSerialNumber(), copy.getBorrower().getName(),
							copy.getBorrower().getPhoneNr(), copy.getDueDate().getTime());
				} else {
					printer.printRecord("COPY", copy.getSerialNumber(), "NOT BORROWED");
				}
			}
		}
	}

	/**
	 * Parses the .csv file reached by the path and filename instance variables of
	 * this class looking for records of Books and Movies. It then instantiates
	 * objects accordingly and saves them to a Library object.
	 */
	public void loadLibrary() throws IOException {
		File csvData = new File(path + "/" + fileName);
		CSVParser parser;
		parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.RFC4180);
		// we make a list of the records and makes for loop with same span as the amount
		// of records
		List<CSVRecord> records = parser.getRecords();
		for (int i = 0; i < records.size(); i++) {
			// instantiate Book if we find records started with BOOK
			if (records.get(i).get(0).equals("BOOK")) {
				Book book = getBookFromRecord(records.get(i));
				lib.addLendableMedia(book);
				// we then check for individual copies of the book expected to be stored
				// consecutively after
				while (records.get(i + 1).get(0).equals("COPY")) {
					// advances the encompassing for-loop as well as these indexes wont be
					// interesting outside this while-loop
					i++;
					MediaCopy copy = getMediaCopyFromRecord(records.get(i));
					book.addNewCopy(copy);
					if (copy.isBorrowed()) { // if borrowed register copy to borrower and borrower to library
						Person borrower = copy.getBorrower();
						borrower.addCopy(copy);
						lib.getBorrowers().put(borrower.getName(), borrower);
					}
				}
				// same flow as for book in the if before here
			} else if (records.get(i).get(0).equals("MOVIE")) {
				Movie movie = getMovieFromRecord(records.get(i));
				lib.addLendableMedia(movie);
				while (records.get(i + 1).get(0).equals("COPY")) {
					i++;
					MediaCopy copy = getMediaCopyFromRecord(records.get(i));
					movie.addNewCopy(copy);
					if (copy.isBorrowed()) { // if borrowed register copy to borrower and borrower to library
						Person borrower = copy.getBorrower();
						borrower.addCopy(copy);
						lib.getBorrowers().put(borrower.getName(), borrower);
					}
				}
			}
		}

	}

	/**
	 * 
	 * @param copyRecord
	 * @return Copies (as in unique examples not data copies) of LendableMedias. The
	 *         copies contain serialnumber, and potential Borrower, borrowers
	 *         phonenumber
	 */
	private MediaCopy getMediaCopyFromRecord(CSVRecord copyRecord) {
		if (copyRecord.get(2).equals("NOT BORROWED")) { // new to be checked first
			return new MediaCopy(Integer.valueOf(copyRecord.get(1)));
		} else {
			int serialNumber = Integer.valueOf(copyRecord.get(1));
			String name = copyRecord.get(2);
			String phonenumber = copyRecord.get(3);
			Person person;
			if (lib.getBorrowers().containsKey(name)) {
				person = lib.getBorrowers().get(name);
			} else {
				person = new Person(name, phonenumber);
			}
			Date dueDate = new Date(Long.valueOf(copyRecord.get(4)));
			return new MediaCopy(serialNumber, person, dueDate);
		}
	}

	private Book getBookFromRecord(CSVRecord bookRecord) {
		Book book = Book.getBook(bookRecord.get(1), lib);
		book.setUpBook(bookRecord.get(2), Integer.valueOf(bookRecord.get(3)), Integer.valueOf(bookRecord.get(4)),
				bookRecord.get(5));
		return book;
	}

	private Movie getMovieFromRecord(CSVRecord movieRecord) {
		Movie movie = Movie.getMovie(movieRecord.get(1), lib);
		movie.setUpMovie(movieRecord.get(2), Integer.valueOf(movieRecord.get(3)), Integer.valueOf(movieRecord.get(4)),
				Double.valueOf(movieRecord.get(5)));
		return movie;
	}

}
