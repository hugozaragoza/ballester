/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.simplegrammar.types;

import org.junit.Assert;
import org.junit.Test;

import ballester.datastructures.fs.FeatStruct;
import ballester.datastructures.fs.Feature;
import ballester.grammar.simplegrammar.types.Lexicon;
import ballester.grammar.simplegrammar.types.Word;

/**
 * @author hugoz
 *
 */
public class LexiconTest {

	@Test
	public void test1() {
		Lexicon l = Lexicon.getLexicon();
		Word w = l.find("green");
		FeatStruct fs = w.semmantics.getFeature(Lexicon.SEM_update).getFeatureStructure();
		Assert.assertEquals("green", fs.getFeatureTerminalValue("value"));
	}

}
