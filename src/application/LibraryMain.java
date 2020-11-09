package application;


public class LibraryMain {

	public static void main(String[] args) {
		Library lib = new Library();
		LibraryController libController = new LibraryController(lib);
		libController.init();
		libController.start();
		while (libController.isRunning()) {
			libController.queryUserAction();
		}
	}
}