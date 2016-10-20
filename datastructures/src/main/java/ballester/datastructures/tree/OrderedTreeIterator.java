/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.datastructures.tree;

import org.apache.log4j.Logger;

/**
 * <code>
 * OrderedTreeIterator<FSValue> it = new OrderedTreeIterator<FSValue>(tree, Order.PREORDER);
 *		int i = -1;
 *		it.next(); // skip root;
 *		so.append(tab(parentDepth, 0, paintNodeIndices) + "[\n");
 *		do {
 *         i = it.next();
 *         if (i < 0)
 *			 { break; }
 *			String name = tree.getNode(i).getTerminal();
 *			int depth = tree.getDepth(i);
 * </code>
 * 
 * (NOTE: Was coded for writting , reading and debugging simplicity, totally not
 * optimized for performance)
 * 
 * @author hugoz
 *
 * @param <T>
 */
public class OrderedTreeIterator<T> {
	final private static Logger logger = Logger.getLogger(OrderedTreeIterator.class);

	protected int current;

	public enum Order {
		POSTORDER(true), PREORDER(true);

		private boolean isDepthFirst;

		Order(boolean isDepthFirst) {
			this.isDepthFirst = isDepthFirst;
		}

		public boolean isDepthFirst() {
			return isDepthFirst;
		}
	};

	Order order;

	OrderedTree<T> tree;
	int headOfIteration;
	boolean started = false;

	public OrderedTreeIterator(OrderedTree<T> tree, Order order) {
		this(tree, order, tree.getRoot());
	}

	public OrderedTreeIterator(OrderedTree<T> tree, Order order, int headOfIteration) {
		this.tree = tree;
		this.order = order;
		current = -1;
		if (headOfIteration < 0)
			this.headOfIteration = tree.getRoot();
		else
			this.headOfIteration = headOfIteration;
	}

	public boolean done() {
		startIfNeeded();
		return (current < 0);
	}

	public boolean notDone() {
		return (current >= 0);
	}

	public void startIfNeeded() {
		if (started != true)
			next();
	}

	public int next() {
		if (started != true)
			started = true;
		else if (current < 0)
			return current;

		if (order.equals(Order.POSTORDER)) {
			current = nextPostOrder(current, headOfIteration);
		} else if (order.equals(Order.PREORDER)) {
			current = nextPreOrder(current, headOfIteration);
		} else {
			logger.error("NOT IMPLEMENTED order:" + order.name());
			return -1;
		}
		return current;
	}

	public T getCurrent() {
		return tree.getNode(current);
	}

	public T getNext() {
		next();
		if (current < 0) {
			return null;
		}
		return tree.getNode(current);
	}

	public int nextBrother() {
		startIfNeeded();
		return tree.getNextSibling(current);
	}

	public int nextNotDown() {

		startIfNeeded();

		if (order.isDepthFirst()) {
			logger.error("Not possible in depth-first order");
			return -1;
		} else {

			int n = -1;
			n = tree.getNextSibling(current);
			if (n < 0) {
				n = tree.getParent(current);
			}

			current = n;
			return current;
		}
	}

	public int current() {
		return current;
	}

	public T currentNode() {
		return tree.getNode(current);
	}

	public void reset() {
		current = -1;
	}

	int lastMove = 0;

	public int nextPostOrder(int node, int iterationStart) {
		if (node == iterationStart)
			return -1; // done.
		if (node == -1)
			return tree.getLastChildRecursively(0); // initial depth first.

		if (tree.nextChild.containsKey(node)) {
			lastMove = 0;
			return tree.getLastChildRecursively(tree.nextChild.get(node));
		} else {
			lastMove = -1;
			return tree.parentOf.get(node);
		}
	}

	public int nextPreOrder(int node, int iterationStart) {

		if (node < 0)
			return iterationStart; // first time

		if (tree.firstChild.containsKey(node)) {
			lastMove++;
			return tree.firstChild.get(node);
		}

		else if (tree.nextChild.containsKey(node)) {
			return tree.nextChild.get(node);
		}

		else {

			do {
				if (tree.parentOf.get(node) == null) {
					// at root
					return -1;
				}
				node = tree.parentOf.get(node);
				lastMove--;
				if (node == iterationStart)
					return -1;
				else if (tree.nextChild.containsKey(node))
					return tree.nextChild.get(node);
			} while (true);
		}
	}

}
