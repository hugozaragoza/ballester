package ballester.gameworld.agent.actions;

import org.apache.log4j.Logger;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentWorld;
import ballester.gameworld.agent.AgentFilter;
import ballester.gameworld.agent.emotionalstate.EmotionalState;
import ballester.gameworld.agent.emotionalstate.ThreatenedBy;
import ballester.gameworld.examples.GameWorldDebug;
import ballester.gameworld.physics.Dynamics;
import ballester.gameworld.physics.Point;

public class RunWhenThreatened extends MoveToAction {

    final private static Logger logger = Logger.getLogger(RunWhenThreatened.class);

    public RunWhenThreatened(AgentWorld world, Agent actor, double speedModifier) {
	super(world, actor);
	this.speedModifier = speedModifier;
	if (speedModifier < 0) {
	    this.display_action = "run away!";
	} else {
	    this.display_action = "you don't scare me!";
	}
    }

    @Override
    public boolean checkCompletedCondition() {
	return (!(actor.emotionalState instanceof ThreatenedBy));
    }

    @Override
    public boolean checkPrecondition() {

	if (actor.emotionalState instanceof ThreatenedBy) {
	    ThreatenedBy tb = (ThreatenedBy) actor.emotionalState;
	    tb.preCondition(actor, world);
	    if (tb.target != null)
		this.target = tb.target;
	    return (tb.target != null);
	}
	return false;
    }

}
