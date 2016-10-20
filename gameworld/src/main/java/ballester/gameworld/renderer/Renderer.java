package ballester.gameworld.renderer;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentWorld;

public abstract class Renderer {

    static int renderers = 0;

    String name = "Renderer_" + (++renderers); // may be used for debugging
					       // displays

    abstract void draw(Agent a);

    public void drawScene(AgentWorld w) {
	initScene();
	drawBox(w);
	for (Agent a : w.getAgents()) {
	    draw(a);
	}

    }

    abstract public void drawStory(String string);

    void render() {
    }

    void initScene() {
    }

    public void drawStory(ArrayList<String> story) {
	drawStory(StringUtils.join(story, "\n"));
    }

    /**
     * @param w
     */
    public void drawBox(AgentWorld w) {

    }
}
