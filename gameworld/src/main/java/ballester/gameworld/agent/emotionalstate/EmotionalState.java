package ballester.gameworld.agent.emotionalstate;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentWorld;

/**
 * Agents can have an *EmotionalState* depending on the environment and their
 * own state : hungry, afraid, scared, agressive...
 * 
 * An *EmotionalStateSequence* can be used to prioritize emotional states: the
 * first checking its precondition is activated.
 * 
 * @author hugoz
 *
 */
public class EmotionalState {

	public Agent target;

	public boolean preCondition(Agent agent, AgentWorld world) {
		return true;
	}

}
