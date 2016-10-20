/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.gameworld.renderer;

import java.util.ArrayList;
import java.util.HashMap;

import javax.print.attribute.HashAttributeSet;

import org.apache.log4j.Logger;

import ballester.gameworld.agent.AgentProperties;
import ballester.gameworld.agent.actions.Action;
import ballester.gameworld.agent.characters.LivingAgent;
import ballester.gameworld.agent.emotionalstate.EmotionalState;

/**
 * @author hugoz
 *
 */
public class Narrator {
	final static Logger logger = Logger.getLogger(Narrator.class);

	ArrayList<String> story = new ArrayList<String>();
	int lastNarrated = -1;

	private HashMap<String, String> cacheSubject = new HashMap<>();

	public Narrator() {
	}

	public void _narrate(String string) {
		if (string == null) {
			return;
		}
		string = normalize(string);
		logger.debug("NARRATED: [" + string + "]");
		if (!sameLastInput(string, 2)) {
			story.add(string);
		} else {
			story.add("and again...");
		}
	}

	/**
	 * @param string
	 * @return
	 */
	private String normalize(String string) {
		String ret = upperFirst(string.toLowerCase());
		return ret;
	}

	private boolean sameLastInput(String msg, int times) {
		int size = story.size();
		if (size < times)
			return false;

		for (int i = 0; i < times; i++) {
			if (!story.get(story.size() - 1 - i).equals(msg))
				return false;
		}
		return true;

	}

	/**
	 * Replace $1 by SUBJECT's description, $2 by OBJECT description
	 * 
	 */
	public void narrate(String string, Object subject, Object object) {
		if (string == null) {
			return;
		}

		Object[] objs = new Object[] { subject, object };
		String[] exps = new String[objs.length];

		for (int i = 0; i < exps.length; i++) {
			if (objs[i] == null) {
				continue;
			}
			exps[i] = objs[i].getClass().getSimpleName();
			if (objs[i] instanceof LivingAgent) {
				exps[i] = "the " + exps[i];
			} else if (objs[i] instanceof EmotionalState) {
				EmotionalState s = (EmotionalState) (objs[i]);
				exps[i] = s.getClass().getSimpleName();
				if (s.target != null) {
					exps[i] += " by the " + s.target.getClass().getSimpleName();
				}
			} else if (objs[i] instanceof Action) {
				Action a = (Action) (objs[i]);
				exps[i] = a.getClass().getSimpleName();
				if (a.target != null) {
					exps[i] += " was " + a.target.getClass().getSimpleName();
				}
			}
		}

		for (int i = 0; i < objs.length; i++) {
			if (objs[i] == null) {
				continue;
			}
			string = string.replaceAll("[$]" + (i + 1), exps[i]);
		}

		if (objs[0] != null) {
			String old = cacheSubject.get(exps[0]);
			if (old != null && old.equals(string)) {
				return;
			} else {
				cacheSubject.put(exps[0], string);
			}
		}

		_narrate(string);

	}

	/**
	 * @param string
	 * @return
	 */
	private String upperFirst(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}

	/**
	 * @return
	 */
	public ArrayList<String> getStory() {
		return story;
	}

	/**
	 * @param string
	 */
	public void narrate(String string) {
		_narrate(string);
	}

	/**
	 * 
	 */
	public void clear() {
		story.clear();
		cacheSubject.clear();

	}

}
