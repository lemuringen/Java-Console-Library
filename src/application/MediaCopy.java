package application;

import java.sql.Date;

public class MediaCopy {
	private Person borrower;
	private Date dueDate;
	private boolean isDue;
	private int serialNumber;

	public MediaCopy(int serialNumber) {
		this(serialNumber, null, null);
	}

	public MediaCopy(int serialNumber, Person borrower, Date dueDate) {
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

	public void checkIn() {
		getBorrower().removeCopy(this);
		this.setBorrower(null);
		this.setDueDate(null);
		this.setDue(false);
	}

	public boolean isBorrowed() {
		if (getBorrower() == null) {
			return false;
		} else {
			return true;
		}
	}

	public void setBorrower(Person borrower) {
		this.borrower = borrower;
	}

	// TODO maybe throw custom exception??
	public Person getBorrower() {
		return this.borrower;
	}

	public Date getDueDate() {
		return dueDate;
	}

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

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
}
