package ballester.gameworld.control;

import java.util.ArrayList;
import java.util.Random;

import org.apache.log4j.Logger;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentWorld;
import ballester.gameworld.agent.StillAgent;
import ballester.gameworld.agent.actions.Action;
import ballester.gameworld.physics.Geometry;
import ballester.gameworld.physics.Point;
import ballester.gameworld.renderer.Renderer;

public class GameController {
    /**
     * 
     */
    private static final int MAX_COLLISIONS = 100;
    private static final double OVERLAP_ALLOWED = 1;

    final private static Logger logger = Logger.getLogger(GameController.class);

    Script script;
    public AgentWorld world;
    Random rnd = new Random();
    private Renderer renderer;

    int lastCollisions;

    public GameController(Script script, Renderer rendered, double width, double height, boolean oneDimensional) {
	this.script = script;
	this.renderer = rendered;
	restart(width, height, oneDimensional);
    }

    public void restart(double width, double height, boolean onedimensional) {
	if (world == null) {
	    world = new AgentWorld(width, height, onedimensional, OVERLAP_ALLOWED);
	}
	restart();
    }

    public void restart() {

	world.clear();
	if (script != null)
	    script.addCharacters(world, 0);
    }

    public boolean checkGameEnd() {
	for (Agent a : world.getAgents()) {
	    if (a.lastAction != null)
		return false;
	}
	return true;
    }

    public void endWorld() {
	world.narrator.narrate("And that's the end of our story... good bye!");
	renderer.drawStory("=========\nGAME OVER. Epoch:" + world.epoch);
    }

    public void runCycle() {
	beginOfTurn();
	scriptActions();
	act();
	world.updateDeaths();
	moveWorld();
	updateCollissions();
	draw();
	endOfTurn();
    }

    private void scriptActions() {
	if (script != null) {
	    script.addCharacters(world, world.epoch);
	}
    }

    private void moveWorld() {

    }

    void updateCollissions() {
	boolean collision = false;
	lastCollisions = 0;
	do {
	    collision = false;
	    for (Agent a : world.getAgents()) {
		if (!a.lastMoved) {
		    continue;
		}

		for (Agent aa : world.getAgents()) {
		    if (a instanceof StillAgent || a == aa) {
			continue;
		    }
		    if (Geometry.touches(a, aa, world.overlapAllowed)) { // collision
			logger.debug("Collision " + a.name + " on " + aa.name + "\n" + "  " + a.infoBox() + "\n  "
				+ aa.infoBox());
			Geometry.pushOutOfCollision(a, aa, world.overlapAllowed, world.oneDimensional);
			lastCollisions++;
		    }
		}
	    }
	} while (collision || lastCollisions == MAX_COLLISIONS);
	logger.debug("#COLLISIONS (max=" + MAX_COLLISIONS + "): " + lastCollisions);
    }

    public void draw() {
	renderer.drawScene(world);
	renderer.drawStory(world.narrator.getStory());
    }

    private void endOfTurn() {
	for (Agent a : world.getAgents()) {
	    a.endOfTurn();
	}
    }

    private void beginOfTurn() {
	world.epoch++;
    }

    private void act() {
	world.act();

    }

}
