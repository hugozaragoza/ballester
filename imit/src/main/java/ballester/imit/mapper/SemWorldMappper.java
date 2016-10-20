/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.imit.mapper;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

import ballester.datastructures.fs.FSValue;
import ballester.datastructures.fs.FeatStruct;
import ballester.datastructures.fs.Feature;
import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentProperties;
import ballester.gameworld.agent.Inventory;
import ballester.gameworld.agent.emotionalstate.EmotionalState;
import ballester.grammar.simplegrammar.parse.SimpleParser;

/**
 * @author hugoz
 *
 */
public class SemWorldMappper {

    public static final String ARG2 = "_arg2";
    public static final String ARG1 = "_arg1";

    public final static Logger logger = Logger.getLogger(SimpleParser.class);
    public static final int EMOTIONAL_STATE_FORCED_DURATION = 3;

    // public SemanticWorld mapSemToWorld(SemanticWorld sw1, AgentWorld aw) {
    // ArrayList<Agent> empty = new ArrayList<Agent>(0);
    //
    // for (Integer i : sw1.getChildren(sw1.getRoot())) {
    // Word w = sw1.getNode(i);
    // Agent a = null;
    // try {
    // a = mapToNewAgent(w.semmantics);
    // } catch (Exception e) {
    // logger.error(e);
    // }
    // if (a != null) {
    // Agent agent = null;
    // Filter f = new Filter(a.getClass(), null);
    // ArrayList<Agent> x = f.findActor(aw.agents, empty);
    // if (x.size() < 1) {
    // logger.debug("GROUNDING: Could not ground " + a);
    // continue;
    // } else if (x.size() > 1) {
    // logger.debug("GROUNDING: Too many " + x.size()
    // + " to ground " + a);
    // } else {
    // agent = x.get(0);
    // logger.debug("GROUNDING: " + a + " TO " + agent);
    // }
    // }
    // }
    // SemanticWorld sw = new SemanticWorld();
    // return sw;
    // }

    public static Agent mapFSToAgent(FeatStruct sf) throws Exception {

	String name = (String) sf.getFeatureTerminalValue("type");
	if (name == null)
	    return null;

	String classname = className(name);
	Agent agent = (Agent) Class.forName(classname).newInstance();

	for (String s : sf.getFetaureNames()) {
	    if (!sf.getFeature(s).isTerminal()) {
		logger.debug("IGNORED non-terminal feature " + s);
		continue;
	    }
	    Object v = sf.getFeatureTerminalValue(s);

	    if (s.equals("type")) {
		continue;
	    }

	    agent.props.set(s, v);
	}
	return agent;
    }

    private static String className(String name) {
	String path = Agent.class.getPackage().getName() + ".characters.";
	return path + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static FeatStruct mapAgentToFS(Agent a) {
	FeatStruct fs = new FeatStruct();
	try {
	    fs.add(new Feature("type", a.getClass().getSimpleName().toLowerCase()));
	    _toFS(a.props, fs);
	} catch (Exception e) {
	    logger.error(e);
	}
	return fs;
    }

    static void _toFS(AgentProperties props, FeatStruct fs) throws IllegalArgumentException, IllegalAccessException {
	// Field[] fields = props.getClass().getFields();
	// for (Field f : fields) {
	// if (java.lang.reflect.Modifier.isStatic(f.getModifiers())
	// || java.lang.reflect.Modifier.isFinal(f.getModifiers())) {
	// continue;
	// }
	// Object val = f.get(props);
	// if (val instanceof String) {
	// String strValue = (String) val;
	// Feature feat = new Feature(f.getName(), strValue);
	// fs.add(feat);
	// }
	// }

	for (String p : props.properties.keySet()) {
	    fs.add(new Feature(p, props.properties.get(p)));
	}

    }

    /**
     * @param a
     * @param aG
     * @throws Exception
     */
    public static void update(Agent aUpdate, Agent a) throws Exception {
	updateInventory(aUpdate, a);
	a.props.updateAgentProperties(aUpdate.props);

    }

    /**
     * TODO: replace by graph matcher
     * 
     * TODO: only adding for now, no removes
     * 
     */
    private static void updateInventory(Agent aUpdate, Agent a) {
	for (Agent ai : aUpdate.getInventory()) {
	    Agent old = WorldMatcher.find(ai, a.getInventory());
	    if (old == null) {
		a.addToInventory(ai);
	    }
	}
    }

    /**
     * @param lemma
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static EmotionalState emmotionalStateFromLemma(String lemma)
	    throws InstantiationException, IllegalAccessException, ClassNotFoundException {
	lemma = lemma.substring(0, 1).toUpperCase() + lemma.substring(1).toLowerCase();
	Package p = EmotionalState.class.getPackage();
	String className = p.getName() + "." + lemma;

	EmotionalState es = (EmotionalState) Class.forName(className).newInstance();
	return es;
    }
}
