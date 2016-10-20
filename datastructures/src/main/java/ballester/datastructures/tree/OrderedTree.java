/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.datastructures.tree;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ballester.datastructures.Copyable;
import ballester.datastructures.fs.FSValue;
import ballester.datastructures.tree.OrderedTreeIterator.Order;

/**
 * Ordered tree
 * 
 * Interface
 * <ul>
 * <li>getParent(node): gives us the parent of each node
 * <li>getChildren(node) gives the (ordered) sequence of children
 * </ul>
 * 
 * Implementation
 * <ul>
 * <li>parent maps children->parent
 * <li>firsChild maps parent->firstChildren
 * <li>nextChild maps sibling->nextSibling
 * <li>lastChild
 * 
 * inEach node may have a parent, a firstChild, a nextChild and a prevChild.
 * <li>The tree has a root, a last child
 * </ul>
 * 
 * NOTES:
 * <ul>
 * <li>this is optimized for simplicity of coding and debugging, not for
 * performance...
 * <li>all the information is stored in this class, so that nodes do not need to
 * implement any "node" interface.
 * 
 * @author hugoz
 *
 * @param <T>
 */
public class OrderedTree<T> {

	public static boolean displayNodeIndices = true;
	private Hashtable<Integer, T> nodes;

	Hashtable<Integer, Integer> parentOf; // child -> parent : only one since
											// nodes in tree cannot have more
											// than
											// one parent

	Hashtable<Integer, Integer> nextChild;
	Hashtable<Integer, Integer> prevChild;
	Hashtable<Integer, Integer> firstChild;
	int lastIndex;
	private int root;

	public int Root() {
		return getRoot();
	}

	public OrderedTree(T rootNode) {
		lastIndex = -1;
		nodes = new Hashtable<Integer, T>();
		root = _addNode(rootNode, null);

		parentOf = new Hashtable<Integer, Integer>();
		nextChild = new Hashtable<Integer, Integer>();
		firstChild = new Hashtable<Integer, Integer>();
		prevChild = new Hashtable<Integer, Integer>();
	}

	/**
	 * This only adds the node to the tree but does not link it, use it only
	 * before linking.
	 * 
	 * @param node
	 * @param forceId
	 *            : node is given this id (this may result in inconsistent
	 *            trees, but useful for moves)
	 * @return
	 */
	private int _addNode(T node, Integer forceId) {
		Integer gindx = forceId;
		if (gindx == null) {
			gindx = ++lastIndex;

		}
		nodes.put(gindx, node);
		return gindx;
	}

	public int addChild(Integer parent, T node) {
		return addChild(parent, node, null);
	}

	/**
	 * @param parent
	 * @param node
	 * @param newNodeId
	 *            : if null, an appropiate one will be assigned, otherwise this
	 *            one is forced, which may lead to an inconsistent tree
	 * @return index of new child
	 */
	public int addChild(Integer parent, T node, Integer newNodeId) {
		if (!nodes.containsKey(parent)) {
			throw (new IndexOutOfBoundsException("No node exists with index: " + parent));
		}
		int gindx = _addNode(node, newNodeId);
		_linkToParent(parent, gindx);
		return gindx;
	}

	/**
	 * @param parent
	 * @param gindx
	 */
	private void _linkToParent(Integer parent, int gindx) {
		parentOf.put(gindx, parent);
		if (firstChild.containsKey(parent) == false) {
			firstChild.put(parent, gindx);
		} else {
			Integer last = getLastChild(parent);
			nextChild.put(last, gindx);
			prevChild.put(gindx, last);
		}
	}

	public int size() {
		return nodes.size();
	}

