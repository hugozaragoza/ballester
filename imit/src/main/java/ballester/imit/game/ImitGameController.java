/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.imit.game;

import java.util.ArrayList;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentWorld;
import ballester.gameworld.control.GameController;
import ballester.gameworld.control.Script;
import ballester.gameworld.physics.HPoint;
import ballester.gameworld.renderer.GraphicRenderer1;
import ballester.gameworld.renderer.Renderer;
import ballester.grammar.simplegrammar.parse.SimpleParser;
import ballester.grammar.simplegrammar.types.Lexicon;
import ballester.imit.mapper.SemWorldMappper;
import ballester.imit.semanticparse.DiscourseDomain;
import ballester.imit.semanticparse.SemanticParser;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * Combianes a GameController (game agent and physics) with Language Grammar and
 * Parsing
 * 
 * @author hugoz
 *
 */
public class ImitGameController extends GameController {

    // INIT SEM WORLD:
    public Lexicon lexicon = Lexicon.getLexicon();
    SimpleParser parser = new SimpleParser();
    SemWorldMappper mapper = new SemWorldMappper();
    DiscourseDomain dd;

    public ImitGameController(Renderer renderer, Script script, double width, double height, boolean onedimensional) {
	super(script, renderer, width, height, onedimensional);
	lexicon = Lexicon.getLexicon();
	parser = new SimpleParser(); // parse0 and parse1
	dd = new DiscourseDomain(this.world);
    }

    public void interpret(String s, GraphicRenderer1 rendererSentenceWorld) throws Exception {

	// SOME DIRTY TRICKS UNTIL GRAMMAR IS DEVELOPED:
	if (s.toLowerCase().startsWith("there was")) {
	    s = s.substring("there was".length());
	}
	AgentWorld wS0 = SemanticParser.parseSem2(s, lexicon, world, dd);

	drawSentenceWorld(wS0, rendererSentenceWorld);
	draw();
	world.narrator.narrate("\"" + s + "\"");

    }

    /**
     * @param wS0
     * @param rendererL
     */
    private void drawSentenceWorld(AgentWorld wS0, GraphicRenderer1 rendererL) {
	rendererL.drawScene(wS0);
    }

    /**
     * @param sw
     */
    private void echo1(String s, String name) {
	System.out.println("********* START " + name + "*****************");
	System.out.println(s);
	System.out.println("********* END " + name + "*****************");
    }

}