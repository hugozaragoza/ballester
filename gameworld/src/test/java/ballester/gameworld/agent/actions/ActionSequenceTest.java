/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.gameworld.agent.actions;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentWorld;
import ballester.gameworld.agent.Consumable;
import ballester.gameworld.agent.AgentFilter;
import ballester.gameworld.agent.characters.Candy;
import ballester.gameworld.agent.characters.LivingAgent;
import ballester.gameworld.agent.emotionalstate.EmotionalState;
import ballester.gameworld.agent.emotionalstate.Hungry;
import ballester.gameworld.physics.HPoint;
import ballester.gameworld.physics.Point;

/**
 * @author hugoz
 *
 */
public class ActionSequenceTest {

    {
	Logger.getLogger(ActionSequence.class).setLevel(Level.TRACE);
    }

    AgentWorld world;
    Agent agent, agent2;
    Action a1, a2;
    ActionSequence seq;

    public void init() {
	world = new AgentWorld();

	agent = new Agent();
	world.createAgent(agent, true);
	agent.point = new HPoint(0., 0.);

	agent2 = new Candy();
	world.createAgent(agent2, true);
	agent2.point = new HPoint(100., 0.);

	Candy agent3 = new Candy();
	world.createAgent(agent3, true);
	agent3.point = new HPoint(200., 0.);

	AgentFilter f2 = new AgentFilter(null, Consumable.class);

	seq = new ActionSequence(world, agent);
	a1 = new RunTowardsIfEmotional(world, agent, new AgentFilter[] { f2 }, new Hungry(), 100., Action.D_TOUCHING);
	seq.add(a1);
	a2 = new Eat(world, agent, new AgentFilter[] { f2 });
	seq.add(a2);
    }

    @Test
    public void testActions() {
	init();
	// First without sequence:
	agent.emotionalState = new EmotionalState();
	Assert.assertFalse(a1.checkPrecondition());
	agent.emotionalState = new Hungry();
	Assert.assertTrue(a1.checkPrecondition());
    }

    @Test
    public void testSequence() {
	init();

	// none
	agent.emotionalState = new EmotionalState();
	Action a = seq.chooseAction();
	Assert.assertNull(a);

	// acquire target and run to it
	agent.emotionalState = new Hungry();
	a = seq.chooseAction();
	Assert.assertEquals(a1, a);
	a = seq.chooseAction();
	Assert.assertEquals(a1, a);

	// once first action completed goes to second
	agent.point = new HPoint(agent2.point);
	a = seq.chooseAction();
	Assert.assertEquals(a2, a);

	// if first action becomes not completed moves to first, then second
	// again:
	agent.point.add(new Point(10., 0.));
	a = seq.chooseAction();
	Assert.assertEquals(a1, a);
	agent.point.add(new Point(-10., 0.));
	a = seq.chooseAction();
	Assert.assertEquals(a2, a);

	// if target is lost, first action kicks in again:
	world.getAgents().remove(agent2);
	a = seq.chooseAction();
	Assert.assertEquals(a1, a);

	// if first action's pre.condition goes to false, sequence stops
	agent.emotionalState = null;
	a = seq.chooseAction();
	Assert.assertNull(a);

    }

}