	@Override
	public String toString() {
		StringBuffer so = new StringBuffer();

		OrderedTreeIterator<T> it = new OrderedTreeIterator<T>(this, Order.PREORDER);
		int i = -1;
		do {
			i = it.next();
			if (i < 0)
				break;
			so.append(tab(getDepth(i), i));
			so.append(getNode(i) + "\n\n");
		} while (true);
		return so.toString();

		//
		// int dl= depthLastNext(-1, getRoot());
		// do {
		// if (lastMove>0) {
		// for(int l=0;l<lastMove;l++) {
		// depth++;
		// for(int j=0;j<depth;j++) so.append("\t");
		// so.append("<\n");
		// }
		// }
		// else {
		// for(int l=0;l<-lastMove;l++) {
		// for(int j=0;j<depth;j++) so.append("\t");
		// so.append(">\n");
		// depth--;
		// }
		//
		// }
		//
		// for(int j=0;j<depth;j++) so.append("\t");
		// T ws = getNode(dl);
		// if (ws==null)
		// so.append( "NULL node\n");
		// else
		// so.append( ws.toString()+ "\n");
		// dl = depthLastNext(dl, getRoot());
		// } while(dl>=0);
		//
		// for(int l=0;l<-lastMove;l++) {
		// for(int j=0;j<depth;j++) so.append("\t");
		// so.append(">\n");
		// depth--;
		// }
		// return so.toString();
	}

	public static String tab(int depth, int node) {
		// return StringUtils.repeat(".",(depth+1))+
		// StringUtils.repeat(" ",4*(depth+1));
		String s = displayNodeIndices ? ("N" + node + "(L" + depth + ")") : "";
		return s + StringUtils.repeat(" ", 4 * (depth + 1));
	}

	public boolean isRoot(int i) {
		return getRoot() == i;
	}

	public boolean isFirstChild(int i) {
		if (getRoot() == i)
			return false; // convention
		return (firstChild.contains(i));
	}

	public boolean isLastChild(int i) {
		if (getRoot() == i)
			return false; // convention
		return !(prevChild.contains(i));
	}

	public T getNode(int i) {
		if (nodes.containsKey(i))
			return nodes.get(i);
		else
			return null;
	}

	public int getNodeIndex(T t) {
		for (int i : nodes.keySet()) {
			if (nodes.get(i) == t) {
				return i;
			}
		}
		return -1;
	}

	public T getNodeRoot() {
		return nodes.get(getRoot());
	}

	public int getLastChild(Integer parent) {
		Integer node = firstChild.get(parent);
		if (node == null)
			return -1;

		while (nextChild.containsKey(node)) {
			node = nextChild.get(node);
		}
		return node;
	}

	public Integer findNode(T node) {
		for (Integer i : nodes.keySet()) {
			if (nodes.get(i).equals(node)) {
				return i;
			}
		}
		return null;
	}

	// public int addSequence(Integer parentNode, Collection<T> sequence) {
	// int last = -1, first = -1;
	// for (T s : sequence) {
	// int i = addNode(s);
	// parent.put(i, parentNode);
	// if (last >= 0) {
	// prevChild.put(i, last);
	// nextChild.put(last, i);
	// } else {
	// first = i;
	// int l = lastChild(parentNode);
	// if (l < 0)
	// firstChild.put(parentNode, i);
	// else {
	// nextChild.put(l, i);
	// prevChild.put(i, l);
	// }
	// }
	// last = i;
	// }
	// return first;
	// }

	/**
	 * @param node
	 * @return keep going down (not nextSibling but nextChild)
	 */
	public Integer getLastChildRecursively(int node) {
		while (firstChild.containsKey(node)) {
			node = firstChild.get(node);
		}
		return node;
	}

	/**
	 * @param node
	 * @return next brother
	 */
	public Integer getNextSibling(int node) {
		if (nextChild.containsKey(node))
			return nextChild.get(node);
		else
			return -1;
	}

	public Integer getPreviousSibling(int node) {
		if (prevChild.containsKey(node))
			return prevChild.get(node);
		else
			return -1;
	}

