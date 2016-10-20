package ballester.gameworld.agent.emotionalstate;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.text.WordUtils;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentInterface;
import ballester.gameworld.agent.AgentList;
import ballester.gameworld.agent.AgentWorld;
import ballester.gameworld.agent.AgentFilter;
import ballester.gameworld.agent.characters.Beast;

public class ThreatenedBy extends EmotionalState {
	double range;
	private AgentFilter filter;

	public ThreatenedBy(AgentFilter filter, double range) {
		this.range=range;
		this.filter = filter;
	}

	@Override
	public boolean preCondition(Agent agent, AgentWorld world) {
		target=null;
		ArrayList<Agent> as = filter.findActor(world.getAgents(), Arrays.asList(new Agent[] {agent}));
		AgentList.sortByDistance(as, true);
		if (as!=null) {			
		for (Agent a : as) {
				if (a.point.distanceTo(agent.point) <= range) {
					target=a;
					return true;				
				}
		}
		}
		return false;	
	}
	
	
}
