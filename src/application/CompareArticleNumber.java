package application;

import java.util.Comparator;


public class CompareArticleNumber implements Comparator<LendableMedia>{

	@Override
	public int compare(LendableMedia media1, LendableMedia media2) {
//		if(media1.getArticleNumber() > media2.getArticleNumber()) return 1;
//		if(media1.getArticleNumber() < media2.getArticleNumber()) return -1;
		return 0;
	}

}
