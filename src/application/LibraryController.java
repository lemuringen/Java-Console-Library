package application;

import java.io.IOException;

public class LibraryController {
	// Contains the library working memory
	public static Library lib;
	private boolean isRunning;
	// handles everything related to the csv storage file
	private LibraryStorageManager storageManager;
	private DialogueManager dialogueManager;

	public LibraryController(Library lib) {
		this.setLib(lib);
		this.storageManager = new LibraryStorageManager("./bin/application/Library", "contents.csv", lib);
		this.dialogueManager = new DialogueManager();
	}

	public void init() { // stuff that needs to be done before running
		storageManager.init();
	}

	public void start() { // start the application and gives welcome message
		setRunning(true);
		dialogueManager.welcome();
		run();
	}
	public void run() {
		dialogueManager.queryUserAction();		
		if(dialogueManager.isPendingStorageUpdate()) {
			updateStorage();
		}
		if(dialogueManager.isPendingExit()) {
			this.isRunning = false;
		}
		if(this.isRunning) {
			run();
		}
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
		return LibraryController.lib;
	}

	private void setLib(Library lib) {
		LibraryController.lib = lib;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
}
