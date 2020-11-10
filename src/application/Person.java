package application;


/**
 *Data container for borrowers of items stored in Library 
 */
public class Person {
	private String name;
	private String phoneNr;

	public Person(String name, String phoneNr) {
		this.setName(name);
		this.setPhoneNr(phoneNr);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNr() {
		return phoneNr;
	}
	public void setPhoneNr(String phoneNr) {
		this.phoneNr = phoneNr;
	}
	@Override
	public String toString() {
		return getName() + ", " + getPhoneNr();
	}
}
