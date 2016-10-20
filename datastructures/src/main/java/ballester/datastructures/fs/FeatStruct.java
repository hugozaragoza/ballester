/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.datastructures.fs;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import ballester.datastructures.tree.OrderedTree;
import ballester.datastructures.tree.OrderedTreeIterator;
import ballester.datastructures.tree.OrderedTreeIterator.Order;

/**
 * Implementation of a Feature Structure (FS)
 * 
 * Simple preliminary implementation, missing many things...
 * 
 * Definitions:
 * <ul>
 * <li>http://en.wikipedia.org/wiki/Feature_structure
 * <li>http://www-01.sil.org/pcpatr/manual/pcpatr.html#sec1.2
 * </ul>
 * 
 * Implementation: we use an ordered tree.
 * 
 * @author hugoz 2005-2015
 *
 */
public class FeatStruct {
    final private static Logger logger = Logger.getLogger(FeatStruct.class);
    private static Gson gson = new Gson(); // FIXME using as object copier for
					   // now, improve this.

    private OrderedTree<FSValue> tree; // implemented as an ordered tree

    public FeatStruct() {
	tree = new OrderedTree<FSValue>(new FSValue("root"));
    }

    public HashSet<String> getFetaureNames() {
	ArrayList<Integer> lis = tree.getChildren(tree.getRoot());
	HashSet<String> ret = new HashSet<String>(lis.size());
	for (Integer i : lis) {
	    String name = (String) tree.getNode(i).getTerminal();
	    ret.add(name);
	}
	return ret;
    }

    public Object getFeatureTerminalValue(String name) {
	Feature f = getFeature(name);
	if (f == null)
	    return null;
	if (!f.getValue().isTerminal()) {
	    throw new IndexOutOfBoundsException("non-terminal feature value reached");
	}
	return f.getValue().getTerminal();
    }

    public Feature getFeature(int nameNode) {
	int valueNode = tree.getFirstChild(nameNode);
	return new Feature((String) tree.getNode(nameNode).getTerminal(), tree.getNode(valueNode));
    }

    public boolean containsFeature(String name) {
	return (this.getFeature(name) != null);
    }

    private int _findIndex(String featureName) {
	for (Integer i : tree.getChildren(tree.getRoot())) {
	    if (tree.getNode(i).getTerminal().equals(featureName)) {
		return i;
	    }
	}
	return -1;
    }

    public Feature getFeature(String name) {
	int i = _findIndex(name);
	if (i < 0)
	    return null;
	int j = tree.getFirstChild(i);
	return new Feature((String) tree.getNode(i).getTerminal(), tree.getNode(j));
    }

    public int size() {
	return tree.size() - 1; // discount root
    }

    public void add(Feature feature) {
	add(feature, false);
    }

    /**
     * Check if can be added and add
     * 
     * @param feature
     */
    public void add(Feature feature, boolean removeExisting) {
	String name = feature.getName();
	if (this.containsFeature(name)) {
	    if (!this.getFeature(name).equals(feature)) {
		if (removeExisting) {
		    remove(name);
		    _add(feature);
		} else {
		    throw (new IndexOutOfBoundsException("inconsistent feature add: [" + name
			    + "] already exists with value [" + this.getFeature(name) + "]"));
		}
	    }
	} else {
	    _add(feature);
	}
    }

    /**
     * override or add
     * 
     * @param feature
     */
    public void set(Feature feature) {
	String name = feature.getName();
	remove(name);
	_add(feature);
    }

    /**
     * internal tree implementation of add. Strict add: does not check for
     * repetitions etc.
     * 
     * @param feature
     */
    private void _add(Feature feature) {
	int j = tree.addChild(tree.getRoot(), new FSValue(feature.getName()));
	tree.addChild(j, feature.value);
    }

