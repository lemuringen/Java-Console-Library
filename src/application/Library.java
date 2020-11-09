package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import application.media.LendableMedia;
import application.media.MediaCopy;

public class Library {
	private ArrayList<LendableMedia> storedMedia; // bad name maybe, even borrowed media is included
	private HashMap<String, LendableMedia> storedMediaHash;
	private HashMap<String, Person> borrowers;
	private boolean isSorted;
	public Library() {
		this.setStoredMedia(new ArrayList<LendableMedia>());
		this.storedMediaHash = new HashMap<String, LendableMedia>();
		this.setBorrowers(new HashMap<String, Person>());
		this.isSorted = false;
	}
	//TODO inconsistent to do this here
	public void registerBorrower(Person borrower) {
		getBorrowers().put(borrower.getName(), borrower);
	}
	private void setStoredMedia(ArrayList<LendableMedia> storedMedia) {
		this.storedMedia = storedMedia;
		this.isSorted = false;
	}
	public void addLendableMedia(LendableMedia media) {
		storedMedia.add(media);
		storedMediaHash.put(media.getArticleNr(), media);
		this.isSorted = false;
	}
	public void sortMedia() {
		if(!isSorted) {
		storedMedia.sort(new CompareArticleNumber());
		this.isSorted = true;
		}
	}
	public void removeMedia(String articleNumber) {
		LendableMedia media = getMedia(articleNumber);
		storedMediaHash.remove(articleNumber);
		storedMedia.remove(media);
	}
	/**
	 * @param media
	 * @return true
	 */
	public void checkInCopy(LendableMedia media, int sn) {	
		if(media.getCopies().containsKey(sn)) {
			MediaCopy copy = media.getCopies().get(sn);
			Person person = copy.getBorrower();
			media.setCopyUnlent(sn);
			if(!person.isBorrowing()) {
				getBorrowers().remove(person.getName());
			}
		}
	}
	public void checkOutCopy(LendableMedia media, Person borrower) {
		media.setCopyToLent(borrower);
		getBorrowers().put(borrower.getName(), borrower);
	}
	public boolean isLibraryEmpty() {
		return getStoredMedia().size() == 0;
	}
	public boolean isPersonBorrowing(String name) {
		return getBorrowers().containsKey(name);
	}
	public Person getBorrower(String name) {
		return getBorrowers().get(name);
	}

	public Person findPerson(String name) {
		return getBorrowers().get(name);
	}
	public LendableMedia getMedia(String articleNumber) {
		return storedMediaHash.get(articleNumber);
	}
	public Iterator<LendableMedia> getStoredMediaIterator() {
		return getStoredMedia().iterator();
	}
	private ArrayList<LendableMedia> getStoredMedia() {
		return storedMedia; //TODO could be changed from here! isSorted? return clone? Iterator
	}
	public Iterator<Person> getBorrowersIterator() {
		return getBorrowers().values().iterator();
	}
	private void setBorrowers(HashMap<String, Person> borrowers) {
		this.borrowers = borrowers;
	}
	private HashMap<String, Person> getBorrowers() {
		return borrowers;
	}
	public boolean isExistingArticleNumber(String articleNumber) {
		return storedMediaHash.containsKey(articleNumber);
	}
}
