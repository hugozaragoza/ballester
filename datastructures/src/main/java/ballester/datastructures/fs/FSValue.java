/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.datastructures.fs;

import ballester.datastructures.Copyable;

/**
 * (Composite pattern)
 * 
 * @author hugoz
 *
 */
public class FSValue {

	private Object value1; // either a primitive
	private FeatStruct value2; // or an FS

	public boolean isTerminal() {
		return value1 != null;
	}

	/**
	 * @return String label value, null is non-terminal node.
	 */
	public Object getTerminal() {
		return value1;
	}

	public FSValue(Object value) {
		value2 = null;
		value1 = value;
	}

	public FSValue(FeatStruct value) {
		value2 = value;
		value1 = null;
	}

	@Override
	public String toString() {
		if (value1 != null)
			return value1.toString();
		return "[\n" + value2.toString() + "]\n";
	}

	public FeatStruct getFeatureSet() {
		return value2;
	}

	public boolean generalizes(FSValue b) {
		if (b.isTerminal()) {
			return (this.value1.equals(b.value1));
		} else {
			return this.value2.generalizes(b.value2);
		}
	}

	@Override
	public boolean equals(Object b) {
		FSValue bb = (FSValue) b;
		boolean ret = true;

		if (bb.value1 != null) {
			ret &= bb.value1.equals(this.value1);
		} else if (value1 != null) {
			ret = false;
		}

		if (bb.value2 != null) {
			ret &= bb.value2.equals(this.value2);
		} else if (value2 != null) {
			ret = false;
		}
		return ret;
	}

}
