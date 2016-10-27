package ballester.gameworld.agent.actions;

import org.apache.log4j.Logger;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentWorld;
import ballester.gameworld.agent.AgentFilter;
import ballester.gameworld.agent.emotionalstate.EmotionalState;
import ballester.gameworld.agent.emotionalstate.ThreatenedBy;
import ballester.gameworld.examples.GameWorldDebug;
import ballester.gameworld.physics.Dynamics;
import ballester.gameworld.physics.Physics;
import ballester.gameworld.physics.Point;

public class MoveToAction extends Action {
    final private static Logger logger = Logger.getLogger(MoveToAction.class);

    double speedModifier = 3.0;

    public MoveToAction(AgentWorld world, Agent actor, Agent target, AgentFilter[] filter, double targetMaxRange) {
	super(world, actor, target, filter, targetMaxRange);
    }

    @Override
    public String info1() {
	return super.info1() + " (" + (target != null ? target.name : "null") + ")";
    }

    public MoveToAction(AgentWorld world, Agent actor) {
	super(world, actor);
	// dynamics are given on act()
    }

    @Override
    public void act() {
	super.act();
	if (target != null) {
	    Point v = new Point(target.point).minus(actor.point);
	    double speed = actor.relativeSpeed * speedModifier;
	    String h = ("AGENT " + actor.name);
	    if (isNear(target, actor)) {
		logger.debug(h + " isNear " + target.name);
		speed /= 2;
	    }
	    if (isVeryNear(target, actor)) {
		logger.debug(h + " isVeryNear " + target.name);
		speed = 1.0 * Math.signum(speed);
	    }
	    v.norm().scale(speed);
	    logger.debug(h + " SPEED: " + speed + ", v=" + v);
	    dynamics = new Dynamics(v, new Point(0.0, 0.0));
	    actor.move(dynamics);

	}
    }

    @Override
    public boolean checkCompletedCondition() {
	if (target == null)
	    return false;

	boolean completed = withinDstanceOfInteraction(target, actor);
	return completed;
    }

}
