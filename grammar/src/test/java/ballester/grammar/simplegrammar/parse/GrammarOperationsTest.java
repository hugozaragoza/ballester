/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.grammar.simplegrammar.parse;

import org.junit.Assert;
import org.junit.Test;

import ballester.datastructures.fs.FeatStruct;
import ballester.datastructures.fs.Feature;
import ballester.datastructures.tree.OrderedTree;
import ballester.grammar.simplegrammar.types.Word;

/**
 * @author hugoz
 *
 */
public class GrammarOperationsTest {

	/**
	 * N1 .semmantics = N2
	 * 
	 * @return
	 */
	public OrderedTree<Word> testTree1() {

		OrderedTree<Word> tree = new OrderedTree<Word>(new Word("root"));
		Word n1 = new Word("w1");
		n1.syntax.add(new Feature("pos", "Det"));
		n1.syntax.add(new Feature("SYNkey1", "valSyn1"));
		n1.semmantics.add(new Feature("SEMkey1", "valSem1"));
		tree.addChild(tree.getRoot(), n1);

		Word n2 = new Word("w2");
		n2.syntax.add(new Feature("pos", "N"));
		n2.syntax.add(new Feature("SYNkey2", "valSyn2"));

		tree.addChild(tree.getRoot(), n2);

		return tree;
	}

	@Test
	public void test_blendSemmanticsWithNextSyntaxMatchingFeat() {
		OrderedTree<Word> tree = testTree1();
		Assert.assertEquals(3, tree.size());

		int child_i = tree.getChild(tree.getRoot(), 0);
		Word child = tree.getNode(child_i);
		Assert.assertEquals("w1", child.lemma);
		Assert.assertEquals(child.syntax.getFeatureTerminalValue("pos"), "Det");

		FeatStruct fs = new FeatStruct();
		fs.add(new Feature("pos", "N"));
		int n = GrammarOperations.blendSemmanticsWithNextSyntaxMatchingFeat(tree, child_i, fs, true);
		Assert.assertEquals(2, n);
		Assert.assertEquals(2, tree.size());
		child_i = tree.getChild(tree.getRoot(), 0);
		child = tree.getNode(child_i);
		Assert.assertEquals("w2", child.lemma);
		// check that semmantics were passed
		Assert.assertEquals("valSem1", child.semmantics.getFeatureTerminalValue("SEMkey1"));

	}

	@Test
	public void test_pushUnderPrevious() {
		int n;

		OrderedTree<Word> tree = testTree1();
		Assert.assertEquals(3, tree.size());
		System.out.println(tree);

		int child_1 = tree.getChild(tree.getRoot(), 0);
		int child_2 = tree.getChild(tree.getRoot(), 1);
		Assert.assertEquals("w1", tree.getNode(child_1).lemma);
		Assert.assertEquals("w2", tree.getNode(child_2).lemma);

		boolean ok = GrammarOperations.pushUnder(tree, child_2, child_1);
		Assert.assertTrue(ok);
		Assert.assertEquals(3, tree.size());
		n = tree.getChild(tree.getRoot(), 0);
		Assert.assertEquals("w1", tree.getNode(n).lemma);
		n = tree.getChild(n, 0);
		Assert.assertEquals("w2", tree.getNode(n).lemma);

	}
}
