/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.gameworld.agent;

import org.junit.Assert;
import org.junit.Test;

import ballester.gameworld.agent.AgentProperties.PropName;
import ballester.gameworld.agent.characters.Dragon;
import ballester.gameworld.agent.characters.Princess;

/**
 * @author hugoz
 *
 */
public class AgentWorldTest {

	@Test
	public void test_AgentInit() {

		AgentWorld w = new AgentWorld();
		Dragon d1 = new Dragon();
		d1.createInWorld(w);
		Princess d2 = new Princess();
		d2.createInWorld(w);

		Assert.assertNotEquals(d1.props.getDouble(PropName.SIZEH), d2.props.getDouble(PropName.SIZEH));

	}
}
