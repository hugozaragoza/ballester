/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.grammar.simplegrammar.parse;

import org.junit.Assert;
import org.junit.Test;

import ballester.datastructures.fs.Feature;
import ballester.grammar.simplegrammar.types.Lexicon;
import ballester.grammar.simplegrammar.types.Word;
import ballester.grammar.simplegrammar.types.WordTree;

/**
 * @author hugoz
 *
 */
public class SimpleParserTest {
    final Lexicon lex = Lexicon.getLexicon();

    /**
     * (SYN0 and SYN1)
     * 
     * Test that a Det passes existance semantics to N
     */
    @Test
    public void test_VDet() {

	Word p = lex.find("princess");
	Assert.assertNull(p.semmantics.getFeature("_exists"));

	// SYN0
	WordTree sen = SimpleParser.parseSyn0(new String[] { "a", "princess" }, lex);
	System.out.println(sen);
	Assert.assertEquals(3, sen.size());
	Word w1 = sen.getNode(sen.getChild(sen.getRoot(), 0));
	Assert.assertEquals("a", w1.lemma);
	Word w2 = sen.getNode(sen.getChild(sen.getRoot(), 1));
	Assert.assertEquals("princess", w2.lemma);

	// make sure dictionary has not been modified
	Assert.assertNull(p.semmantics.getFeature("_exists"));

	// SYN1
	SimpleParser.parseSyn1(sen);
	Assert.assertEquals(2, sen.size());
	Word n = sen.getNode(sen.getChild(sen.getRoot(), 0));
	Assert.assertEquals(n.lemma, "princess");
	Assert.assertFalse(n.semmantics.isTrue(Lexicon.SEM_GROUNDED));
	// make sure dictionary has not been modified:
	Assert.assertNull(p.semmantics.getFeature(Lexicon.SEM_GROUNDED));

    }

    /**
     * (SYN0 and SYN1)
     * 
     * Test ARG1 words are pushed into verb semantics
     * 
     */
    @Test
    public void test_V2() {

	// a princess had a bow
	String subj = "princess";
	String obj = "bow";
	WordTree sen = SimpleParser.parseSyn0(new String[] { "a", subj, "had", "a", obj }, lex);
	SimpleParser.parseSyn1(sen);
	testV2(subj, obj, sen, "have", Lexicon.SEM_V_POSSESSION);

	obj = "hungry";
	// a princess was hungry
	sen = SimpleParser.parseSyn0(new String[] { "a", subj, "was", obj }, lex);
	SimpleParser.parseSyn1(sen);
	testV2(subj, obj, sen, "to_be", Lexicon.SEM_V_STATE);

    }

    /**
     * @param subj
     * @param obj
     * @param sen
     */
    private void testV2(String subj, String obj, WordTree sen, String vLemma, String sem) {
	System.out.println(sen);
	int i = sen.getChild(sen.getRoot(), 0);
	Word n = sen.getNode(i);
	Assert.assertEquals(n.lemma, vLemma);
	Assert.assertTrue("SEMMATINC FET NOT FOUND: " + sem, n.semmantics.isTrue(sem));
	Assert.assertEquals(2, sen.size()); // words were removed
	// CHECK ARGUMENTS:
	Word arg1 = (Word) n.semmantics.getFeatureTerminalValue(SimpleParser.ARG1);
	Word arg2 = (Word) n.semmantics.getFeatureTerminalValue(SimpleParser.ARG2);
	Assert.assertEquals(arg1.lemma, subj);
	Assert.assertEquals(arg2.lemma, obj);
    }

}
