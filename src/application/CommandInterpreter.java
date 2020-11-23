package application;

/**
 * Extracts commands from strings and checks for general errors pertaining to
 * the extracted command. Does not compare command arguments with library
 * database. Might serve best to make this class fully static depending on how
 * it will be used in the end.
 */
public class CommandInterpreter {

	private final String input; // user input goes here
	private String[] splitInput; // user input split by space symbol
	private Command command;
	private Error error;

	public CommandInterpreter(String input) {
		this.input = input;
		this.setCommand(null);
		this.setError(null);
		String[] splitInput = { input }; // TODO ugly workaround
		this.splitInput = splitInput;
		if (input.contains(" "))
			this.splitInput = input.split("[ ]");
		processCommand();
	}

	public void processCommand() {
		// dependent on check order, help needs to be first as it can be combined with
		if (splitInput[0].matches("\\b" + Command.HELP.name + "\\b")) {
			help();
		} else if (splitInput[0].matches("\\b" + Command.CHECKOUT.name + "\\b")) { 
			checkOut();
		} else if (splitInput[0].matches("\\b" + Command.DEREGISTER.name + "\\b")) {		
			deregister();
		} else if (splitInput[0].matches("\\b" + Command.REGISTER.name + "\\b")) {
			register();
		} else if (splitInput[0].matches("\\b" + Command.INFO.name + "\\b")) {
			info();
		} else if (splitInput[0].matches("\\b" + Command.LIST.name + "\\b")) {
			list();
		} else if (splitInput[0].matches("\\b" + Command.QUIT.name + "\\b")) {
			quit();
		} else if (splitInput[0].matches("\\b" + Command.CHECKIN.name + "\\b")) {
			checkIn();
		} else if(splitInput[0].matches("\\b" + Command.LOANS.name + "\\b")){
			loans();
		}else {
			unknownCommand();
		}
	}
	public void loans() {
		this.setCommand(Command.LOANS);
		if (input.equals(Command.LOANS.name)) { // no errors
			this.setError(Error.NO_ERROR);
		} else if(splitInput.length > 1 && (CommunicationsUtility.onlyLetters(getArgument()))){
			this.setError(Error.NO_ERROR);
		}else if(splitInput.length == 2 && splitInput[1].equalsIgnoreCase("-expired")) {
			this.setError(Error.NO_ERROR);
		}
		else{this.setError(Error.LOANS_ARGUMENT); //bad arguments
		}
	}

	public void unknownCommand() { // does not contain any properly spelled commands
		this.setCommand(Command.UNKNOWN_COMMAND);
		this.setError(Error.UNKNOWN_COMMAND);
	}

	public void help() { // works both without and with arguments
		this.setCommand(Command.HELP);
		if (input.equalsIgnoreCase(Command.HELP.name)) { // no errors
			this.setError(Error.NO_ERROR);
		} else if (splitInput.length == 2 && splitInput[0].equals(Command.HELP.name) && isUsableCommand(splitInput[1])) {
			this.setError(Error.NO_ERROR);
		} 
		else if (splitInput.length > 2 || (splitInput.length == 2 && !isUsableCommand(splitInput[1]))) {
			this.setError(Error.HELP_ARGUMENT);
			 // TODO allow commands as argument?
		}else {
			this.setError(Error.HELP_ARGUMENT);
		}
	}

	public void quit() { // exit application
		this.setCommand(Command.QUIT);
		if (input.equals(Command.QUIT.name)) { // no errors
			this.setError(Error.NO_ERROR);
		} else{
			this.setError(Error.QUIT_ARGUMENT); // should not have arguments
		}
	}

	public void checkIn() { // checkin a lended book back to the library
		this.setCommand(Command.CHECKIN);
		if (splitInput.length == 2 && splitInput[0].equals(Command.CHECKIN.name) && CommunicationsUtility.isNumber(splitInput[1])) { // no
																													// errors

			this.setError(Error.NO_ERROR);
		} else if (splitInput.length == 1) { // no argument or improper placement of argument

			this.setError(Error.CHECKIN_NO_ARGUMENT);
		} else if (splitInput.length > 2 || (splitInput.length == 2 && !CommunicationsUtility.isNumber(splitInput[1]))) { // too many
																									// arguments or non
																									// digits used in
																									// argument

			this.setError(Error.INVALID_ARGUMENT);
		} else { // TODO special case needs better error message: ascheckinf 0001
			this.setError(Error.CHECKIN_NO_ARGUMENT);
		}
	}

