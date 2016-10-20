/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.gameworld.agent;

import ballester.gameworld.agent.characters.LivingAgent;

/**
 * @author hugoz
 *
 */
public class FilterLivingAgentWithoutWeapon extends AgentFilter {

    /**
     * @param agentPrototype
     */
    public FilterLivingAgentWithoutWeapon() {
	super(LivingAgent.class, null);
    }

    @Override
    public boolean test(Agent a) {
	return !a.weapon && super.test(a);
    }

}
