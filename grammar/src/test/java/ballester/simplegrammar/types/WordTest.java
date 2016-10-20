/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.simplegrammar.types;

import org.junit.Assert;
import org.junit.Test;

import ballester.datastructures.fs.Feature;
import ballester.grammar.simplegrammar.types.Word;

/**
 * @author hugoz
 *
 */
public class WordTest {

	@Test
	public void testCopy() {
		Word w = new Word("dragon");
		w.semmantics.add(new Feature("FEATNAME", "feat_value"));

		Word w2 = w.copy();
		Assert.assertEquals("feat_value", w2.semmantics.getFeatureTerminalValue("FEATNAME"));

		// check that modifying copy does not modify original
		w2.semmantics.add(new Feature("FEATNAME2", "feat_value2"));
		Assert.assertEquals("feat_value2", w2.semmantics.getFeatureTerminalValue("FEATNAME2"));
		Assert.assertNull(w.semmantics.getFeatureTerminalValue("FEATNAME2"));

	}

}
