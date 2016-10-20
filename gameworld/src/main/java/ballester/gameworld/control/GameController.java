package ballester.gameworld.control;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentWorld;
import ballester.gameworld.renderer.Renderer;

public class GameController {
    final private static Logger logger = Logger.getLogger(GameController.class);

    Script script;
    public AgentWorld world;

    private Renderer renderer;

    public GameController(Script script, Renderer rendered, double width, double height) {
	this.script = script;
	this.renderer = rendered;
	restart(width, height);
    }

    public void restart(double width, double height) {
	if (world == null) {
	    world = new AgentWorld(width, height);
	}
	restart();
    }

    public void restart() {
	world.clear();
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

    private void updateCollissions() {
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
