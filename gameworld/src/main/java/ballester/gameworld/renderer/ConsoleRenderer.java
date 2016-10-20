package ballester.gameworld.renderer;

import org.apache.log4j.Logger;

import ballester.gameworld.agent.Agent;

public class ConsoleRenderer extends Renderer {
	final private static Logger logger = Logger.getLogger(ConsoleRenderer.class);

	ConsoleWriter log = new ConsoleWriter();
	boolean draw = false;
	LineRenderer screen = new LineRenderer();

	public String info1(Agent a) {
		String loc = a.point.toString();
		String st = a.emotionalState.getClass().getSimpleName()
				+ (a.emotionalState.target != null ? " |" + a.emotionalState.target.getClass().getSimpleName() : "");

		String beh = a.lastAction == null ? "null" : a.lastAction.info1();

		String info = loc + " " + a.name + " {S:" + st + "}  {B:" + beh + "} ";
		return info;
	}

	@Override
	public void draw(Agent a) {
		if (draw || logger.isDebugEnabled()) {
			String info = info1(a);
			log.print(info);
		}
		screen.draw(a);
	}

	@Override
	public void drawStory(String string) {
		log.print(string);
	}

	@Override
	public void render() {
		screen.render();
	}

}
