import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.voetsjoeba.imdb.renamer.domain.analysis.StandardFilenameAnalyzer;
import com.voetsjoeba.imdb.renamer.domain.analysis.FilenameAnalyzer;
import com.voetsjoeba.imdb.renamer.domain.analysis.FilenameInfo;


public class TitleNameExtractorTest {
	
	private static final FilenameAnalyzer filenameAnalyzer = new StandardFilenameAnalyzer();
	
	protected String getExtractedTitle(String title){
		File dummyFile = new File(title);
		FilenameInfo extractedInfo = filenameAnalyzer.getFilenameInfo(dummyFile);
		List<String> words = extractedInfo.getTitleWords();
		return StringUtils.join(words, " ");
	}
	
	/**
	 * Tests whether only the last match of a release group pattern is removed
	 */
	@Test public void testRemoveLastReleaseGroupOnly() {
		
		String title = getExtractedTitle("Take.Me.Home.Tonight.2011.DVDRip.XViD-EP1C-EP1C.avi");
		Assert.assertTrue(title.contains("EP1C"));
		
	}
	
	@Test public void testDummy(){
		getExtractedTitle("Take.Me.Home.Tonight.2011.DVDRip.XViD-EP1C.avi");
	}
	
}
