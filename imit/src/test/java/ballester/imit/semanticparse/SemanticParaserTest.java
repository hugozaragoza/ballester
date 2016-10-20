/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.imit.semanticparse;

import org.junit.Assert;
import org.junit.Test;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentWorld;
import ballester.gameworld.agent.AgentProperties.PropName;
import ballester.gameworld.agent.characters.Princess;
import ballester.grammar.simplegrammar.parse.SimpleParser;
import ballester.grammar.simplegrammar.types.Lexicon;
import ballester.grammar.simplegrammar.types.Word;
import ballester.grammar.simplegrammar.types.WordTree;
import ballester.imit.mapper.WorldMatcher;

/**
 * @author hugoz
 *
 */
public class SemanticParaserTest {
    final Lexicon lexicon = Lexicon.getLexicon();

    @Test
    public void testCase1() throws Exception {
	AgentWorld world = new AgentWorld();
	world.createAgent(new Princess(), true);
	Assert.assertTrue(world.getAgents().get(0).props.getHealth() > 0.0);

	SemanticParser.parseSem2("the princess is dead", lexicon, world);
	Assert.assertTrue(world.getAgents().get(0).props.getHealth() <= 0.0);

    }

    @Test
    public void parseSem1_GroundObjectsTest_Existing() throws Exception {

	String color = "green";
	String s = "the princess is " + color;

	WordTree wt = SimpleParser.parseSyn0(s.split(" "), lexicon);
	AgentWorld world = new AgentWorld();

	// Fails to ground:
	SimpleParser.parseSyn1(wt);
	try {
	    SemanticParser.parseSem1_GroundObjects(wt, world);
	    Assert.fail("Should throw exception");
	} catch (SemanticParseException e) {

	} catch (Exception e) {
	    Assert.fail("Should throw SemanticParseException exception but threw:" + e.getMessage());
	}

	// Grounds:
	world = new AgentWorld();
	world.createAgent(new Princess(), true);
	Agent worldSubject = world.getAgents().get(0);
	SemanticParser.parseSem1_GroundObjects(wt, world);
	Word verb = wt.getNode(wt.getChild(0));
	Word subj = (Word) verb.semmantics.getFeatureTerminalValue(SimpleParser.ARG1);
	Agent sntcSubject = (Agent) subj.obj;
	Assert.assertNotNull(sntcSubject);
	Assert.assertTrue(worldSubject == sntcSubject); // same obj ref

    }

    @Test
    public void parseSem_creteNewObjects() throws Exception {
	AgentWorld world = new AgentWorld();
	String s = "a green princess";
	SemanticParser.parseSem2(s, lexicon, world);

	Assert.assertEquals(1, world.getAgents().size());
	Agent a = world.getAgents().get(0);
	Assert.assertTrue(a instanceof Princess);
	Assert.assertEquals("green", a.props.getColor());
    }

    @Test
    public void parseSem1_test1() throws Exception {

	String color = "green";
	String s = "a princess is " + color;

	WordTree wt = SimpleParser.parseSyn0(s.split(" "), lexicon);

	// Partial sem parse
	SimpleParser.parseSyn1(wt);

	AgentWorld world = new AgentWorld();
	AgentWorld sw = SemanticParser.parseSem1(wt, world);
	Assert.assertEquals(sw.getAgents().size(), 1); // princess was not in
						       // world, so was created
						       // in sw
	Agent a = sw.getAgents().get(0);
	Assert.assertTrue(a instanceof Princess);
	Assert.assertEquals(color, a.props.getColor());

    }

    @Test
    public void testParse_SubjectObjectSelection() throws Exception {
	AgentWorld sw = new AgentWorld();

	SemanticParser.parseSem2("a white princess", lexicon, sw);
	SemanticParser.parseSem2("a blue princess", lexicon, sw);
	SemanticParser.parseSem2("a black princess", lexicon, sw);

	Agent a1 = new Princess();
	a1.props.set(PropName.COLOR, "blue");
	Agent a2 = new Princess();
	a2.props.set(PropName.COLOR, "red");

	Agent f1 = WorldMatcher.find(a1, sw.getAgents());
	Assert.assertNotNull(f1);
	Agent f2 = WorldMatcher.find(a2, sw.getAgents());
	Assert.assertNull(f2);

	SemanticParser.parseSem2("the blue princess is red", lexicon, sw);

	f1 = WorldMatcher.find(a1, sw.getAgents());
	Assert.assertNull(f1);
	f2 = WorldMatcher.find(a2, sw.getAgents());
	Assert.assertNotNull(f2);

    }
}
