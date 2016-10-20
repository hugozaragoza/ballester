/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.gameworld.agent;

/**
 * @author hugoz
 *
 */
public class GarphicInfo {

    public boolean drawTitle = true;

    public enum Shape {
	RECT, CIRCL
    }

    public Shape shape = Shape.RECT;

}
