/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.imit.semanticparse;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import ballester.datastructures.fs.FeatStruct;
import ballester.datastructures.tree.OrderedTreeIterator;
import ballester.datastructures.tree.OrderedTreeIterator.Order;
import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentWorld;
import ballester.gameworld.agent.emotionalstate.EmotionalState;
import ballester.grammar.simplegrammar.parse.SimpleParser;
import ballester.grammar.simplegrammar.types.Lexicon;
import ballester.grammar.simplegrammar.types.Word;
import ballester.grammar.simplegrammar.types.WordTree;
import ballester.imit.mapper.SemWorldMappper;
import ballester.imit.mapper.WorldMatcher;

/**
 * @author hugoz
 *
 */
public class SemanticParser {
    final private static Logger logger = Logger.getLogger(SemanticParser.class);

    /**
     * @param wt
     * @param wS0
     * @return
     * @throws Exception
     */
    public static AgentWorld parseSem1(WordTree wt, AgentWorld stateWorld) throws Exception {

	// GROUND ALL OBJECTS:
	AgentWorld wS0 = parseSem1_GroundObjects(wt, stateWorld);

	// APPLY VERBS:
	OrderedTreeIterator<Word> wi;
	boolean updated;
	do {
	    updated = false;
	    wi = new OrderedTreeIterator<Word>(wt, Order.PREORDER);
	    for (Word w = wi.getNext(); w != null; w = wi.getNext()) {
		Word arg1 = (Word) w.semmantics.getFeatureTerminalValue(SemWorldMappper.ARG1);
		Word arg2 = (Word) w.semmantics.getFeatureTerminalValue(SemWorldMappper.ARG2);
		Agent a1 = arg1 == null ? null : (Agent) arg1.obj;
		Agent a2 = arg2 == null ? null : (Agent) arg2.obj;

		if (w.semmantics.isTrue(Lexicon.SEM_V_POSSESSION)) {
		    a1.addToInventory(a2);
		    updated = true;
		    w.semmantics.remove(Lexicon.SEM_V_POSSESSION);
		    break;

		} else if (w.semmantics.containsFeature(Lexicon.SEM_V_STATE)) {
		    vAction_semUpdate(w, arg2, a1);
		}

	    }
	} while (updated);
	return wS0;
    }

    /**
     * @param w
     * @param arg2
     * @param a1
     * @throws SemanticParseException
     */
    private static void vAction_semUpdate(Word w, Word arg2, Agent a1) throws SemanticParseException {
	if (!w.semmantics.containsFeature(Lexicon.SEM_V_STATE)) {
	    return;
	}
	if (arg2.semmantics.containsFeature(Lexicon.SEM_update)) {
	    FeatStruct fs = arg2.semmantics.getFeature(Lexicon.SEM_update).getFeatureStructure();
	    String p = (String) fs.getFeatureTerminalValue("property");
	    Object o = fs.getFeatureTerminalValue("value");
	    if (o == null) {
		o = fs.getFeatureTerminalValue("quantity");
	    }
	    if (o == null) {
		SemWorldMappper.logger.error("CANNOT PARSE Lexicon.SEM_update with fs: [" + fs.toString() + "]");
	    } else {
		switch ((String) fs.getFeatureTerminalValue("update_type")) {
		case "set":
		    if (a1 == null) {
			String s = "NO ARG1 Subject for update?";
			throw new SemanticParseException(s);
		    }
		    a1.props.set(p, o);
		    break;

		default:
		    SemWorldMappper.logger
			    .error("UNIMPLEMENTED update TYPE: [" + fs.getFeatureTerminalValue("update_type") + "]");
		    break;
		}
		w.semmantics.remove(Lexicon.SEM_update);
	    }
	} else {
	    SemWorldMappper.logger.error("NOT IMPLEMENTED SEM_V_STAGE on such an arg2: " + arg2.toString());
	}
    }

    /**
     * @param sw
     * @param stateWorld
     * @return
     * @throws Exception
     */
    static AgentWorld parseSem1_GroundObjects(WordTree sw, AgentWorld stateWorld) throws Exception {
	AgentWorld wS0 = new AgentWorld();

	// CREATE ALL NOUN OBJECTS into a new world ws0
	OrderedTreeIterator<Word> wi = new OrderedTreeIterator<Word>(sw, Order.PREORDER);
	for (Word pw = wi.getNext(); pw != null; pw = wi.getNext()) {
	    ArrayList<Word> lis = new ArrayList<>();
	    lis.add(pw);
	    Object w1 = pw.semmantics.getFeatureTerminalValue(SimpleParser.ARG1);
	    Object w2 = pw.semmantics.getFeatureTerminalValue(SimpleParser.ARG2);
	    if (w1 != null) {
		lis.add((Word) w1);
	    }
	    if (w2 != null) {
		lis.add((Word) w2);
	    }
	    for (Word w : lis) {
		FeatStruct sem = w.semmantics;
		Agent a = null;
		try {
		    a = SemWorldMappper.mapFSToAgent(sem);
		} catch (Exception e) {
		    logger.error("Exception while mapping FA to Agent: " + sem);
		    throw (e);
		}
		if (a == null) {
		    continue;
		}
		boolean grounded = sem.isTrue(Lexicon.SEM_GROUNDED);
		if (grounded) {
		    a.props.remove(Lexicon.SEM_GROUNDED);
		    Agent aGround = WorldMatcher.find(a, stateWorld.getAgents());
		    if (aGround != null) {
			a = aGround; // ground
		    } else {
			a.props.set(Lexicon.SEM_GROUNDED, true);// put it back
								// before
								// error.
			throw new SemanticParseException("Could not ground " + a.toString()); // ERROR
		    }
		} else { // CREATE NEW ONE and GROUND otherwise
		    wS0.createAgent(a, false);
		}
		w.obj = a;
	    }
	}
	return wS0;

    }

    public static AgentWorld parseSem2(String s, Lexicon lexicon, AgentWorld world) throws Exception {
	// Parse Syn0, Syn1, Syn2
	WordTree sw = SimpleParser.parse(s.split(" "), lexicon);

	// Parse SEM1 SEM2
	AgentWorld wS0 = parseSem1(sw, world);

	parseSem2(wS0, world);

	return wS0; // perhaps we should return an unmodified copy before sem2
    }

    /**
     * Create agents not yet in world
     * 
     *
     * @param wS0
     * @param world
     * @throws Exception
     */
    public static void parseSem2(AgentWorld wS0, AgentWorld world) throws Exception {
	for (Agent a : wS0.getAgents()) {
	    world.createAgent(a, true);
	}
	// // THIS SHOULD BE REPLACED BY SOME TREE MATCHING ALGO, but right now
	// // world is a flat list of agents and their inventories
	//
	// for (Agent a : wS0.getAgents()) {
	// if (a.isInInventory()) // FIXME are we sure?
	// continue;
	//
	// if (!a.exists) { // need to be created
	// world.createAgent(a, true);
	// a.exists = true;
	// } else { // match and update.
	// // Agent aG = WorldMatcher.find(a, world.getAgents());
	// // if (aG == null) {
	// // world.createAgent(a, true);
	// // a.exists = null; // ERROR, COULD NOT FIND GROUND
	// // } else {
	// // SemWorldMappper.update(a, aG);
	// // }
	// if (!world.getAgents().contains(a)) {
	// logger.error("Object should be already in world: " + a);
	// }
	// }
	// }
    }

}
