package application;

import java.util.regex.Pattern;

/**
 * Extracts commands from strings and checks for general errors pertaining to
 * the extracted command. Does no compare command arguments with library
 * database. Might serve best to make this class fully static depending on how
 * it will be used in the end.
 */
public class CommandInterpreter {
	public enum Command { // Commands available to user
		LIST, CHECKOUT, CHECKIN, REGISTER, DEREGISTER, INFO, QUIT, UNKNOWN_COMMAND
	}

	public enum Error { // General errors that might emerge from using the commands
		// TODO SYNTAX_ERROR is never used!
		SYNTAX_ERROR, UNKNOWN_COMMAND, INVALID_ARGUMENT, LIST_ARGUMENT, CHECKOUT_NO_ARGUMENT, CHECKIN_NO_ARGUMENT,
		REGISTER_ARGUMENT, DEREGISTER_NO_ARGUMENT, INFO_NO_ARGUMENT, QUIT_ARGUMENT, NO_ERROR
	}

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
	}

	public void processCommand() {
		// TODO need to figure out where to check article numbers
		if (input.contains(Command.CHECKIN.name())) {
			checkIn();
		} else if (input.contains(Command.CHECKOUT.name())) {
			checkOut();
		} else if (input.contains(Command.DEREGISTER.name())) {
			deregister();
		} else if (input.contains(Command.REGISTER.name())) {
			register();
		} else if (input.contains(Command.INFO.name())) {
			info();
		} else if (input.contains(Command.LIST.name())) {
			list();
		} else if (input.contains(Command.QUIT.name())) {
			quit();
		} else { // TODO check for unknown commands
			unknownCommand();
		}
	}

	private boolean isNumber(String number) {
		if (number.length() == 0)
			return false; // without this check empty strings [""] will return true
		return Pattern.matches("\\d{" + number.length() + "}", number);
	}

	public void unknownCommand() { // does not contain any properly spelled commands
		this.setCommand(Command.UNKNOWN_COMMAND); 
		this.setError(Error.UNKNOWN_COMMAND);
	}

	public void quit() { // exit application
		this.setCommand(Command.QUIT);
		if (input.equals(Command.QUIT.name())) { // no errors
			this.setError(Error.NO_ERROR);
		} else if (splitInput.length > 1) {
			this.setError(Error.QUIT_ARGUMENT); // should not have arguments
		}
	}

	public void checkIn() { // checkin a lended book back to the library
		this.setCommand(Command.CHECKIN);
		if (splitInput.length == 2 && splitInput[0].equals(Command.CHECKIN.name()) && isNumber(splitInput[1])) { // no
																													// errors

			this.setError(Error.NO_ERROR);
		} else if (splitInput.length == 1) { // no argument or improper placement of argument

			this.setError(Error.CHECKIN_NO_ARGUMENT);
		} else if (splitInput.length > 2 || (splitInput.length == 2 && !isNumber(splitInput[1]))) { // too many
																									// arguments or non
																									// digits used in
																									// argument

			this.setError(Error.INVALID_ARGUMENT);
		}
	}

	public void checkOut() { // checkout a book to a new lender(person object)
		this.setCommand(Command.CHECKOUT);
		if (splitInput.length == 2 && splitInput[0].equals(Command.CHECKOUT.name()) && isNumber(splitInput[1])) {
			this.setError(Error.NO_ERROR);
		} else if (splitInput.length == 1) { // no argument or improper placement of argument
			this.setError(Error.CHECKOUT_NO_ARGUMENT);
		} else if (splitInput.length > 2 || (splitInput.length == 2 && !isNumber(splitInput[1]))) { // too many
																									// arguments or non
																									// digits used in
																									// argument
			this.setError(Error.INVALID_ARGUMENT);
		}
	}

	public void list() { // view a list of all the contents stored at the library
		this.setCommand(Command.LIST);
		if (input.equals(Command.LIST.name())) {
			this.setError(Error.NO_ERROR);
		} else if (splitInput.length > 1) {
			this.setError(Error.LIST_ARGUMENT); // should not have arguments
		}
	}

	public void deregister() { // remove media from library
		this.setCommand(Command.DEREGISTER);
		if (splitInput.length == 2 && splitInput[0].equals(Command.DEREGISTER.name()) && isNumber(splitInput[1])) {

			this.setError(Error.NO_ERROR);
		} else if (splitInput.length == 1) { // no argument or improper placement of argument
			this.setError(Error.DEREGISTER_NO_ARGUMENT);
		} else if (splitInput.length > 2 || (splitInput.length == 2 && !isNumber(splitInput[1]))) { // too many
																									// arguments or non
																									// digits used in
																									// argument
			this.setError(Error.INVALID_ARGUMENT);
		}
	}

	public void register() { // add a media to library
		this.setCommand(Command.REGISTER);
		if (input.equals(Command.REGISTER.name())) {
			this.setError(Error.NO_ERROR);
		} else if (splitInput.length > 1) {
			this.setError(Error.REGISTER_ARGUMENT); // should not have arguments
		}
	}

	public void info() { // get in depth info about a media
		this.setCommand(Command.INFO);
		if (splitInput.length == 2 && splitInput[0].equals(Command.INFO.name()) && isNumber(splitInput[1])) {
			this.setError(Error.NO_ERROR);
		} else if (splitInput.length == 1) { // no argument or improper placement of argument
			this.setError(Error.INFO_NO_ARGUMENT);
		} else if (splitInput.length > 2 || (splitInput.length == 2 && !isNumber(splitInput[1]))) { // too many
																									// arguments or non
																									// digits used in
																									// argument
			this.setError(Error.INVALID_ARGUMENT);
		}
	}
	public String getArgument() {
		if(splitInput.length == 2) {
			return splitInput[1];
		}else {
			return null;
		}
		
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
