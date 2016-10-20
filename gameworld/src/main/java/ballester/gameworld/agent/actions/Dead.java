package ballester.gameworld.agent.actions;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentProperties.PropName;
import ballester.gameworld.agent.AgentWorld;

public class Dead extends Action {

    public Dead(AgentWorld world, Agent actor) {
	super(world, actor);
    }

    @Override
    public void act() {
	super.act();
	world.narrator.narrate("The $1 died...", actor, null);
	actor.props.set(PropName.HEALTH, 0.0);
    }

    @Override
    public boolean checkCompletedCondition() {
	return (actor.props.getHealth() <= 0.0);
    }

}
