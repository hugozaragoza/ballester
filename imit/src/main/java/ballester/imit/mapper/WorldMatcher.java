/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.imit.mapper;

import java.util.ArrayList;
import java.util.List;

import ballester.datastructures.fs.FeatStruct;
import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentProperties;
import ballester.imit.semanticparse.DiscourseDomain;

/**
 * @author hugoz
 *
 */
public class WorldMatcher {

    public static Agent find(Agent a, DiscourseDomain references, ArrayList<Agent> all) {
	Agent ret = find(a, references.getReferentsLast());
	if (ret == null) {
	    ret = find(a, all);
	}
	return ret;
    }

    public static Agent find(Agent a, List<Agent> agents) {
	for (Agent i : agents) {
	    if (isContainedIn(a, i)) {
		return i;
	    }
	}
	return null;
    }

    private static boolean isContainedIn(Agent a, Agent inB) {
	FeatStruct fs1 = SemWorldMappper.mapAgentToFS(a);
	FeatStruct fs2 = SemWorldMappper.mapAgentToFS(inB);
	return fs1.generalizes(fs2);
    }

}
