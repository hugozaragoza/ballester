package ballester.datastructures.fs;

import org.junit.Assert;
import org.junit.Test;

public class FSTest {

	@Test
	public void generalizationTest() {
		FeatStruct large = new FeatStruct();
		large.add(new Feature("name1", "val1"));
		large.add(new Feature("name2", "val2"));

		FeatStruct large2 = new FeatStruct();
		large2.add(new Feature("name1", "val1"));
		large2.add(new Feature("name2", "val2"));

		Assert.assertTrue(large.generalizes(large2));
		Assert.assertTrue(large2.generalizes(large));
		Assert.assertTrue(large.generalizes(large));
	}

	@Test
	public void constructionTest() {
		FeatStruct fs = new FeatStruct();
		fs.add(new Feature("name1", "val1"));
		System.out.println(fs);
		fs.add(new Feature("name2", "val2"));
		System.out.println(fs);
		Feature f = fs.get(1);
		Assert.assertEquals("name1", f.getName());
		Assert.assertEquals("val1", f.value.getTerminal());
		Assert.assertTrue(f.isTerminal());
		f = fs.get(2);
		Assert.assertEquals("name2", f.getName());
		Assert.assertEquals("val2", f.value.getTerminal());
		Assert.assertTrue(f.isTerminal());

		FeatStruct fs2 = new FeatStruct();
		fs2.add(new Feature("name11", "val11"));
		System.out.println(fs2);

		fs.add(new Feature("name3", fs2));
		fs.add(new Feature("name4", "val4"));

		System.out.println(fs2);
		System.out.println(fs);

		f = fs.get(1);
		Assert.assertEquals("name1", f.getName());
		Assert.assertEquals("val1", f.value.getTerminal());
		Assert.assertTrue(f.isTerminal());
		f = fs.get(2);
		Assert.assertEquals("name2", f.getName());
		Assert.assertEquals("val2", f.value.getTerminal());
		Assert.assertTrue(f.isTerminal());
		f = fs.get(3);
		Assert.assertEquals("name3", f.getName());
		Assert.assertFalse(f.isTerminal());
		FeatStruct v = f.value.getFeatureSet();
		f = v.get(1);
		Assert.assertEquals("name11", f.getName());
		Assert.assertEquals("val11", f.getValue().getTerminal());

		f = fs.get(4);
		Assert.assertEquals("name4", f.getName());
		Assert.assertTrue(f.isTerminal());
		Assert.assertEquals("val4", f.value.getTerminal());
	}
}