	public void checkOut() { // checkout a book to a new lender(person object)
		this.setCommand(Command.CHECKOUT);
		if (splitInput.length == 2 && splitInput[0].equals(Command.CHECKOUT.name) && CommunicationsUtility.isNumber(splitInput[1])) {
			this.setError(Error.NO_ERROR);
		} else if (splitInput.length == 1) { // no argument or improper placement of argument
			this.setError(Error.CHECKOUT_NO_ARGUMENT);
		} else if (splitInput.length > 2 || (splitInput.length == 2 && !CommunicationsUtility.isNumber(splitInput[1]))) { // too many
																									// arguments or non
																									// digits used in
																									// argument
			this.setError(Error.INVALID_ARGUMENT);
		} else { // TODO special case needs better error message: ascheckinf 0001
			this.setError(Error.CHECKOUT_NO_ARGUMENT);
		}
	}

	public void list() { // view a list of all the contents stored at the library TODO give filters as argument? IE: list borrowed/list stocked
		this.setCommand(Command.LIST); 
		if (input.equals(Command.LIST.name())) {
			this.setError(Error.NO_ERROR);
		} else {
			this.setError(Error.LIST_ARGUMENT); // should not have arguments
		}
	}

	public void deregister() { // remove media from library
		this.setCommand(Command.DEREGISTER);
		if (splitInput.length == 2 && splitInput[0].equals(Command.DEREGISTER.name) && CommunicationsUtility.isNumber(splitInput[1])) {

			this.setError(Error.NO_ERROR);
		} else if (splitInput.length == 1) { // no argument or improper placement of argument
			this.setError(Error.DEREGISTER_NO_ARGUMENT);
		} else if (splitInput.length > 2 || (splitInput.length == 2 && !CommunicationsUtility.isNumber(splitInput[1]))) { // too many
																									// arguments or non
																									// digits used in
																									// argument
			this.setError(Error.INVALID_ARGUMENT);
		} else { // TODO special case needs better error message: ascheckinf 0001
			this.setError(Error.DEREGISTER_NO_ARGUMENT);
		}
	}

	public void register() { // add a media to library
		this.setCommand(Command.REGISTER);
		if (input.equals(Command.REGISTER.name)) {
			this.setError(Error.NO_ERROR);
		} else {
			this.setError(Error.REGISTER_ARGUMENT); // should not have arguments
		}
	}

	public void info() { // get in depth info about a media
		this.setCommand(Command.INFO);
		if (splitInput.length == 2 && splitInput[0].equals(Command.INFO.name) && CommunicationsUtility.isNumber(splitInput[1])) {
			this.setError(Error.NO_ERROR);
		} else if (splitInput.length == 1) { // no argument or improper placement of argument
			this.setError(Error.INFO_NO_ARGUMENT);
		} else if (splitInput.length > 2 || (splitInput.length == 2 && !CommunicationsUtility.isNumber(splitInput[1]))) { // too many
																									// arguments or non
																									// digits used in
																									// argument
			this.setError(Error.INVALID_ARGUMENT);
		} else { // TODO special case needs better error message: ascheckinf 0001
			this.setError(Error.INFO_NO_ARGUMENT);
		}
	}

	private boolean isUsableCommand(String argument) {
		Command[] commands = Command.values();
		if (argument.equals(Command.UNKNOWN_COMMAND.name)) { // UNKNOWN_COMMAND is not meant to be used by user
			return false;
		}
		for (Command command : commands) {
			if (argument.equals(command.name())) { // if we can match argument with any of the Command enumerations we
													// return true
				return true;
			}
		}
		return false; // no matches
	}

/**
 * Crops the first word (expected to be a command) from instance variable input 
 * (expected to be user direction to the program), as such only the argument
 * should be left. It is not checked if argument is faulty. Argument can have
 * whitespaces as they may be needed for names.
 * @return Returns a string assumed (but not checked) to be an argument in user input.
 */
	public String getArgument() { 
		if (splitInput.length > 1) {
			return  this.input.substring(splitInput[0].length()+1);
		} else {
			return null;
		}
	}
	public String getUnrecognizedCommand() {
		return this.splitInput[0];
	}
	public Command getCommand() {
		return command;
	}

	private void setCommand(Command command) {
		this.command = command;
	}

	public Error getError() {
		return error;
	}

	private void setError(Error error) {
		this.error = error;
	}

}
