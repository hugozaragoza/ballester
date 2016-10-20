/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.semgame.mapper;

import org.junit.Assert;
import org.junit.Test;

import ballester.datastructures.fs.FeatStruct;
import ballester.datastructures.fs.Feature;
import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentProperties;
import ballester.gameworld.agent.characters.Dragon;
import ballester.gameworld.agent.characters.Princess;
import ballester.gameworld.agent.emotionalstate.EmotionalState;
import ballester.gameworld.agent.emotionalstate.Hungry;
import ballester.grammar.simplegrammar.types.WordTree;
import ballester.imit.mapper.SemWorldMappper;

/**
 * @author hugoz
 *
 */
public class SemWorldMapperTest {

	@Test
	public void testMapToFeatStruct() throws Exception {
		Princess p = new Princess();
		p.props.set(AgentProperties.PropName.COLOR, "green");
		FeatStruct fs = SemWorldMappper.mapAgentToFS(p);
		FeatStruct des = new FeatStruct();
		des.add(new Feature("type", "princess"));
		des.add(new Feature("color", "green"));
		Assert.assertEquals(fs, des);
	}

	@Test
	public void testMapToAgent() throws Exception {

		FeatStruct fs = new FeatStruct();
		fs.add(new Feature("type", "dragon"));
		fs.add(new Feature("color", "green"));

		Agent a = SemWorldMappper.mapFSToAgent(fs);
		Assert.assertNotNull(a);
		Assert.assertTrue(a instanceof Dragon);
		Assert.assertEquals("green", a.props.getColor());

	}

	@Test
	public void testEmmotionalStateFromLemma() throws Exception {
		EmotionalState act = SemWorldMappper.emmotionalStateFromLemma("hungry");
		EmotionalState des = new Hungry();
		Assert.assertEquals(act.getClass(), des.getClass());
	}

}
