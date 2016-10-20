package ballester.datastructures.fs;

import java.io.IOException;
import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import ballester.utils.UnitTestUtils;

public class FSDaoTest {

	@Test
	public void test2() throws ParseException, IOException {
		String txt = UnitTestUtils.readResourceAsString(this.getClass(), "testFeatureSet1.fs");

		FeatStruct fs = FSDao.parse(txt);
		System.out.println(fs);
		Assert.assertEquals(4, fs.size());
		// FIXME complete tests
	}

}
