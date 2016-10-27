package ballester.gameworld.agent.actions;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentWorld;

public class Bored extends Action {

    public Bored(AgentWorld world, Agent actor) {
	super(world, actor);
    }

    @Override
    public void act() {
	super.act();
	world.narrator.narrate("The $1 was bored...", actor, null);
    }

    @Override
    public boolean checkCompletedCondition() {
	return false;
    }

}
