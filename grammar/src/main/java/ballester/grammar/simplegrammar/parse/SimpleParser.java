/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.grammar.simplegrammar.parse;

import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ballester.datastructures.fs.FeatStruct;
import ballester.datastructures.fs.Feature;
import ballester.datastructures.tree.OrderedTreeIterator;
import ballester.datastructures.tree.OrderedTreeIterator.Order;
import ballester.grammar.simplegrammar.types.Lexicon;
import ballester.grammar.simplegrammar.types.Word;
import ballester.grammar.simplegrammar.types.WordTree;

/**
 * @author hugoz
 *
 */
public class SimpleParser {

    public static final String ARG2 = "_arg2";
    public static final String ARG1 = "_arg1";
    public static final String ARGn1 = "_argN1";
    public static final String ARGn2 = "_argN2";
    final private static Logger logger = Logger.getLogger(SimpleParser.class);

    /**
     * wordnize
     * 
     * @param sentence
     * @return
     */
    public static WordTree parseSyn0(String[] sentence, Lexicon lex) {
	WordTree sent1 = new WordTree();
	for (String s : sentence) {
	    Word w = lex.find(s.toLowerCase());
	    if (w == null) {
		w = new Word(s + " ?");
	    } else {
		w = w.copy(); // copy
	    }
	    sent1.addChild(sent1.getRoot(), w);
	}
	return sent1;
    }

    /**
     * FIXME : placeholder, grammar not yet implemented
     * 
     * @param sentence
     * @throws Exception
     */
    public static void parseSyn1(WordTree sentence) throws Exception {
	HashSet<Integer> visited = new HashSet<Integer>(sentence.size());
	String oldSent = "";
	boolean update = false;
	int order = 0;
	do {
	    update = false;

	    OrderedTreeIterator<Word> it = new OrderedTreeIterator<Word>(sentence, Order.POSTORDER);
	    int i;
	    while ((i = it.next()) > 0) { // TODO simple place-holder code, use
					  // a
					  // grammar instead such as TFSG

		if (visited.contains(i))
		    continue;
		Word w = sentence.getNode(i);

		String pos = (String) w.syntax.getFeatureTerminalValue(Lexicon.FEATNAME_POS);
		if (pos == null)
		    continue;

		if (order == 0) {
		    // startsWith allows for hierarchy so we get a simple type
		    // system...
		    if (pos.equals(Lexicon.Pos.DET.name()) || pos.equals(Lexicon.Pos.ADJ0.name())) {
			logger.trace("Inspecting DET* or ADJ_*: " + w.lemma);
			int n = blendWithNextN(sentence, i);
			if (n >= 0) {
			    // sentence.remove(i);
			    visited.add(i);
			    update = true;
			    break;
			}
		    }
		}

		else if (order == 1) {

		    if (pos.equals(Lexicon.Pos.COMP.name())) {
			pushARGS_leftRight(sentence, i, ARGn1, ARGn2);
		    }

		} else if (order == 10) {

		    if (logger.isDebugEnabled()) {
			logger.debug("Inspecting POS:[" + pos + "] LEMMA:[" + w.lemma + "]");
		    }
		    if (pos.equals(Lexicon.Pos.V2.name())) {
			// ASSUME ARG1 is previous and ARG2 is next:
			pushARGS_leftRight(sentence, i, ARG1, ARG2);
			update = true;
			visited.add(i);
			break;
		    }
		}

	    }
	    if (!update) {
		if (logger.isDebugEnabled()) {
		    String sent = sentence.toString();
		    if (!sent.equals(oldSent)) {
			oldSent = sent;
			logger.debug("\n---START order=" + order + "-------------------" + sent + "\n---END order="
				+ order + "-------------------");

		    }

		}
		order++;
	    }
	} while (order < 100);

    }

    /**
     * @param sentence
     * @param i
     * @throws Exception
     */
    private static void pushARGS_leftRight(WordTree sentence, int i, String arg1FeatName, String arg2FeatName)
	    throws Exception {
	int arg1i = sentence.getPreviousSibling(i);
	int arg2i = sentence.getNextSibling(i);
	Word v = sentence.getNode(i);
	Word arg1 = sentence.getNode(arg1i);
	try {
	    v.semmantics.add(new Feature(arg1FeatName, arg1));
	} catch (Exception e) {
	    logger.error("ERROR ADDING " + arg1FeatName + " TO " + v);
	    throw (e);
	}
	Word arg2 = sentence.getNode(arg2i);
	try {
	    v.semmantics.add(new Feature(arg2FeatName, arg2));
	} catch (Exception e) {
	    logger.error("ERROR ADDING " + arg2FeatName + " TO " + v);
	    throw (e);
	}
	sentence.remove(arg1i);
	sentence.remove(arg2i);
    }

    /**
     * @param sentence
     * @param i
     * @return
     */
    private static int blendWithNextN(WordTree sentence, int i) {
	FeatStruct fs = new FeatStruct();
	fs.add(new Feature("pos", "N"));
	return GrammarOperations.blendSemmanticsWithNextSyntaxMatchingFeat(sentence, i, fs, true);
    }

    public static WordTree parse(String[] sentence, Lexicon lex) throws Exception {
	WordTree tree;

	if (logger.isDebugEnabled()) {
	    String msg = "---------------------\nLEXICON:\n" + lex + "\n---------------------\nSENTENCE:\n"
		    + StringUtils.join(sentence, ", ");
	    logger.debug(msg);
	}

	tree = parseSyn0(sentence, lex);

	if (logger.isDebugEnabled()) {
	    logger.debug("---------------------\nPARSE 0:\n" + tree.toString());
	}

	parseSyn1(tree);

	if (logger.isDebugEnabled()) {
	    logger.debug("---------------------\nPARSE 1:\n" + tree.toString());
	}

	return tree;
    }
}
