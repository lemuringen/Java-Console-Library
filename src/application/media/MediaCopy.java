package application.media;

import java.sql.Date;

import application.Person;

public class MediaCopy {
	private Person borrower;
	private Date dueDate;
	private boolean isDue;
	private int serialNumber;

	protected MediaCopy(int serialNumber) {
		this(serialNumber, null, null);
	}
	/**should only be 
	 * 
	 * @param serialNumber
	 * @param borrower
	 * @param dueDate
	 */
	protected MediaCopy(int serialNumber, Person borrower, Date dueDate) {
		this.serialNumber = serialNumber;
		this.borrower = borrower;
		this.dueDate = dueDate;
		if (dueDate != null) {
			long millisCurrent = System.currentTimeMillis();
			Date today = new Date(millisCurrent);
			if (today.after(dueDate)) {
				this.isDue = true;
			} else {
				this.isDue = false;
			}
		} else {
			this.isDue = false;
		}
	}



	public boolean isBorrowed() {
		if (getBorrower() == null) {
			return false;
		} else {
			return true;
		}
	}

	protected void setBorrower(Person borrower) {
		this.borrower = borrower;
	}

	// TODO maybe throw custom exception??
	public Person getBorrower() {
		return this.borrower;
	}

	public Date getDueDate() {
		return dueDate;
	}
//TODO extend loan
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public boolean isDue() {
		return isDue;
	}

	public void setDue(boolean isDue) {
		this.isDue = isDue;
	}

	public int getSerialNumber() {
		return serialNumber;
	}

private void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
}
