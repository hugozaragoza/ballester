package ballester.gameworld.control;

import java.util.ArrayList;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentWorld;

public interface Script {

    /**
     * @param world
     * @param epoch
     *            0 is init, 1 is first...
     * @return
     */
    public void addCharacters(AgentWorld world, int epoch);

}
