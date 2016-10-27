package ballester.gameworld.agent;

import java.util.ArrayList;
import java.util.Random;

import org.apache.log4j.Logger;

import ballester.gameworld.agent.actions.Action;
import ballester.gameworld.agent.characters.LivingAgent;
import ballester.gameworld.physics.HPoint;
import ballester.gameworld.physics.Physics;
import ballester.gameworld.physics.Point;
import ballester.gameworld.renderer.Narrator;

public class AgentWorld {
    final private static Logger logger = Logger.getLogger(AgentWorld.class);
    final private static Random rnd = new Random();

    private ArrayList<Agent> agents = new ArrayList<Agent>();
    public Physics physics;
    public int epoch = 0;

    public double width, height;
    public boolean oneDimensional;

    // public Renderer renderer;

    public Narrator narrator = new Narrator();

    public double overlapAllowed;

    public AgentWorld() {
	this(100., 100., false, 0.0);
    }

    public AgentWorld(double width, double height, boolean oneDimensional, double overlapAllowed) {
	this.width = width;
	this.height = height;
	this.oneDimensional = oneDimensional;
	physics = new Physics(width, height);
	this.overlapAllowed = overlapAllowed;
    }

    public void act() {

	for (Agent a : getAgents()) {

	    a.updateStats();
	    a.updateEmotionalState();

	    Action b = a.action;
	    if (!b.checkCompletedCondition()) {
		if (b.checkPrecondition()) {
		    b.act();
		} else {
		    a.lastAction = null;
		}
	    }

	}
    }

    public boolean contains(Agent target) {
	return (getAgents().contains(target));
    }

    /**
     * Associates agent to this world and calls agent.createInWorld
     * 
     * @param agent
     * @param init
     *            : when creating agent for a temp world (e.g. sentence world)
     *            it should not be modified
     */
    public void createAgent(Agent agent, boolean init) {
	if (agent == null) {
	    logger.error("CREATE null AGENT???");
	    return;
	}
	getAgents().add(agent);
	if (init) {
	    agent.createInWorld(this);
	    agent.point = new HPoint(chooseStartingPoint());
	}
    }

    /**
     * @return
     */
    private Point chooseStartingPoint() {
	double x = rnd.nextDouble() * this.width;
	double y = oneDimensional ? (this.height / 2) : (rnd.nextDouble() * this.height);
	return new Point(x, y);
    }

    public ArrayList<Agent> getAgents() {
	return agents;
    }

    /**
     * 
     */
    public void updateDeaths() {
	ArrayList<Agent> remove = new ArrayList<>();
	for (Agent a : agents) {
	    if (a.props.isDead()) {
		remove.add(a);
		narrator.narrate(a instanceof LivingAgent ? "$1 is dead!" : null, a, null);
		logger.debug("DEAD AGENT " + a);
	    }
	}

	agents.removeAll(remove);
    }

    /**
     * 
     */
    public void clear() {
	this.agents.clear();
	this.narrator.clear();
	this.epoch = 0;
    }

}
