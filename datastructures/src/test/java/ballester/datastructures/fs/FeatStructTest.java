/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.datastructures.fs;

import org.junit.Test;

import junit.framework.Assert;

/**
 * @author hugoz
 *
 */
public class FeatStructTest {

    @Test
    public void testBooleans() {

	FeatStruct fs = new FeatStruct();
	Assert.assertFalse(fs.isTrue("prop1"));
	fs.set("prop1", true);
	Assert.assertTrue(fs.isTrue("prop1"));
	fs.set("prop1", false);
	Assert.assertFalse(fs.isTrue("prop1"));
    }
}
