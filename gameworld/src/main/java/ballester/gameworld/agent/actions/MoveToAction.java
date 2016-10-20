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

    double speedModifier = 3.0;
    double toDistance = Action.D_TOUCHING;

    public MoveToAction(AgentWorld world, Agent actor, Agent target, AgentFilter[] filter, Double targetRange,
	    Double toDistance) {
	super(world, actor, target, filter, targetRange);
	this.toDistance = toDistance;
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
	    if (isNear(target, actor)) {
		speed /= 2;
	    }
	    if (isVeryNear(target, actor)) {
		speed = 1.0;
	    }
	    dynamics = new Dynamics(v.norm().scale(speed), new Point(0.0, 0.0));
	    actor.world.physics.move(actor.point, dynamics);
	}
    }

    @Override
    public boolean checkCompletedCondition() {
	if (target == null)
	    return false;

	boolean completed = isNotFar(target, actor, toDistance);
	return completed;
    }

}
