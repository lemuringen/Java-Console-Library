package application;

public enum Command { // Commands available to user excl. UNKOWN_COMMAND that is solely meant for internal purposes
	LIST("LIST"), CHECKOUT("CHECKOUT"), CHECKIN("CHECKIN"), REGISTER("REGISTER"), DEREGISTER("DEREGISTER"),
	INFO("INFO"), QUIT("QUIT"), HELP("HELP"), UNKNOWN_COMMAND("UNKNOWN COMMAND"), LOANS("LOANS");

	public String name;

	Command(String name) {
		this.name = name;
	}
}