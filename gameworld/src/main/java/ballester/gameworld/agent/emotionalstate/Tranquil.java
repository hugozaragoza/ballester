package ballester.gameworld.agent.emotionalstate;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentWorld;
import ballester.gameworld.agent.Consumable;
import ballester.gameworld.agent.AgentFilter;

public class Tranquil extends EmotionalState {
	
	
	public Tranquil() {
	}
	
	public boolean preCondition(Agent agent, AgentWorld world) {
			return true;			
	}


}
