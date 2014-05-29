import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voetsjoeba.imdb.renamer.domain.exception.InfiniteInterpolationException;
import com.voetsjoeba.imdb.renamer.domain.exception.InterpolationException;
import com.voetsjoeba.imdb.renamer.domain.interpolation.HashMapInterpolationArguments;
import com.voetsjoeba.imdb.renamer.domain.interpolation.InterpolationArguments;
import com.voetsjoeba.imdb.renamer.domain.interpolation.InterpolationResults;
import com.voetsjoeba.imdb.renamer.domain.interpolation.RenameFormatInterpolator;
import com.voetsjoeba.imdb.renamer.domain.interpolation.StringInterpolator;

public class InterpolationTest {
	
	private static final Logger log = LoggerFactory.getLogger(InterpolationTest.class);
	
	@Test
	public void testInfiniteInterpolation() {
		
		StringInterpolator interpolator = new RenameFormatInterpolator();
		
		InterpolationArguments args = new HashMapInterpolationArguments();
		args.add("episodeTitle", "Recursive ${episodeTitle}");
		
		boolean caughtInfiniteException = false;
		
		try {
			interpolator.interpolate("${episodeTitle}", args);
		}
		catch(InfiniteInterpolationException iiex) {
			caughtInfiniteException = true;
		}
		catch(InterpolationException e) {
			// any other error can be ignored, we're only interested in the infinite interpolation case
		}
		
		Assert.assertTrue(caughtInfiniteException);
		
	}
	
	@Test
	public void testInterpolationArguments() throws InterpolationException {
		
		StringInterpolator interpolator = new RenameFormatInterpolator();
		
		InterpolationArguments args = new HashMapInterpolationArguments();
		args.add("episodeNr", Integer.valueOf(2));
		
		String interpolated = interpolator.interpolate("${episodeNr:3}", args);
		String interpolated2 = interpolator.interpolate("${episodeNr:03}", args);
		
		Assert.assertEquals(interpolated, "  2");
		Assert.assertEquals(interpolated2, "002");
		
	}
	
	@Test
	public void testExtendedInterpolation() throws InterpolationException {
		
		// O hai ${epi${seriesTitle}Title} is usum
		// seriesTitle -> sode
		// episodeTitle -> GUI
		
		StringInterpolator interpolator = new RenameFormatInterpolator();
		
		InterpolationArguments args = new HashMapInterpolationArguments();
		args.add("episodeTitle", "GUI");
		args.add("seriesTitle", "sode");
		
		InterpolationResults results = interpolator.interpolateExtended("O hai ${epi${seriesTitle}Title} is usum", args);
		Assert.assertEquals(results.getOutput(), "O hai GUI is usum");
		
	}
	
	@Test
	public void testAdjacentInterpolationRegression() throws InterpolationException {
		
		StringInterpolator interpolator = new RenameFormatInterpolator();
		
		InterpolationArguments args = new HashMapInterpolationArguments();
		args.add("episodeTitle", "wat");
		args.add("seriesTitle", "wut");
		args.add("seasonNr", Integer.valueOf(2));
		args.add("episodeNr", Integer.valueOf(4));
		
		InterpolationResults result = interpolator.interpolateExtended("${seriesTitle} - S${seasonNr:02}${episodeNr:02} - ${episodeTitle}", args);
		Assert.assertEquals(result.getOutput(), "wut - S0204 - wat");
		
	}
	
}
