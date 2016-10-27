package ballester.gameworld.agent.actions;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentWorld;
import ballester.gameworld.physics.Dynamics;
import ballester.gameworld.physics.Geometry;
import ballester.gameworld.agent.AgentFilter;

/**
 * An Action, carried out by an actor (possibly to a target).
 * 
 * Actions have pre and completion conditions based on 'targetFilter' and a
 * 'targetRange'
 * 
 * Actions can act upon the target or the actor, can have 'dynamics'
 * 
 * 
 * @author hugoz
 *
 */
public class Action {

    final private static Logger logger = Logger.getLogger(Action.class);

    public static final double D_COLISION = 5;
    public static final double D_TOUCHING = D_COLISION + 5;
    public static final double D_VERYNEAR = 2 * D_TOUCHING;
    public static final double D_NEAR = 3 * D_VERYNEAR;
    public static final double D_FAR = 3 * D_NEAR;
    public static final double D_VERYFAR = 3 * D_FAR;

    public static final double TARGET_DISTANCE_NORMAL = D_FAR;

    public Agent actor; // parents
    public Agent target; // target (when needed)
    public Agent oldTarget;
    public AgentFilter[] targetFilter = new AgentFilter[0]; // filter to choose
							    // a target
    // (when needed)
    public Dynamics dynamics; // movement properties (when needed)
    public Double targetRange; // precondition range (when needed)
    public boolean reachInteractionDistance; // precondition range (when needed)

    public String display_action = "";

    transient AgentWorld world;

    public Action(AgentWorld world, Agent actor) {
	this(world, actor, null, null, null);
    }

    public Action(AgentWorld world, Agent actor, Agent target, AgentFilter[] filter, Double range) {
	this.world = world;
	this.actor = actor;
	this.target = target;
	this.targetFilter = filter;
	this.targetRange = range;
	this.reachInteractionDistance = true;
	this.display_action = this.getClass().getSimpleName().toLowerCase();
    }

    public Action(AgentWorld world, Agent actor, Agent target, AgentFilter[] filter) {
	this(world, actor, target, filter, null);
	this.reachInteractionDistance = true;
    }

    public void act() {
	actor.lastAction = this;
    }

    public boolean checkPrecondition() {
	return true;
    }

    /**
     * In an action sequence, this allows to go to the next
     * 
     * @return
     */
    public boolean checkCompletedCondition() {
	return false;
    }

    public String info1() {
	ArrayList<String> msg = new ArrayList<String>();
	msg.add((target != null ? target.name : "*"));
	for (AgentFilter t : targetFilter) {
	    msg.add((t != null ? t.getName() : "*"));
	}
	return "|" + this.getClass().getSimpleName() + " " + StringUtils.join(msg, ":") + "|";
    }

    public void chooseTarget() {

	oldTarget = target;
	target = null;
	for (AgentFilter t : targetFilter) {
	    Agent tmpTarget = t.findNearest(actor, world.getAgents(), Arrays.asList(new Agent[] { actor }));
	    if (tmpTarget != null) {
		if (targetRange != null && tmpTarget.point.distanceTo(actor.point) <= targetRange) {
		    target = tmpTarget;
		    break;
		} else if (reachInteractionDistance && withinDstanceOfInteraction(actor, tmpTarget)) {
		    target = tmpTarget;
		    break;
		}
	    }
	}

	if (logger.isDebugEnabled()) {
	    String h = "TARGET: " + actor.name + "; ";
	    if (oldTarget != target) {
		if (target == null) {
		    logger.debug(h + "lost all targets");
		} else {
		    logger.debug(h + "found new target: [" + target.name + "]");
		}
	    } else {
		logger.debug(h + "continued on target: [" + (target == null ? "-" : target.name) + "]");
	    }
	}
    }

    public boolean withinDstanceOfInteraction(Agent target, Agent actor) {
	return Geometry.touches(target, actor, 0);
    }

    boolean isNotFar(Agent target, Agent actor, double distance) {
	double d = target.point.distanceTo(actor.point);
	return d <= distance;
    }

    boolean isVeryNear(Agent target, Agent actor) {
	double d = target.point.distanceTo(actor.point);
	return d <= D_VERYNEAR;
    }

    boolean isNear(Agent target, Agent actor) {
	double d = target.point.distanceTo(actor.point);
	return d <= D_NEAR;
    }

    /**
     * @return
     */
    public boolean getDisplay_action_noise() {
	return display_action != null && display_action.length() > 0;
    }

}
