package ballester.gameworld.agent.actions;

import org.apache.log4j.Logger;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentWorld;
import ballester.gameworld.agent.AgentFilter;
import ballester.gameworld.agent.emotionalstate.EmotionalState;
import ballester.gameworld.physics.Dynamics;
import ballester.gameworld.physics.Point;

public class RunTowardsIfEmotional extends MoveToAction {

    final private static Logger logger = Logger.getLogger(RunTowardsIfEmotional.class);

    private EmotionalState triggerState;

    public RunTowardsIfEmotional(AgentWorld world, Agent actor, AgentFilter[] filter, EmotionalState triggerState,
	    double targetMaxRange, double toDistance) {
	super(world, actor, null, filter, targetMaxRange, toDistance);
	this.triggerState = triggerState;
	this.speedModifier = 2.0;
    }

    @Override
    public boolean checkPrecondition() {
	target = null;
	if (triggerState.getClass().isInstance(actor.emotionalState)) {
	    this.display_action = actor.emotionalState.getClass().getSimpleName().toLowerCase() + ", run!";
	    chooseTarget();
	}
	return target != null;
    }

}
