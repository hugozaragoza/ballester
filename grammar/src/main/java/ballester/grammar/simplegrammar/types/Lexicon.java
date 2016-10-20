/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.grammar.simplegrammar.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.SortedMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import ballester.datastructures.fs.FeatStruct;
import ballester.datastructures.fs.Feature;
import ballester.datastructures.tree.OrderedTree;
import ballester.grammar.simplegrammar.data.WordFactory;

/**
 * @author hugoz
 *
 */
public class Lexicon extends ArrayList<Word> {

    public static String FEATNAME_POS = "pos";
    public static final String SEM_V_STATE = "_state";
    public static final String SEM_V_POSSESSION = "_posses";
    public static final String SEM_forceState = "_force_state";
    public static final String SEM_update = "_update_property";
    public static final String SEM_GROUNDED = "__grounded";

    public static final String[] colorNames = new String[] { "black", "blue", "brown", "green", "orange", "pink", "red",
	    "violet", "white", "yellow" };

    public enum Pos {
	N, ADJ0, DET, ADV, V2
    }

    public String info1() {
	StringBuffer sb = new StringBuffer();

	for (Pos p : Pos.values()) {
	    sb.append("<h4>" + p.name() + "</h4><ul><li>");
	    Stream<Word> stream = this.stream().filter(x -> p.name().equals(x.getPos()));
	    String str = stream.map(Word::getLemma).sorted().collect(Collectors.joining("</li>\n  <li>"));
	    sb.append(str);
	    sb.append("</li>\n</ul>");
	}
	return sb.toString();
    }

    /**
     * @return
     */
    public static Lexicon getLexicon() {
	Lexicon lexicon = new Lexicon();
	ArrayList<Word> ws;
	FeatStruct sem, syn;

	// NOUNS

	// NOUNS. ACTORS
	sem = new FeatStruct();
	sem.add(new Feature("type", "$lemma"));

	syn = new FeatStruct();
	syn.add(new Feature(FEATNAME_POS, Pos.N.name()));

	ws = WordFactory.buildWords(new String[] { "princess", "dragon", "knight", "bow", "candy" }, syn, sem);
	lexicon.addAll(ws);

	// COLORS:

	syn = new FeatStruct();
	syn.add(new Feature(FEATNAME_POS, Pos.ADJ0.name()));

	// when object action: "the princess is red"
	FeatStruct update = new FeatStruct();
	update.add(new Feature("property", "color"));
	update.add(new Feature("update_type", "set"));
	update.add(new Feature("value", "$lemma"));
	sem = new FeatStruct();
	sem.add(new Feature(SEM_update, update));

	// when modifier: "the red princess"
	sem.add(new Feature("color", "$lemma"));

	ws = WordFactory.buildWords(colorNames, syn, sem);
	lexicon.addAll(ws);

	// STATES forceState:
	sem = new FeatStruct();
	sem.add(new Feature(SEM_forceState, "$lemma"));

	syn = new FeatStruct();
	syn.add(new Feature(FEATNAME_POS, Pos.ADV.name()));

	ws = WordFactory.buildWords(new String[] { "agressive", "scared", "bored" }, syn, sem);
	lexicon.addAll(ws);

	// STATES update:
	ws = verbModifyingProperty("hungry", "health", 5.0);
	lexicon.addAll(ws);

	ws = verbModifyingProperty("dead", "health", 0.0);
	lexicon.addAll(ws);

	// DETERMINANTS
	Word w;

	w = new Word("a");
	w.syntax.add(new Feature(FEATNAME_POS, Pos.DET.name()));
	w.semmantics.set(SEM_GROUNDED, false);
	lexicon.add(w);

	w = new Word("the");
	w.syntax.add(new Feature(FEATNAME_POS, Pos.DET.name()));
	w.semmantics.set(SEM_GROUNDED, true);
	lexicon.add(w);

	// VERBS OF STATE
	//
	// "Alice was hungry", "Alice was afraid"
	//
	w = new Word("to_be");
	w.aliases.addAll(Arrays.asList(new String[] { "is", "was" }));
	w.syntax.add(new Feature(FEATNAME_POS, Pos.V2.name()));
	w.semmantics.set(SEM_V_STATE, true);
	lexicon.add(w);

	// VERBS OF POSESSION
	w = new Word("have");
	w.aliases.addAll(Arrays.asList(new String[] { "has", "had" }));
	w.syntax.add(new Feature(FEATNAME_POS, Pos.V2.name()));
	w.semmantics.set(SEM_V_POSSESSION, true);
	lexicon.add(w);

	// END
	return lexicon;

    }

    /**
     * @param lemma
     * @param propertyModified
     * @param quantity
     * @return
     */
    private static ArrayList<Word> verbModifyingProperty(String lemma, String propertyModified, double quantity) {
	ArrayList<Word> ws;
	FeatStruct sem;
	FeatStruct syn;
	FeatStruct update;
	update = new FeatStruct();
	update.add(new Feature("update_type", "set"));
	update.add(new Feature("property", propertyModified));
	update.add(new Feature("quantity", quantity));
	sem = new FeatStruct();
	sem.add(new Feature(SEM_update, update));
	syn = new FeatStruct();
	syn.add(new Feature(FEATNAME_POS, Pos.ADV.name()));

	ws = WordFactory.buildWords(new String[] { lemma }, syn, sem);
	return ws;
    }

    /**
     * @param s
     * @return
     */
    public Word find(String s) {
	for (Word w : this) {
	    if (w.aliases.contains(s)) {
		return w;
	    }
	}
	return null;
    }

    @Override
    public String toString() {
	String ret = "";
	for (Word w : this) {
	    ret += w.toString() + "\n";
	}
	return ret;
    }
}
