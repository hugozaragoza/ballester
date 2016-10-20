package ballester.gameworld.agent.actions;

import org.apache.log4j.Logger;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentWorld;
import ballester.gameworld.agent.Consumable;
import ballester.gameworld.agent.AgentFilter;
import ballester.gameworld.agent.AgentInterface;
import ballester.gameworld.agent.emotionalstate.Hungry;

public class Eat extends Action {

    final private static Logger logger = Logger.getLogger(Eat.class);

    public Eat(AgentWorld world, Agent actor, AgentFilter[] filter) {
	super(world, actor, null, filter, Action.D_TOUCHING);
	this.display_action = "yummy!";
    }

    @Override
    public void act() {
	super.act();
	if (target == null) {
	    logger.error("SHOULD NOT HAVE PASSED THE PRECONDITION?!");
	    return;
	}
	target.transferHealth(actor, 1.0);

	if (!target.props.isDead()) {
	    world.narrator.narrate("$1 ate a piece of $2", actor, target);
	} else {
	    world.narrator.narrate("$1 finished $2", actor, target);
	}
    }

    @Override
    public boolean checkPrecondition() {
	boolean ok = false;
	oldTarget = target;
	if (this.actor.emotionalState instanceof Hungry) {
	    logger.debug("Eat: " + actor + " is hungry");
	    // always check new targets to eat (may be closer)
	    chooseTarget();
	    if (target != null && isTouching(target, actor)) {
		logger.debug("Eat: " + actor + " reached target: " + target);
		ok = true;
	    }
	} else {
	    logger.debug("Eat: " + actor + " not hungry");
	}
	return ok;
    }

    public static ActionSequence addEatActionSequence(AgentWorld world, Agent a, Class<? extends Agent> agentFilter,
	    Class<? extends AgentInterface> agentFilter2) {
	ActionSequence b2 = new ActionSequence(world, a);
	AgentFilter f2 = new AgentFilter(agentFilter, agentFilter2);
	b2.add(new RunTowardsIfEmotional(world, a, new AgentFilter[] { f2 }, new Hungry(), Action.D_FAR,
		Action.D_TOUCHING));
	b2.add(new Eat(world, a, new AgentFilter[] { f2 }));
	return b2;
    }

}
