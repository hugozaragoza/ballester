/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.semgame.mapper;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import ballester.datastructures.fs.FeatStruct;
import ballester.datastructures.fs.Feature;
import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentProperties;
import ballester.gameworld.agent.AgentProperties.PropName;
import ballester.gameworld.agent.characters.Dragon;
import ballester.gameworld.agent.characters.Princess;
import ballester.imit.mapper.SemWorldMappper;
import ballester.imit.mapper.WorldMatcher;

/**
 * @author hugoz
 *
 */
public class WorldMatcherTest {

	@Test
	public void findAgent() {
		Agent act;

		Agent a1 = new Princess();
		a1.props.set(PropName.COLOR, "red");

		Agent a2 = new Princess();
		a2.props.set(PropName.COLOR, "green");

		Agent a3 = new Dragon();
		ArrayList<Agent> lis = new ArrayList<>();
		lis.add(a1);
		lis.add(a2);
		lis.add(a3);

		act = WorldMatcher.find(a2, lis);
		Assert.assertTrue(act instanceof Princess);
		Assert.assertEquals("green", act.props.getString(PropName.COLOR));

		act = WorldMatcher.find(a3, lis);
		Assert.assertTrue(act instanceof Dragon);
		Assert.assertNull(act.props.getString(PropName.COLOR));

	}

}
