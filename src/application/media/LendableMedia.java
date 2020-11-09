package application.media;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import application.Library;
import application.Person;
import application.exceptions.OverStockedException;

public abstract class LendableMedia {

	/**
	 * All lendable media at the library should extend this. It contains copy
	 * management and general media attributes. Copy when referred to here
	 * means MediaCopy instances and not data copies.
	 */
	private String articleNr;
	private String title;
	private int value;
	// the key represent serialNumber
	private HashMap<Integer, MediaCopy> copies;
	private boolean isInitialised;

//
//	public void addNewCopy(MediaCopy copy) throws OverStockedException {
//		MediaCopy copy;
//		if (getCopies().size() == 0) {
//			copy = new MediaCopy(0);
//			getCopies().put(copy.getSerialNumber(), copy);
//		} else {
//			int maxCopies = 1000000;
//			for (int i = 0; i < maxCopies; i++) {
//				if (!getCopies().containsKey(i)) {
//					copy = new MediaCopy(i);
//					getCopies().put(copy.getSerialNumber(), copy);
//					i = maxCopies + 1;
//				}
//				if (i == maxCopies) {
//					throw new OverStockedException("A media can't store more than " + maxCopies + ".");
//				}
//			}
//
//		}
//	}
/**
 * Makes a new MediaCopy with unique serialnumber.
 * @return
 */
	public MediaCopy getCopy() throws OverStockedException{
		int maxCopies = 1000000;
		for (int i = 0; i < maxCopies; i++) {
			if (!getCopies().containsKey(i)) {
				return new MediaCopy(i);
			}
		}
		throw new OverStockedException("A media can't store more than " + maxCopies + ".");
	}
	/**
	 * Look if a copy with argumented serialnumber exists if not it instantiates one and returns it.
	 * @param serialNumber SerialNumber is an ID for copies, it is expected to be unique.
	 * @return
	 */
	public MediaCopy getCopy(int serialNumber) {
		for (int i = 0; i < getCopies().size(); i++) {
			if (getCopies().get(i).getSerialNumber() == serialNumber) {
				return getCopies().get(i);
			}
		}
		//if serialnumber not registered
		return new MediaCopy(serialNumber);
	}
	/** Look if a copy with argumented serialnumber exists if not it instantiates one and sets
	 * borrower and dueDate according to arguments.
	 * 
	 * @param serialNumber SerialNumber is an ID for copies, it is expected to be unique.
	 * @param borrower
	 * @param dueDate
	 * @return
	 */
	public MediaCopy getCopy(int serialNumber, Person borrower, Date dueDate) {
		MediaCopy copy = getCopy(serialNumber);
		copy.setBorrower(borrower);
		copy.setDueDate(dueDate);
		return copy;
	}
/**
 * Register a copy with the media.
 * @param copy
 */
	public void addCopy(MediaCopy copy) {
		getCopies().put(copy.getSerialNumber(), copy);
	}
	public void removeCopy(int serialNumber) {
		getCopies().remove(serialNumber);
	}

	public abstract String generalInfo();

	public String getArticleNr() {
		return articleNr;
	}

	// should throw illegalArticleNumber if String is not number
	public void setArticleNr(String articleNr) {
		this.articleNr = articleNr;
	}

