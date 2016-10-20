package ballester.gameworld.examples;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentProperties;
import ballester.gameworld.agent.AgentWorld;
import ballester.gameworld.agent.characters.Candy;
import ballester.gameworld.agent.characters.Dragon;
import ballester.gameworld.agent.characters.Princess;
import ballester.gameworld.control.GameController;
import ballester.gameworld.control.Script;
import ballester.gameworld.renderer.ConsoleRenderer;

public class GameWorldDebug {
    final private static Logger logger = Logger.getLogger(GameWorldDebug.class);
    private static final int MAX_EPOCHS = 40;

    public static void main(String[] args) {

	// RUN:
	GameController mc = new GameController(new MyScript(), new ConsoleRenderer(), 100, 100);
	for (int i = 0; i < MAX_EPOCHS; i++) {
	    mc.runCycle();
	    if (mc.checkGameEnd())
		break;
	}
	mc.endWorld();
    }

}

class MyScript implements Script {

    final private static Logger logger = Logger.getLogger(MyScript.class);
    static Agent a2;

    @Override
    public void addCharacters(AgentWorld world, int epoch) {
	ArrayList<Agent> as = new ArrayList<Agent>();

	if (epoch == 1) {
	    Candy a1 = new Candy();
	    a1.createInWorld(world);
	    a1.point.setXY(5, 0);
	    as.add(a1);

	    a2 = new Princess();
	    a2.createInWorld(world);
	    a2.point.setXY(10, 0);
	    as.add(a2);

	}

	if (epoch == 3) {
	    // logger.info("Forcing " + a2.name + " to be hungry for 5");
	    // a2.setEmotionalStateForced(new Hungry(), 5);

	    logger.info("Forcing " + a2.name + " to be hungry for 5");
	    a2.props.set(AgentProperties.PropName.HEALTH, 1.0);

	}

	if (epoch == 10) {
	    Dragon d = new Dragon();
	    d.createInWorld(world);
	    logger.info("Adding a " + d);
	    d.point.setXY(10, 0);
	    as.add(d);
	}

    }

}
