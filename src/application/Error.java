package application;

public enum Error { // General errors that might emerge from using the commands
	// TODO should be more obvious which Errors that are syntax errors
	UNKNOWN_COMMAND, INVALID_ARGUMENT, LIST_ARGUMENT, CHECKOUT_NO_ARGUMENT, CHECKIN_NO_ARGUMENT, REGISTER_ARGUMENT,
	DEREGISTER_NO_ARGUMENT, INFO_NO_ARGUMENT, QUIT_ARGUMENT, NO_ERROR, HELP_ARGUMENT, LOANS_ARGUMENT
}