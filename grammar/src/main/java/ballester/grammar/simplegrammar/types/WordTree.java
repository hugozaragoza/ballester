/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.grammar.simplegrammar.types;

import ballester.datastructures.tree.OrderedTree;

/**
 * @author hugoz
 *
 */
public class WordTree extends OrderedTree<Word> {

	public WordTree() {
		super(new Word("_S"));
	}

}
