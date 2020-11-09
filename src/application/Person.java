package application;

import java.util.ArrayList;

import application.media.MediaCopy;

/**
 *Data container for borrowers of items stored in Library 
 */
public class Person {
	private String name;
	private String phoneNr;
	private ArrayList<MediaCopy> borrowedCopy; // is this really best way?
	
	public Person(String name, String phoneNr) {
		this.setName(name);
		this.setPhoneNr(phoneNr);
		this.setBorrowedCopy(new ArrayList<MediaCopy>());
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
	
	public ArrayList<MediaCopy> getBorrowedCopy() {
		return borrowedCopy;
	}
	private void setBorrowedCopy(ArrayList<MediaCopy> borrowedCopy) {
		this.borrowedCopy = borrowedCopy;
	}
	public void addCopy(MediaCopy copy) {
		getBorrowedCopy().add(copy);
	}
	public boolean isBorrowing() {
		if(getBorrowedCopy().size() > 0) {
			return true;
		}else {
			return false;
		}
	}
	public void removeCopy(MediaCopy copy) {
		getBorrowedCopy().remove(copy);
	}
	@Override
	public String toString() {
		return getName() + ", " + getPhoneNr();
	}
}
