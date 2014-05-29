import junit.framework.Assert;

import org.junit.Test;

import com.voetsjoeba.imdb.renamer.util.FilenameUtils;


public class FilenameTest {
	
	@Test
	public void testFilenameSafe() throws Exception {
		
		String clean = FilenameUtils.safeFilename("Foo\n\07\tbar\0?*<>|`:");
		Assert.assertEquals("Foobar", clean);
		
	}
	
}
