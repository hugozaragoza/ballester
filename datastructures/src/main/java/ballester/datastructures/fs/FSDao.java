/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.datastructures.fs;

import java.text.ParseException;
import java.util.Stack;

import org.apache.log4j.Logger;

/**
 * 
 * Roughly following http://www-01.sil.org/pcpatr/manual/pcpatr.html
 * 
 * Eventually may want to implement full
 * http://www.tei-c.org/release/doc/tei-p5-doc/en/html/FS.html
 * 
 * 
 * 
 * <pre>
 * 
 * [
 * 
 *   obj1 : [
 *   feature_name : feature_value
 *   ]
 * 
 *   obj2 : [
 *   class : prince
 *   HASA : [
 *     class: sword
 *     color: blue
 *   ]
 *   ] 
 * ]
 * </pre>
 * 
 * @author hugoz 2015
 *
 */
public class FSDao {
	final private static Logger logger = Logger.getLogger(FSDao.class);

	private static final String CLOSE = "]";
	private static final String OPEN = "[";

	public static FeatStruct parse(String worldInSSF) throws ParseException {
		// SemWorld w = null;
		String[] lines = worldInSSF.split("\n");
		int i = 0;
		FeatStruct ret = null;
		Stack<FeatStruct> parentStack = new Stack<FeatStruct>();

		while (i < lines.length) {
			String l = lines[i].trim();
			logger.trace("PARSE START L" + i + " |" + l + "|");
			if (l.length() > 0 && !l.startsWith("#")) {
				if (l.equals(CLOSE)) {
					parentStack.pop();
				} else if (l.startsWith(OPEN)) {
					FeatStruct fs = new FeatStruct();
					if (parentStack.size() == 0) { // FIRST
						ret = fs;
					}
					// w.add(new F("OBJ" + (objs++), fs));
					parentStack.push(fs);
				} else {
					String[] ll = l.split(":");
					logger.trace("PUT| " + l);
					String t = ll[1].trim();
					FSValue val = null;
					FeatStruct nameNode = parentStack.peek();
					if (t.equals(OPEN)) {
						FeatStruct fs = new FeatStruct();
						val = new FSValue(fs);
						parentStack.push(fs);
					} else {
						val = new FSValue(t);
					}
					nameNode.add(new Feature(ll[0].trim(), val));
				}
				logger.debug(ret + "\n-------\n");
			}
			i++;
		}
		return ret;
	}
}
