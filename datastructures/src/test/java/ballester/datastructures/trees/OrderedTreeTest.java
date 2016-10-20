package ballester.datastructures.trees;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ballester.datastructures.tree.OrderedTree;

public class OrderedTreeTest {
	static int nodes = 0;

	class Node {
		String name = "Node_" + (nodes++);

		@Override
		public String toString() {
			return name;
		}
	};

	@Test
	public void testGetChildren() {
		ArrayList<Integer> s;

		// 0 [ 1 [ 2 , 3 ]]
		Node[] nodes = new Node[10];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node();
		}
		int i = 0;
		OrderedTree<Node> tree = new OrderedTree<OrderedTreeTest.Node>(
				nodes[i++]);
		tree.addChild(0, nodes[i++]);
		tree.addChild(1, nodes[i++]);
		tree.addChild(1, nodes[i++]);

		s = tree.getChildren(0);
		List<Integer> des = Arrays.asList(new Integer[] { 1 });
		Assert.assertEquals(des, s);

		s = tree.getChildren(1);
		des = Arrays.asList(new Integer[] { 2, 3 });
		Assert.assertEquals(des, s);

		s = tree.getSiblings(1, false);
		des = Arrays.asList(new Integer[] { 1 });
		Assert.assertEquals(des, s);

		s = tree.getSiblings(1, true);
		des = Arrays.asList(new Integer[] {});
		Assert.assertEquals(des, s);

		s = tree.getSiblings(2, false);
		des = Arrays.asList(new Integer[] { 2, 3 });
		Assert.assertEquals(des, s);

		s = tree.getSiblings(2, true);
		des = Arrays.asList(new Integer[] { 3 });
		Assert.assertEquals(des, s);

	}

	@Test
	public void testTreeBuiliding() {

		Node[] nodes = new Node[10];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node();
		}
		int i = 0;
		OrderedTree<Node> tree = new OrderedTree<OrderedTreeTest.Node>(
				nodes[i++]);
		Assert.assertEquals(nodes[0], tree.getNode(0));
		Assert.assertEquals(0, tree.getRoot());
		Assert.assertEquals(0, tree.numOfChildren(0));
		Assert.assertEquals(-1, tree.getLastChild(0));
		Assert.assertTrue(tree.isRoot(0));
		Assert.assertFalse(tree.isFirstChild(0));
		Assert.assertFalse(tree.isLastChild(0));

		Assert.assertEquals(0, tree.getNodeIndex(nodes[0]));

		// 0 [ 1 [ 3] , 2 ]
		tree.addChild(0, nodes[i++]);
		tree.addChild(0, nodes[i++]);
		tree.addChild(1, nodes[i++]);

		for (int j = 0; j < i; j++) {
			Assert.assertEquals(nodes[j], tree.getNode(j));
		}
		Assert.assertEquals(0, tree.getRoot());

		Assert.assertEquals(0, tree.getNodeIndex(nodes[0]));
		Assert.assertEquals(1, tree.getNodeIndex(nodes[1]));
		Assert.assertEquals(2, tree.getNodeIndex(nodes[2]));
		Assert.assertEquals(3, tree.getNodeIndex(nodes[3]));
		Assert.assertFalse(1 == tree.getNodeIndex(nodes[3]));

		Assert.assertEquals(2, tree.numOfChildren(0));
		Assert.assertEquals(1, tree.numOfChildren(1));
		Assert.assertEquals(0, tree.numOfChildren(2));
		Assert.assertEquals(0, tree.numOfChildren(3));

		Assert.assertEquals(2, tree.getLastChild(0));
		Assert.assertEquals(3, tree.getLastChild(1));
		Assert.assertEquals(-1, tree.getLastChild(2));
		Assert.assertEquals(-1, tree.getLastChild(3));
		Assert.assertTrue(tree.isRoot(0));

		Assert.assertEquals(false, tree.isFirstChild(0));
		Assert.assertEquals(true, tree.isFirstChild(1));
		Assert.assertEquals(false, tree.isFirstChild(2));
		Assert.assertEquals(true, tree.isFirstChild(3));

		Assert.assertEquals(false, tree.isLastChild(0));
		Assert.assertEquals(false, tree.isLastChild(1));
		Assert.assertEquals(true, tree.isLastChild(2));
		Assert.assertEquals(true, tree.isLastChild(3));

		Assert.assertFalse(tree.isRoot(1));
	}

	@Test
	public void testPushUnder() {
		Node[] nodes = new Node[10];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node();
		}
		int i = 0;
		OrderedTree<Node> tree = new OrderedTree<OrderedTreeTest.Node>(
				nodes[i++]);

		// 0 [ 1 , 2 [ 3] ] --> 0 [2 [ 3 , 1 ]]
		tree.addChild(0, nodes[i++]);
		tree.addChild(0, nodes[i++]);
		tree.addChild(2, nodes[i++]);
		System.out.println(tree);
		tree.pushUnderSibling(1, 2);
		System.out.println(tree);

		for (int j = 0; j < i; j++) {
			Assert.assertEquals(nodes[j], tree.getNode(j));
		}
		Assert.assertEquals(0, tree.getRoot());

		Assert.assertEquals(1, tree.numOfChildren(0));
		Assert.assertEquals(0, tree.numOfChildren(1));
		Assert.assertEquals(2, tree.numOfChildren(2));
		Assert.assertEquals(0, tree.numOfChildren(3));

		Assert.assertEquals(2, tree.getLastChild(0));
		Assert.assertEquals(-1, tree.getLastChild(1));
		Assert.assertEquals(1, tree.getLastChild(2));
		Assert.assertEquals(-1, tree.getLastChild(3));
		Assert.assertTrue(tree.isRoot(0));

		Assert.assertEquals(false, tree.isFirstChild(0));
		Assert.assertEquals(false, tree.isFirstChild(1));
		Assert.assertEquals(true, tree.isFirstChild(2));
		Assert.assertEquals(true, tree.isFirstChild(3));

		Assert.assertEquals(false, tree.isLastChild(0));
		Assert.assertEquals(true, tree.isLastChild(1));
		Assert.assertEquals(true, tree.isLastChild(2));
		Assert.assertEquals(false, tree.isLastChild(3));
	}
}
