package ballester.gameworld.renderer;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ballester.gameworld.agent.Agent;

public class LineRenderer extends Renderer {
	final private static Logger logger = Logger.getLogger(LineRenderer.class);
	final int LEN = 20;
	ConsoleWriter log = new ConsoleWriter();

	ArrayList<String[]> screen = new ArrayList<String[]>();

	@Override
	public void draw(Agent a) {
		int pos;
		if (screen.size() < 1)
			screen.add(new String[LEN]);

		if (a.isInInventory()) {
			return;
		}

		pos = a.point.getXInt();
		screen.get(0)[pos] = (a.getClass().getSimpleName()).substring(0, 1);

		int i = 1;
		for (Agent ai : a.getInventory()) {
			if (screen.size() < (1 + i)) {
				screen.add(new String[LEN]);
			}
			pos = ai.inInventoryOf().point.getXInt();
			screen.get(i)[pos] = (ai.getClass().getSimpleName()).substring(0, 1).toLowerCase();
			i++;
		}

		return;
	}

	@Override
	public void render() {
		for (String[] s : screen) {
			System.out.println("SCREEN:       [ " + StringUtils.join(s, " ") + " ]");
		}
		screen = new ArrayList<String[]>();
	}

	@Override
	public void drawStory(String string) {
		System.err.println(string);
	}
}
