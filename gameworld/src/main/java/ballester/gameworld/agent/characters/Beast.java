package ballester.gameworld.agent.characters;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentProperties;
import ballester.gameworld.agent.AgentWorld;
import ballester.gameworld.agent.Consumable;
import ballester.gameworld.agent.AgentFilter;
import ballester.gameworld.agent.FilterLivingAgentWithoutWeapon;
import ballester.gameworld.agent.actions.Action;
import ballester.gameworld.agent.actions.ActionSequence;
import ballester.gameworld.agent.actions.Bored;
import ballester.gameworld.agent.actions.Eat;
import ballester.gameworld.agent.actions.RunWhenThreatened;
import ballester.gameworld.agent.actions.RunTowardsIfEmotional;
import ballester.gameworld.agent.emotionalstate.Hungry;
import ballester.gameworld.agent.emotionalstate.ThreatenedBy;
import ballester.gameworld.agent.emotionalstate.Tranquil;

public class Beast extends LivingAgent {

    public Beast() {
	super();
	weapon = true;
	relativeSpeed = 2.0; // faster than normal
    }

    @Override
    protected void setSize() {
	setSizeBeast(2.0, 3.0);
    }

    @Override
    protected Action getAction() {
	// BEHAVIOUR

	ActionSequence bs = new ActionSequence(world, this);

	this.emotions.add(0, new Hungry());
	this.emotions.add(0, new ThreatenedBy(new AgentFilter(Beast.class, null), Action.D_NEAR));

	// Run away from enemy
	Action b = new RunWhenThreatened(world, this, -2.0);
	bs.add(b);

	// Eat something (go to food, eat it)
	ActionSequence b2 = new ActionSequence(world, this);
	AgentFilter f1 = new FilterLivingAgentWithoutWeapon();
	AgentFilter f2 = new AgentFilter(null, Consumable.class);
	AgentFilter[] fs = new AgentFilter[] { f1, f2 };
	b2.add(new RunTowardsIfEmotional(world, this, fs, new Hungry(), Action.D_VERYFAR, Action.D_TOUCHING));
	b2.add(new Eat(world, this, fs));
	bs.add(b2);

	// Wait
	Action b3 = new Bored(world, this);
	bs.add(b3);
	return bs;
    }

}
