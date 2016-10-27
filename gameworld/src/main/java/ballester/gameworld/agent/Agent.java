package ballester.gameworld.agent;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import ballester.gameworld.agent.AgentProperties.PropName;
import ballester.gameworld.agent.actions.Action;
import ballester.gameworld.agent.actions.ActionSequence;
import ballester.gameworld.agent.emotionalstate.EmotionalState;
import ballester.gameworld.agent.emotionalstate.Tranquil;
import ballester.gameworld.examples.GameWorldDebug;
import ballester.gameworld.physics.Dynamics;
import ballester.gameworld.physics.Geometry;
import ballester.gameworld.physics.HPoint;
import ballester.gameworld.physics.Point;

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

    // Current State
    public EmotionalState emotionalState;
    public AgentProperties props = new AgentProperties();

    private Inventory inventory = new Inventory();
    private Agent inInventoryOf;

    // External
    public transient AgentWorld world;
    public transient Action lastAction = null;
    public boolean lastMoved = false;

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
	this.props.set(AgentProperties.PropName.HEALTH_NORMAL, 20.0);
	this.props.set(AgentProperties.PropName.HEALTH, 20.0);
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
	EmotionalState oldEm = emotionalState;
	emotionalState = null;
	// ORGANIC emotional state
	for (EmotionalState es : emotions) {
	    boolean s = es.preCondition(this, world);
	    if (s) {
		emotionalState = es;
		break;
	    }
	}
	if (oldEm != null && emotionalState == null) {
	    world.narrator.narrate("$1 was no longer $2", this, oldEm);
	} else {
	    if (emotionalState != null && !emotionalState.equals(oldEm)) {
		world.narrator.narrate("$1 became $2", this, emotionalState);
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
	props.setSize(1.0, 1.0);
    }

    /**
     * Run for every agent at the end of turn
     */
    public void endOfTurn() {
	lastMoved = false;
    }

    /**
     * @param dynamics
     * @param object
     */
    public void move(Dynamics dynamics) {
	lastMoved = true;
	point.remember();
	world.physics.move(point, dynamics);
    }

    /**
     * @return
     */
    public String infoBox() {
	return "P=" + this.point + ", S=" + this.props.getSize() + ", BOX=(" + //
		(this.point.getX() - this.props.getSize().getX() / 2.0) + "," + //
		(this.point.getY() - this.props.getSize().getX() / 2.0) + ") (" + //
		(this.point.getX() + this.props.getSize().getX() / 2.0) + "," + //
		(this.point.getY() + this.props.getSize().getX() / 2.0) + ")";
    }

}
