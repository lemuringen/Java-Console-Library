package application;

import java.util.ArrayList;
import java.util.HashMap;

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
	public ArrayList<LendableMedia> getStoredMedia() {
		return storedMedia; //TODO could be changed from here! isSorted? return clone? Iterator
	}
	public void setStoredMedia(ArrayList<LendableMedia> storedMedia) {
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
	 * 
	 * @param media
	 * @return true
	 */
	public void checkInCopy(LendableMedia media, int sn) {
		if(media.getCopies().containsKey(sn)) {
			MediaCopy copy = media.getCopies().get(sn);
			Person person = copy.getBorrower();
			copy.checkIn();
			if(!person.isBorrowing()) {
				getBorrowers().remove(person.getName());
			}
		}
	}
	public Person findPerson(String name) {
		for(LendableMedia media : getStoredMedia()) {
			Person person = media.getBorrower(name);
			if(person != null) {
				return person;
			}
		}
		return null;
	}
	//TODO should return iterator!!
	public LendableMedia getMedia(String articleNumber) {
//		for(LendableMedia media : getStoredMedia()) {
//				if(media.getArticleNr().equals(articleNumber)) {
//					return media;
//				}
//			}
//			return null;
		//throw if null?
		return storedMediaHash.get(articleNumber);
	}
	public boolean isExistingArticleNumber(String articleNumber) {
//		for (LendableMedia media : getStoredMedia()) {
//			if (media.getArticleNr().equals(articleNumber)) {
//				return true;
//			}
//		}
//		return false;
		return storedMediaHash.containsKey(articleNumber);
	}
	public HashMap<String, Person> getBorrowers() {
		return borrowers;
	}
	public void setBorrowers(HashMap<String, Person> borrowers) {
		this.borrowers = borrowers;
	}

}
