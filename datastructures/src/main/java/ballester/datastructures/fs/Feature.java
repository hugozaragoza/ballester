/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.datastructures.fs;

/**
 * Feature part of a Feature Structure
 * 
 * @author hugoz
 *
 */
public class Feature {
    private String name;
    FSValue value;

    public boolean isTerminal() {
	return value.isTerminal();
    }

    public Feature(String name, FSValue value) {
	super();
	this.setName(name);
	this.value = value;
    }

    public Feature(String name, Object value) {
	super();
	this.setName(name);
	this.value = new FSValue(value);
    }

    public Feature(String name, FeatStruct value) {
	this(name, new FSValue(value));
    }

    @Override
    public boolean equals(Object b) {
	Feature bb = (Feature) b;
	return (name.equals(bb.getName()) && value.equals(bb.getValue()));
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public FSValue getValue() {
	return value;
    }

    public void setValue(FSValue val) {
	this.value = val;
    }

    /**
     * @param otheF
     * @return
     */
    public boolean generalizes(Feature otheF) {
	if (otheF == null) {
	    return false;
	}
	if (this.getName().equals(otheF.getName())) {
	    return this.getValue().generalizes(otheF.getValue());
	} else {
	    return false;
	}
    }

    @Override
    public String toString() {
	String val = value.isTerminal() ? value.getTerminal().toString() : value.toString();
	return name + ":" + val;
    }

    /**
     * @return
     */
    public FeatStruct getFeatureStructure() {
	return getValue().getFeatureSet();
    }
}
