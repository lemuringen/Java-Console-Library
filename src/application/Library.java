package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import application.media.LendableMedia;
import application.media.MediaCopy;

public class Library {
	private ArrayList<LendableMedia> storedMedia; // bad name maybe, even borrowed media is included
	// key = articleNumber,
	private HashMap<String, LendableMedia> storedMediaHash;
	private boolean isSorted;

	public Library() {
		this.setStoredMedia(new ArrayList<LendableMedia>());
		this.storedMediaHash = new HashMap<String, LendableMedia>();
		this.isSorted = false;
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
		if (!isSorted) {
			storedMedia.sort(new CompareArticleNumber());
			this.isSorted = true;
		}
	}

	public void removeMedia(String articleNumber) {// TODO what if there are references left to the media otherwhere?
		LendableMedia media = getMedia(articleNumber);
		storedMediaHash.remove(articleNumber);
		storedMedia.remove(media);
	}

	/**
	 * @param media
	 * @return true
	 */
	public void checkInCopy(LendableMedia media, int sn) {
		if (media.isExistingCopy(sn)) {
			media.setCopyUnlent(sn);
		}
	}

	public void checkOutCopy(LendableMedia media, Person borrower) {
		media.setCopyToLent(borrower);
	}

	public boolean isLibraryEmpty() {
		return getStoredMedia().size() == 0;
	}

	public boolean isPersonBorrowing(String name) {
		return getBorrower(name) != null;
	}

	public LendableMedia getMedia(String articleNumber) {
		return storedMediaHash.get(articleNumber);
	}

	public Iterator<LendableMedia> getStoredMediaIterator() {
		return getStoredMedia().iterator();
	}

	private ArrayList<LendableMedia> getStoredMedia() {
		return storedMedia; // TODO could be changed from here! isSorted? return clone? Iterator
	}

	public Iterator<LendableMedia> getLoanedMedia(String name) {
		ArrayList<LendableMedia> loanedMedia = new <LendableMedia>ArrayList();
		for(LendableMedia media : getStoredMedia()) {
			Person person = media.getBorrower(name);
			if(media.getBorrower(name) != null) {
				loanedMedia.add(media);
			}
		}
		return loanedMedia.iterator();
	}

	public MediaCopy getLoanedCopy(String name, LendableMedia media) {
		return media.getBorrowedCopy(name);
	}

	public Person getBorrower(String name) {
		for(LendableMedia media : getStoredMedia()) {
			Person person = media.getBorrower(name);
			if(person != null) {
				return person;
			}
		}
		return null;
	}

	public boolean isExistingArticleNumber(String articleNumber) {
		return storedMediaHash.containsKey(articleNumber);
	}


}