    public boolean remove(String featureName) {
	int i = _findIndex(featureName);
	if (i < 0)
	    return false;
	int j = tree.getFirstChild(i);
	if (tree.getFirstChild(j) > 0) {
	    throw (new IndexOutOfBoundsException("cannot remove non-atomic nodes (NOT IMPLEMENTED YET)"));
	}
	tree.remove(j); // value first
	tree.remove(i); // then name
	return true;
    }

    public Feature get(int i) {
	int j = (i * 2 - 1);
	return new Feature((String) tree.getNode(j).getTerminal(), tree.getNode(j + 1));
    }

    @Override
    public String toString() {
	return toString(0, true);
    }

    public String toString(int parentDepth, boolean paintNodeIndices) {
	StringBuffer so = new StringBuffer();

	OrderedTreeIterator<FSValue> it = new OrderedTreeIterator<FSValue>(tree, Order.PREORDER);
	int i = -1;
	it.next(); // skip root;
	so.append(tab(parentDepth, 0, paintNodeIndices) + "[\n");
	do {
	    i = it.next();
	    if (i < 0)
		break;

	    String name = (String) tree.getNode(i).getTerminal();
	    int depth = tree.getDepth(i);
	    so.append(tab(parentDepth, depth, paintNodeIndices));
	    so.append(name);
	    so.append(" : ");
	    i = it.next();

	    FSValue value = tree.getNode(i);
	    if (value.isTerminal()) {
		so.append(value.getTerminal());
		so.append("\n");
	    } else {
		String s = value.getFeatureSet().toString(parentDepth + 1, paintNodeIndices);
		so.append("\n" + s);
	    }
	} while (true);
	so.append(tab(parentDepth, 0, paintNodeIndices) + "]\n");
	return so.toString();
    }

    public static String tab(int parentDepth, int depth, boolean paintNodeIndices) {
	// return StringUtils.repeat(".",(depth+1))+
	// StringUtils.repeat(" ",4*(depth+1));
	String ni = paintNodeIndices ? ("L" + parentDepth + "." + depth + "|\t") : "";
	return ni + StringUtils.repeat(" ", 4 * (parentDepth) + 2 * depth + 1);
    }

    /**
     * //FIXME add cases
     * 
     * @param fs
     * @return
     */
    public boolean generalizes(FeatStruct fs) {
	HashSet<String> thisN = this.getFetaureNames();
	for (String s : thisN) {
	    Feature thisF = this.getFeature(s);
	    Feature otheF = fs.getFeature(s);
	    if (!thisF.generalizes(otheF))
		return false;
	}
	return true;

    }

    /**
     * Adds all features, overrriding pre-existing ones with same name
     * 
     * @param semmantics
     */
    public void add(FeatStruct semmantics) {
	for (Feature f : semmantics.getFeatures()) {
	    add(f, true);
	}

    }

    /**
     * @return
     */
    private ArrayList<Feature> getFeatures() {
	HashSet<String> ss = getFetaureNames();
	ArrayList<Feature> ret = new ArrayList<Feature>();
	for (String s : ss) {
	    ret.add(getFeature(s));
	}
	return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     * 
     * FIXME: implement propertly
     */
    @Override
    public boolean equals(Object o) {
	if (!(o instanceof FeatStruct)) {
	    return false;
	}
	FeatStruct other = (FeatStruct) o;
	return other.toString().equals(this.toString());

    }

    /**
     * @param string
     * @return
     */
    public boolean isTrue(String featureName) {
	try {
	    Boolean ret = (Boolean) getFeature(featureName).getValue().getTerminal();
	    return ret;
	} catch (Exception e) {
	    return false;
	}
    }

    public void set(String featureName, Boolean bool) {
	if (bool) {
	    set(new Feature(featureName, true));
	} else {
	    set(new Feature(featureName, false));
	}
    }

    /**
     * FIXME: using Gson for now, Kryo copy did not do full deep copy
     * 
     * @return
     */
    public FeatStruct copy() {
	return gson.fromJson(gson.toJson(this), FeatStruct.class);
    }

}
