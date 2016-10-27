package ballester.gameworld.agent.emotionalstate;

import org.apache.log4j.Logger;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentProperties;
import ballester.gameworld.agent.AgentProperties.PropName;
import ballester.gameworld.agent.AgentWorld;
import ballester.gameworld.agent.actions.Eat;

public class Hungry extends EmotionalState {
    final private static Logger logger = Logger.getLogger(Hungry.class);

    private static final double HungerTriggerRatio = 0.5;

    public Hungry() {
    }

    @Override
    public boolean preCondition(Agent agent, AgentWorld world) {
	Double h = agent.props.getDouble(PropName.HEALTH) / agent.props.getDouble(PropName.HEALTH_NORMAL);
	return h <= HungerTriggerRatio;
    }

}
