package application;

import java.util.Comparator;

public class CompareArticleNumber implements Comparator<LendableMedia>{

	@Override
	public int compare(LendableMedia media1, LendableMedia media2) {
		if(Long.valueOf(media1.getArticleNr()) > Long.valueOf(media2.getArticleNr())) return 1;
		if(Long.valueOf(media1.getArticleNr()) < Long.valueOf(media2.getArticleNr())) return -1;
		if(media1.getArticleNr().length() > media2.getArticleNr().length()) return 1; // 001 > 01
		if(media1.getArticleNr().length() < media2.getArticleNr().length()) return -1;
		return 0;
	}
}
