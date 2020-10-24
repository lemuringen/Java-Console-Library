package application;

public class LibraryMain {

	public static void main(String[] args) {
		Library lib = new Library();
		LibraryController libController = new LibraryController(lib);
		libController.saveLibrary();
		libController.setRunning(true);;
		while (libController.isRunning()) {
			libController.queryUserCommand();
		}
	}

}
