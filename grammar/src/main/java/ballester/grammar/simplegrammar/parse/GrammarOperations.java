/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.grammar.simplegrammar.parse;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import ballester.datastructures.fs.FeatStruct;
import ballester.datastructures.tree.OrderedTree;
import ballester.grammar.simplegrammar.types.Word;

/**
 * @author hugoz
 *
 */
public class GrammarOperations {
	final private static Logger logger = Logger.getLogger(GrammarOperations.class);

	/**
	 * @param tree
	 * @param i
	 */
	static int blendSemmanticsWithNextSyntaxMatchingFeat(OrderedTree<Word> tree, int i, FeatStruct fs,
			boolean removeAfter) {
		int noun = findSiblingGeneralizingSyntax(tree, i, fs, true);
		Word parent = tree.getNode(noun);
		Word attaching = tree.getNode(i);
		if (noun >= 0) {
			parent.semmantics.add(attaching.semmantics);
			if (removeAfter) {
				tree.remove(i);
			}
			return noun;
		}
		return -1;
	}

	/**
	 * @param tree
	 * @param i
	 * @return true if ok
	 */
	static boolean pushUnder(OrderedTree<Word> tree, int pushedNode, int newParent) {
		Word current = tree.getNode(pushedNode);
		tree.remove(pushedNode);
		int newID = tree.addChild(newParent, current, pushedNode);
		if (newID != pushedNode) {
			logger.error("SHOULD NOT HAPPEN! " + newID + "!=" + pushedNode);
		}
		return true;
	}

	/**
	 * find siblings with a feature with 'name' and a 'value' that generalizes
	 * 'value'
	 * 
	 * @param tree
	 * @param i
	 * @param fsValue
	 * @return
	 */
	private static int findSiblingGeneralizingSyntax(OrderedTree<Word> tree, int i, FeatStruct syntax,
			boolean nextOnly) {

		// get sibling words (removing itself):
		ArrayList<Integer> lisS = tree.getSiblings(i, true);
		ArrayList<Integer> lis = new ArrayList<Integer>(lisS.size());
		for (int j : lisS)
			lis.add(j);
		ArrayList<Word> lisW = tree.getNodes(lis);

		// find match:
		Word t = findNodeMatchingSyntax(syntax, lisW);
		if (t == null)
			return -1;
		int j = tree.getNodeIndex(t);
		return j;
	}

	static Word findNodeMatchingSyntax(FeatStruct query, ArrayList<Word> words) {
		for (Word w : words) {
			FeatStruct targetSyntax = w.syntax;
			if (w.syntax != null && w.syntax.size() > 0) {
				if (query.generalizes(targetSyntax))
					return w;
			}
		}
		return null;
	}
}
