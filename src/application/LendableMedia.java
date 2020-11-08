package application;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class LendableMedia {

	/**
	 * 
	 */
	private String articleNr;
	private String title;
	private int value;
	//private ArrayList<MediaCopy> copies; 
	private HashMap<Integer, MediaCopy> copies;
	private boolean isInitialised;

	public void addNewCopy() {
		MediaCopy copy;
		if (getCopies().size() == 0) {
			copy = new MediaCopy(0);
			getCopies().put(copy.getSerialNumber(), copy);
		} else {
			boolean snExists;
			for (int i = 0; i < 1000000; i++) {
				snExists = false;
				if(!getCopies().containsKey(i)) {
					copy = new MediaCopy(i);
					getCopies().put(copy.getSerialNumber(), copy);
					i = 1000000;
				}
			}

		}
	}

	public void addNewCopy(MediaCopy copy) {
		for (int i = 0; i < getCopies().size(); i++) {
			if (getCopies().get(i).getSerialNumber() == copy.getSerialNumber()) {
				// TODO throw exception
				return;
			}
		}
		getCopies().put(copy.getSerialNumber(),copy);
	}

	public abstract String generalInfo(); // TODO

	public String getArticleNr() {
		return articleNr;
	}

	// should throw illegalArticleNumber if String is not number
	public void setArticleNr(String articleNr) {
		this.articleNr = articleNr;
	}
	public Person getBorrower(String name) {
		for(MediaCopy copy : getCopies().values()) {
			Person borrower = copy.getBorrower();
			if(borrower.getName().equalsIgnoreCase(name)) {
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
		if (getCopies().size() == 0)
			return null;
		for (MediaCopy copy : getCopies().values()) {
			if (!copy.isDue()) {
				// TODO throw exception
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

	public void borrow(Person borrower) { // TODO if not in stock?
		if (isInStock()) {
			long millisFourWeeks = 2419200000L; // four weeks in milliseconds
			long millisCurrent = System.currentTimeMillis();
			MediaCopy copy = getAvailableCopy();
			copy.setDueDate(new Date(millisCurrent + millisFourWeeks));
			copy.setBorrower(borrower);
			copy.setDue(true);
			borrower.getBorrowedCopy().add(copy);
		}
	}

	public MediaCopy getCopyWithEarliestDueDate() { // TODO name wtf
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
