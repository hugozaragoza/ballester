package ballester.gameworld.agent;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import ballester.gameworld.agent.AgentProperties.PropName;
import ballester.gameworld.agent.actions.Action;
import ballester.gameworld.agent.actions.ActionSequence;
import ballester.gameworld.agent.emotionalstate.EmotionalState;
import ballester.gameworld.agent.emotionalstate.Tranquil;
import ballester.gameworld.examples.GameWorldDebug;
import ballester.gameworld.physics.HPoint;

/**
 * 
 * An agent is defined has:
 * <ul>
 * <li>a point in space</li>
 * <li>a vector of agent statistics</li>
 * <li>a state which can trigger a number of behaviours</li>
 * <li>a behavior tree</li>
 * <li>a character state</li>
 * </ul>
 * 
 * Agents are fully initialised only when createInWorld()
 * 
 * 
 * @author hugoz
 *
 */
public class Agent {
    final private static Logger logger = Logger.getLogger(Agent.class);
    public static int agents = 0;

    public GarphicInfo graphicInfo = new GarphicInfo();

    public String name = "AnAgent";

    // Physical position
    public HPoint point = new HPoint(0, 0);

    // Possible Actions
    public Action action;
    public ArrayList<EmotionalState> emotions = new ArrayList<EmotionalState>();
    // ACTIONS AND DYNAMICS:
    protected boolean weapon = false; // TODO make a weapons system
    public double relativeSpeed = 0.5;
    public double minSpeed = relativeSpeed;

    // Current State
    public EmotionalState emotionalState;
    public AgentProperties props = new AgentProperties();

    private Inventory inventory = new Inventory();
    private Agent inInventoryOf;

    // External
    public transient AgentWorld world;
    public transient Action lastAction = null;

    private static final double RATIO = 1.61803398875;
    private static final double UNITSIZE = 20;

    /**
     * use only for reflection and serialization
     */
    public Agent() {
    }

    /**
     * Initiate Agent in this world and add to it
     * 
     * @param world
     */
    public void createInWorld(AgentWorld world) {
	this.world = world;
	agents++;
	this.name = newName();
	this.action = getAction();
	this.props.inc(AgentProperties.PropName.HEALTH_NORMAL, 20.0);
	this.props.inc(AgentProperties.PropName.HEALTH, 20.0);
	setSize();
    }

    protected Action getAction() {
	return new ActionSequence(world, null);
    }

    private String newName() {
	String prefix = this.getClass().getSimpleName();
	return prefix + "_" + (agents);
    }

    public void updateEmotionalState() {

	// ORGANIC emotional state
	for (EmotionalState es : emotions) {
	    boolean s = es.preCondition(this, world);
	    if (s) {
		if (!es.equals(emotionalState)) {
		    world.narrator.narrate("$1 became $2", this, es);
		}
		emotionalState = es;

		break;
	    }
	}

    }

    public void updateStats() {
    }

    public void transferHealth(Agent actor, double d) {
	if (actor != null) {
	    this.props.inc(AgentProperties.PropName.HEALTH, -d);
	    actor.props.inc(AgentProperties.PropName.HEALTH, d);
	}

    }

    /**
     * 
     */
    public void resetState() {
	emotionalState = null;

    }

    /**
     * @param a2
     */
    public void addToInventory(Agent a2) {
	a2.inInventoryOf = this;
	a2.point = null;
	inventory.add(a2);
    }

    /**
     * @return
     */
    public boolean isInInventory() {
	return inInventoryOf != null;
    }

    /**
     * @return
     */
    public Inventory getInventory() {
	return inventory;
    }

    /**
     * @return
     */
    public Agent inInventoryOf() {
	return inInventoryOf;
    }

    protected void setSize() {
	setSizeBeast(1.0, 1.0);
    }

    protected void setSizePerson() {
	double h = UNITSIZE;
	props.set(PropName.SIZEH, h);
	props.set(PropName.SIZEW, h / RATIO);
    }

    protected void setSizeBeast(double relSizeH, double relSizeW) {
	props.set(PropName.SIZEH, UNITSIZE * relSizeH);
	props.set(PropName.SIZEW, UNITSIZE * relSizeW);
    }

    /**
     * Run for every agent at the end of turn
     */
    public void endOfTurn() {
    }
}
