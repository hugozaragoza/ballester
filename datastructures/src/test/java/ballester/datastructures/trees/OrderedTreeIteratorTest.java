package ballester.datastructures.trees;

import org.junit.Assert;
import org.junit.Test;

import ballester.datastructures.tree.OrderedTree;
import ballester.datastructures.tree.OrderedTreeIterator;
import ballester.datastructures.tree.OrderedTreeIterator.Order;

public class OrderedTreeIteratorTest {

	static int nodes = 0;

	class Node {
		String name;

		public Node(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "node_" + name;
		}

	};

	@Test
	public void testIterator2() {
		Node[] nodes = new Node[10];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node("N" + i);
		}
		int[] depth;

		// [0 [1 [2]] [3]
		OrderedTree<Node> tree = new OrderedTree<Node>(nodes[0]);
		tree.addChild(0, nodes[1]);
		tree.addChild(1, nodes[2]);
		tree.addChild(0, nodes[3]);
		System.out.println(tree);

		OrderedTreeIterator<Node> it;
		it = new OrderedTreeIterator<Node>(tree, Order.PREORDER);
		depth = new int[] { 0, 1, 2, 3 };
		for (int j = 0; j < depth.length; j++) {
			Assert.assertEquals(depth[j], it.next());
		}
		Assert.assertEquals(-1, it.next());
		Assert.assertEquals(-1, it.next());

		it = new OrderedTreeIterator<Node>(tree, Order.POSTORDER);
		depth = new int[] { 2, 1, 3, 0 };
		for (int j = 0; j < depth.length; j++) {
			Assert.assertEquals(depth[j], it.next());
		}
		Assert.assertEquals(-1, it.next());
		Assert.assertEquals(-1, it.next());
		Assert.assertEquals(-1, it.next());

	}

	@Test
	public void testIterator() {
		Node[] nodes = new Node[10];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node("N" + i);
		}
		int i = 0;
		int[] depth;
		OrderedTree<Node> tree = new OrderedTree<Node>(nodes[0]);
		tree.addChild(0, nodes[1]);
		tree.addChild(0, nodes[2]);
		tree.addChild(1, nodes[3]);
		tree.addChild(1, nodes[4]);
		// 0 [ 1 [ 3 4 ] , 2 ]
		System.out.println(tree);

		OrderedTreeIterator<Node> it;

		it = new OrderedTreeIterator<Node>(tree, Order.PREORDER);
		depth = new int[] { 0, 1, 3, 4, 2 };
		for (int j = 0; j < depth.length; j++) {
			int n = it.next();
			Assert.assertEquals(depth[j], n);
			Assert.assertEquals("N" + depth[j], tree.getNode(n).name);
		}
		Assert.assertEquals(-1, it.next());
		Assert.assertEquals(-1, it.next());
		Assert.assertEquals(-1, it.next());

		it = new OrderedTreeIterator<Node>(tree, Order.POSTORDER);
		depth = new int[] { 3, 4, 1, 2, 0 };
		for (int j = 0; j < depth.length; j++) {
			Assert.assertEquals(depth[j], it.next());
		}
		Assert.assertEquals(-1, it.next());
		Assert.assertEquals(-1, it.next());
		Assert.assertEquals(-1, it.next());

	}

}
