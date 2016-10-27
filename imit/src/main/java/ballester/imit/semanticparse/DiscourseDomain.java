/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.imit.semanticparse;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentWorld;

/**
 * @author hugoz
 *
 */
public class DiscourseDomain {

    LinkedList<Agent> referentsLast = new LinkedList<>();
    AgentWorld world;

    public DiscourseDomain(AgentWorld w) {
	this.world = w;
    }

    public List<Agent> getReferentsLast() {
	cleanup();
	return referentsLast;
    }

    private void cleanup() {

	// REMOVE DEAD AGENTS:
	ListIterator<Agent> li = referentsLast.listIterator();
	while (li.hasNext()) {
	    Agent a = li.next();
	    if (!world.getAgents().contains(a)) {
		li.remove();
	    }
	}

    }

    /**
     * last in first out
     */
    public void addReferent(Agent o) {
	if (referentsLast.contains(o)) {
	    referentsLast.remove(o);
	}
	referentsLast.addFirst(o);
    }

    public void clear() {
	referentsLast.clear();
    }
}
