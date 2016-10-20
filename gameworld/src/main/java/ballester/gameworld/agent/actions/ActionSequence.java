package ballester.gameworld.agent.actions;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentWorld;

/**
 * Several actions can be put in an **ActionSequence** where the first action
 * failing its completion condition is tested for pre-conditions and fired if
 * possible.
 * 
 * (Composite pattern)
 * 
 * @author hugoz
 *
 */
public class ActionSequence extends Action {
    final private static Logger logger = Logger.getLogger(ActionSequence.class);

    public ActionSequence(AgentWorld world, Agent actor) {
	super(world, actor);
    }

    ArrayList<Action> sequence = new ArrayList<Action>();

    public Action chooseAction() {
	if (this.checkCompletedCondition() == true)
	    return null;
	if (this.checkPrecondition() == false)
	    return null;

	int i = 0;
	Action ret = null;
	logger.trace("SEQUENCE STATE chooseAction(): " + actor.name);

	for (Action b : sequence) {
	    logger.trace(" #" + (i++) + ": " + b.getClass().getSimpleName());
	    if (b instanceof ActionSequence) {
		ret = ((ActionSequence) b).chooseAction();
		if (ret != null) {
		    break;
		}
	    } else {
		if (!b.checkPrecondition()) { // also updated targets, etc.
		    continue;
		}
		boolean comp = b.checkCompletedCondition();
		logger.trace("     COMP:" + comp);
		if (!comp) {
		    boolean pre = b.checkPrecondition();
		    logger.trace("     PREC:" + pre);
		    if (pre) {
			logger.trace("     ACT: " + b.getClass().getSimpleName());
			ret = b;
			break;
		    }
		}
	    }
	}
	logger.trace("SEQUENCE STATE chooseAction(): " + actor.name + " CHOSE: " + (ret == null ? "null" : ret));

	return ret;
    }

    @Override
    public void act() {
	Action a = chooseAction();
	logger.debug("CHOOSE ACTION: Actor=[" + actor.getClass().getSimpleName() + "], Action=["
		+ (a == null ? "null" : a.getClass().getSimpleName()) + "]");
	if (a != null) {
	    if (a instanceof ActionSequence) {
		logger.error("CHOSE SEQUENCE!?");
		return;
	    }
	    a.act();
	}
    }

    @Override
    public boolean checkCompletedCondition() {
	if (sequence.size() == 0)
	    return true;
	return sequence.get(sequence.size() - 1).checkCompletedCondition();
    }

    public void add(Action b) {

	if (sequence == null) {
	    sequence = new ArrayList<Action>();
	}
	sequence.add(b);
    }

}
