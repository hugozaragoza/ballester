/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.datastructures.fs;

import ballester.datastructures.tree.OrderedTree;
import ballester.datastructures.tree.OrderedTreeIterator;

public class FSIterator extends OrderedTreeIterator<FSValue> {

	public FSIterator(OrderedTree<FSValue> tree, Order order) {
		super(tree, order);
	}

}
