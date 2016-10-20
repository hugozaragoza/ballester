package ballester.gameworld.agent.actions;

import org.apache.log4j.Logger;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentWorld;
import ballester.gameworld.agent.AgentFilter;

public class Attack extends Action {

    final private static Logger logger = Logger.getLogger(Attack.class);

    /**
     * @param world
     * @param actor
     * @param target
     * @param filter
     */
    public Attack(AgentWorld world, Agent actor, Agent target, AgentFilter filter, double attackRange) {
	super(world, actor, null, new AgentFilter[] { filter }, attackRange);
    }

    @Override
    public boolean checkPrecondition() {
	chooseTarget();
	if (target != null) {
	    // CHOOSE WEAPON; RANGE; ETC:
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public void act() {
	super.act();
	if (target == null) {
	    logger.error("TARGET NULL??? SHOULD NOT HAVE PASSED THE PRECONDITION?!");
	    return;
	}
	// CHOOSE WEAPON; RANGE; ETC:
	target.transferHealth(actor, 0.5);
    }

}