	// public Boolean removeHead(int headNode) {
	//
	// if (nextChild.containsKey(headNode))
	// return false;
	//
	// int parentN = getParent(headNode);
	// int childN = getFirstChild(headNode);
	//
	// if (headNode == getRoot()) {
	// root = childN;
	// if (childN > 0)
	// parent.remove(childN);
	// } else if (childN > 0)
	// firstChild.put(parentN, childN);
	//
	// while (childN > 0) {
	// parent.put(childN, parentN);
	// childN = getNextSibling(childN);
	// }
	// return true;
	// }

	// /**
	// * pushes under newHead the sub-sequence starting from startNode of length
	// * length
	// *
	// * @param startNode
	// * @param length
	// * @param newHead
	// * @return index of new node
	// */
	// public int pushDownSequence(int startNode, int length, T newHead) {
	// int leftNode = -1;
	// int parentNode = -1;
	// int rightNode = startNode;
	// int lastSeqNode = startNode;
	//
	// if (prevChild.containsKey(startNode))
	// leftNode = prevChild.get(startNode);
	// if (parent.containsKey(startNode))
	// parentNode = parent.get(startNode);
	//
	// int gindx = ++lastIndex;
	// nodes.put(gindx, newHead);
	// firstChild.put(gindx, startNode);
	// for (int i = 0; i < length; i++) {
	// parent.put(rightNode, gindx);
	// lastSeqNode = rightNode;
	// rightNode = getNextSibling(rightNode);
	// }
	//
	// if (parentNode >= 0)
	// parent.put(gindx, parentNode);
	//
	// if (leftNode >= 0) {
	// nextChild.put(leftNode, gindx);
	// prevChild.put(gindx, leftNode);
	// prevChild.remove(startNode);
	// } else if (parentNode >= 0)
	// firstChild.put(parentNode, gindx);
	// else
	// root = gindx;
	//
	// if (rightNode >= 0) {
	// nextChild.put(gindx, rightNode);
	// prevChild.put(rightNode, gindx);
	// nextChild.remove(lastSeqNode);
	// }
	//
	// return gindx;
	// }

	/**
	 * @param father
	 * @param n
	 *            : nth child (starting at 0)
	 * @return
	 */
	public int getChild(int father, int n) {
		if (firstChild.containsKey(father) == false)
			return -1;
		int ret = firstChild.get(father);
		for (int i = 1; i <= n; i++) {
			ret = getNextSibling(ret);
			if (ret < 0)
				return -1;
		}
		return ret;
	}

	/**
	 * get child of root
	 * 
	 * @param n
	 *            : nth child (starting at 0)
	 * @return
	 */
	public int getChild(int n) {
		return getChild(getRoot(), n);
	}

	public int numOfChildren(int node) {
		int children = 0;
		if (firstChild.containsKey(node) == false)
			return 0;
		node = firstChild.get(node);
		children = 1;
		while (nextChild.containsKey(node) == true) {
			node = nextChild.get(node);
			children++;
		}
		return children;
	}

	public int numOfNextNodes(int node) {
		int next = 0;

		while (nextChild.containsKey(node)) {
			node = nextChild.get(node);
			next++;
		}
		return next;
	}

	public int getRoot() {
		return root;
	}

	public int getParent(int node) {
		if (parentOf.containsKey(node))
			return parentOf.get(node);
		else
			return -1;
	}

	public int getDepth(int i) {
		int d = 0;
		i = getParent(i);
		while (i >= 0) {
			d++;
			i = getParent(i);
		}
		return d;
	}

	/**
	 * 
	 * @param parentNode
	 * @return
	 */
	public ArrayList<Integer> getChildren(int parentNode) {

		if (getFirstChild(parentNode) < 0)
			return new ArrayList<Integer>(0);

		ArrayList<Integer> ret = new ArrayList<Integer>();
		ret.add(getFirstChild(parentNode));
		while (nextChild.containsKey(ret.get(ret.size() - 1))) {
			ret.add(nextChild.get(ret.get(ret.size() - 1)));
		}
		return ret;
	}

