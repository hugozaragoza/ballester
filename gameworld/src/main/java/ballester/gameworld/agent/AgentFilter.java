package ballester.gameworld.agent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Selects an agent from a set based on different types of conditions
 * 
 * @author hugoz
 *
 */
public class AgentFilter {
    Agent agentPrototype;
    Class<? extends Agent> agentClass;
    Class<? extends AgentInterface> agentInterface;
    Double minRange;
    Double maxRange;

    public AgentFilter(Agent agentPrototype) {
	this(agentPrototype.getClass(), null);
	this.agentPrototype = agentPrototype;
    }

    public AgentFilter(Class<? extends Agent> agentClass, Class<? extends AgentInterface> agentInterface) {
	this.agentClass = agentClass;
	this.agentInterface = agentInterface;
    }

    public boolean test(Agent a) {
	boolean ok = true;
	if (agentClass != null) {
	    ok = ok && agentClass.isInstance(a);
	}
	if (agentInterface != null) {
	    ok = ok && agentInterface.isInstance(a);
	}
	if (agentPrototype != null) {
	    ok = ok && isContained(agentPrototype, a);
	}
	return ok;
    }

    /**
     * @param agentPrototype2
     * @param a
     * @return
     */
    private boolean isContained(Agent agentPrototype2, Agent a) {
	// TODO do this
	return a.getClass().equals(agentPrototype2.getClass());
    }

    public String getName() {
	return this.getClass().getSimpleName();
    }

    public Agent findFirstActor(List<Agent> agents, List<Agent> exclude) {
	return findFirstActor(agents, this, exclude);
    }

    public ArrayList<Agent> findActor(List<Agent> agents, List<Agent> exclude) {
	return findActor(agents, this, exclude);
    }

    // ---------------------------------------------------------------
    // FUNCTION LIBRARY:
    // ---------------------------------------------------------------

    public static ArrayList<Agent> findActor(List<Agent> agents, AgentFilter filter, Collection<Agent> excludeList) {
	ArrayList<Agent> ret = new ArrayList<Agent>();
	for (Agent a : agents) {
	    if (filter.test(a)) {
		if (excludeList == null || !excludeList.contains(a))
		    ret.add(a);
	    }
	}
	return ret;
    }

    public Agent findNearest(Agent source, List<Agent> agents, List<Agent> excludeList) {
	if (source.point == null) {
	    return null;
	}
	ArrayList<Agent> ret = findActor(agents, this, excludeList);
	Double minD = null;
	Agent aret = null;
	for (Agent a : ret) {
	    if (a.point == null) {
		continue;
	    }
	    double d = source.point.distanceTo(a.point);
	    if (minD == null || minD > d) {
		minD = d;
		aret = a;
	    }
	}
	return aret;
    }

    public static Agent findFirstActor(List<Agent> agents, AgentFilter filter, Collection<Agent> excludeList) {
	ArrayList<Agent> ret = findActor(agents, filter, excludeList);
	if (ret != null && ret.size() > 0)
	    return ret.get(0);
	else
	    return null;
    }

    public static ArrayList<Agent> findActor(List<Agent> agents, AgentFilter filter) {
	return AgentFilter.findActor(agents, filter, null);
    }

}
