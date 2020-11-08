package application;

import java.util.HashMap;

public class Movie extends LendableMedia {
	private int length; // length in minutes
	private double IMDBRating; //rating with one decimal up to 10,0 TODO

	private Movie(String articleNumber) {
		this.setInitialised(false);
		this.setArticleNr(articleNumber);
	}

	// throw exception if book not found? throw exception if movie with same articleNumber?
	public static Movie getMovie(String articleNumber, Library lib) { 
		for (LendableMedia media : lib.getStoredMedia()) {
			if (media.getArticleNr().equals(articleNumber)) {
				if (media instanceof Movie) {
					return (Movie) media;
				}
			}
		}
		return new Movie(articleNumber);
	}

	public void setUpMovie(String title, int value, int length, double rating) {
		this.setTitle(title);
		this.setValue(value);
		this.setLength(length);
		this.setIMDBRating(rating);
		this.setCopies(new HashMap<Integer, MediaCopy>());
		this.setInitialised(true);
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public double getIMDBRating() {
		return IMDBRating;
	}

	public void setIMDBRating(double iMDBRating) {
		IMDBRating = iMDBRating;
	}

	@Override
	public String generalInfo() {
		return "(Movie) " + getTitle() + "\nValue: " + getValue() + "kr \nLength: " + getLength() + "m \nRating: "
				+ getIMDBRating();
	}

}
