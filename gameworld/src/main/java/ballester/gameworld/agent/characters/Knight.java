/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.gameworld.agent.characters;

import ballester.gameworld.agent.AgentFilter;
import ballester.gameworld.agent.Consumable;
import ballester.gameworld.agent.actions.Action;
import ballester.gameworld.agent.actions.ActionSequence;
import ballester.gameworld.agent.actions.Bored;
import ballester.gameworld.agent.actions.Eat;
import ballester.gameworld.agent.actions.RunWhenThreatened;
import ballester.gameworld.agent.actions.RunTowardsIfEmotional;
import ballester.gameworld.agent.emotionalstate.Hungry;
import ballester.gameworld.agent.emotionalstate.ThreatenedBy;

/**
 * @author hugoz
 *
 */
public class Knight extends Beast {

    @Override
    protected Action getAction() {
	// BEHAVIOUR

	ActionSequence bs = new ActionSequence(world, this);

	this.emotions.add(0, new Hungry());
	this.emotions.add(0, new ThreatenedBy(new AgentFilter(Beast.class, null), Action.D_FAR));

	// Run away from enemy
	Action b = new RunWhenThreatened(world, this, 2.0);
	bs.add(b);

	// Eat something (go to food, eat it)
	ActionSequence b2 = Eat.addEatActionSequence(world, this, null, Consumable.class);
	bs.add(b2);

	// Wait
	Action b3 = new Bored(world, this);
	bs.add(b3);
	return bs;
    }

}