	/**
	 * @param node
	 * @return first child
	 */
	public int getFirstChild(int node) {
		if (firstChild.containsKey(node))
			return firstChild.get(node);
		else
			return -1;
	}

	/**
	 * @param i
	 * @return
	 */
	public int getNext(int i) {
		if (nextChild.containsKey(i))
			return nextChild.get(i);
		else
			return -1;
	}

	/**
	 * 
	 * 
	 * @param i
	 * @param newParent
	 */
	public void pushUnderSibling(int i, int nextSibling) {
		if (i < 0 || nextSibling < 0) {
			throw new IndexOutOfBoundsException();
		}

		if (parentOf.get(i) != parentOf.get(nextSibling)) {
			throw new IndexOutOfBoundsException("pushUnderSibling: nodes are not siblings! " + i + "," + nextSibling);
		}

		_unlinkParentsAndSiblings(i);
		_linkToParent(nextSibling, i);
	}

	/**
	 * FIXME: depth will no longer be reliable!
	 * 
	 * @param i
	 */
	private void _unlinkParentsAndSiblings(int i) {

		if (i == root) {
			throw new ArrayIndexOutOfBoundsException();
		}
		Integer p = getParent(i);
		Integer l = getPreviousSibling(i);
		Integer r = getNextSibling(i);

		// ....... p
		// ...... ^v
		// . l <-> i <-> r

		parentOf.remove(i);
		if (firstChild.get(p) == i) {
			if (r >= 0) {
				firstChild.put(p, r);
			} else {
				firstChild.remove(p);
			}
		}

		if (r >= 0) {
			nextChild.remove(i);
			prevChild.remove(r);
		}
		if (l >= 0) {
			nextChild.remove(l);
			prevChild.remove(i);
		}
		if (l >= 0 && r >= 0) {
			nextChild.put(l, r);
			prevChild.put(r, l);
		} else {
			if (l == 0) { // first child
				if (r == 0) { // none lef
					firstChild.remove(i);
				} else
					// connect parent
					firstChild.put(p, r);
			}
		}

		// update depth of all siblings:
		OrderedTreeIterator<T> it = new OrderedTreeIterator<T>(this, Order.PREORDER);

	}

	/**
	 * @param i
	 * @param b
	 * @param nextOnly
	 *            if true, only siblings to the right (after in order) are
	 *            returned
	 * @return
	 * @return all siblings including itself
	 */
	public ArrayList<Integer> getSiblings(int i, boolean nextOnly) {
		if (i == root)
			return new ArrayList<Integer>();
		else if (!hasNode(i)) {
			throw (new IndexOutOfBoundsException("node " + i + " not in tree"));
		}
		int p = getParent(i);
		ArrayList<Integer> lis = getChildren(p);
		if (nextOnly) {
			int j = lis.indexOf(i);
			lis = new ArrayList<Integer>(lis.subList(j + 1, lis.size()));
		}
		return lis;

	}

	/**
	 * @param i
	 * @return
	 */
	public boolean hasNode(int i) {
		return nodes.containsKey(i);
	}

	/**
	 * @param lisI
	 * @return
	 */
	public ArrayList<T> getNodes(List<Integer> lisI) {
		ArrayList<T> ret = new ArrayList<T>(lisI.size());
		for (int i : lisI) {
			T node = getNode(i);
			if (node == null) {
				throw (new IndexOutOfBoundsException("node not found: " + i));
			}
			ret.add(getNode(i));
		}
		return ret;
	}

	/**
	 * @param i
	 */
	public int remove(int i) {
		if (firstChild.containsKey(i)) { // not implemented!
			throw (new IndexOutOfBoundsException("cannot remove nodes with children: " + i));
		}
		_unlinkParentsAndSiblings(i);
		nodes.remove(i);
		return i;
	}

	/**
	 * @param i
	 */
	public int remove(T node) {
		int i = getNodeIndex(node);
		if (i < 0)
			return i;
		return remove(i);
	}

}
