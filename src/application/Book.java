package application;

import java.util.HashMap;

public class Book extends LendableMedia {
	private int pages;
	private String author;

	private Book(String articleNumber) {
		this.setInitialised(false);
		this.setArticleNr(articleNumber);
	}

	// throw exception if book not found? throw exception if movie with same articleNumber?
	public static Book getBook(String articleNumber, Library lib) { 
		for (LendableMedia media : lib.getStoredMedia()) {
			if (media.getArticleNr().equals(articleNumber)) {
				if (media instanceof Book) {
					return (Book) media;
				}
			}
		}
		return new Book(articleNumber);
	}

	public void setUpBook(String title, int value, int pages, String author) {
		this.setTitle(title);
		this.setValue(value);
		this.setPages(pages);
		this.setAuthor(author);
		this.setCopies(new HashMap<Integer,MediaCopy>());
		this.setInitialised(true);
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}


	@Override
	public String generalInfo() {
		return "(BOOK) " + getTitle() + "\nValue: " + getValue() + "kr \nPages: " + getPages() + "\nAuthor: "
				+ getAuthor();
	}

}