	public Person getBorrower(String name) {
		for (MediaCopy copy : getCopies().values()) {
			Person borrower = copy.getBorrower();
			if (borrower.getName().equalsIgnoreCase(name)) {
				return borrower;
			}
		}
		return null;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isInStock() {
		if (getCopies().size() == 0)
			return false;
		for (MediaCopy copy : getCopies().values()) {
			if (!copy.isBorrowed()) {
				// TODO throw exception
				return true;
			}
		}
		return false;
	}

	public boolean isLended() {
		if (getNumberOfLendedCopies() > 0) {
			return true;
		}
		return false;
	}

	public boolean isNotLended() { // TODO namn..
		if (getCopies().size() == 0)
			return true;
		for (MediaCopy copy : getCopies().values()) {
			if (copy.isBorrowed()) {
				// TODO throw exception
				return false;
			}
		}
		return true;
	}

	public int getNumberOfCopies() {
		return getCopies().size();
	}

	public int getNumberOfLendedCopies() {
		if (hasCopy()) {
			return getNumberOfCopies() - getNumberOfAvailableCopies();
		} else {
			return 0;
		}
	}

	public int getNumberOfAvailableCopies() {
		if (hasCopy()) {
			int copyCounter = 0;
			for (MediaCopy copy : getCopies().values()) {
				if (!copy.isBorrowed()) {
					copyCounter++;
				}
			}
			return copyCounter;
		} else {
			return 0;
		}

	}

	public int getNumberOfExpiredLoans() {
		int expiredCounter = 0;
		if (hasCopy()) {
			for (MediaCopy copy : getCopies().values()) {
				if (copy.isDue()) {
					expiredCounter++;
				}
			}
		}
		return expiredCounter;
	}

	public ArrayList<MediaCopy> getLendedCopies() {
		ArrayList<MediaCopy> lendedCopies = new ArrayList<MediaCopy>();
		if (hasCopy()) {
			for (MediaCopy copy : getCopies().values()) {
				if (copy.isBorrowed()) {
					lendedCopies.add(copy);
				}
			}
		}
		return lendedCopies;
	}

	public ArrayList<MediaCopy> getExpiredCopies() {
		ArrayList<MediaCopy> expiredCopies = new ArrayList<MediaCopy>();
		if (hasCopy()) {
			for (MediaCopy copy : getCopies().values()) {
				if (copy.isDue()) {
					expiredCopies.add(copy);
				}
			}
		}
		return expiredCopies;
	}

	public MediaCopy getAvailableCopy() {
		for (MediaCopy copy : getCopies().values()) {
			if (!copy.isBorrowed()) {
				return copy;
			}
		}
		return null;
	}

	public boolean hasCopy() {
		if (getCopies().size() == 0) {
			return false;
		} else {
			return true;
		}
	}
/**Sets the copy to its lended state.
 * 
 * @param borrower the person borrowing the media.
 * @return Returns true if checkout successful false if not.
 */
	public boolean setCopyToLent(Person borrower) { //TODO what if we are reseting from memory
		if (isInStock()) {
			long millisFourWeeks = 2419200000L; // four weeks in milliseconds
			long millisCurrent = System.currentTimeMillis();
			MediaCopy copy = getAvailableCopy();
			copy.setDueDate(new Date(millisCurrent + millisFourWeeks));
			copy.setBorrower(borrower);
			copy.setDue(true);
			borrower.getBorrowedCopy().add(copy);
			return true;
		}else {
			return false;
		}
	}
	/**
	 * Resets the copy to its unlended state.
	 * @param serialNumber
	 */
	public void setCopyUnlent(int serialNumber) {
		MediaCopy copy = getCopy(serialNumber);
		copy.getBorrower().removeCopy(copy);
		copy.setBorrower(null);
		copy.setDueDate(null);
		copy.setDue(false);
	}
	/**Which copy is supposed to be turned in the soonest?
	 * 
	 * @return
	 */
	public MediaCopy getCopyWithEarliestDueDate() {
		if (isInStock()) {
			return getAvailableCopy();
		}
		if (getCopies().size() > 0) {
			MediaCopy earliestCopy = getCopies().get(0);
			for (MediaCopy copy : getCopies().values()) {
				if (copy.getDueDate().before(earliestCopy.getDueDate())) {
					earliestCopy = copy;
				}
			}
			return earliestCopy;
		}
		return null;
	}

	public HashMap<Integer, MediaCopy> getCopies() {
		return copies;
	}

	public void setCopies(HashMap<Integer, MediaCopy> copies) {
		this.copies = copies;
	}

	public boolean isInitialised() {
		return isInitialised;
	}

	public void setInitialised(boolean isInitialised) {
		this.isInitialised = isInitialised;
	}

}
