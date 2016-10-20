/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.grammar.simplegrammar.data;

import java.util.ArrayList;

import ballester.datastructures.fs.FeatStruct;
import ballester.datastructures.fs.Feature;
import ballester.grammar.simplegrammar.types.Word;

/**
 * @author hugoz
 *
 */
public class WordFactory {

	/**
	 * @param lemma
	 * @param syntax
	 *            : (will create a copy of this)
	 * @param semantics
	 *            : (will create a copy of this)
	 * @return
	 */
	public static Word buildWord(String lemma, FeatStruct syntax, FeatStruct semantics) {
		Word w = new Word(lemma);
		if (syntax != null) {
			w.syntax.add(syntax.copy());
		}
		if (semantics != null) {
			w.semmantics.add(semantics.copy());
			_fillVar(lemma, "$lemma", w.semmantics);
		}
		return w;
	}

	/**
	 * @param value
	 * @param w
	 */
	private static void _fillVar(String value, String variable, FeatStruct fs) {
		for (String s : fs.getFetaureNames()) {
			Feature f = fs.getFeature(s);
			if (f.getFeatureStructure() != null) {
				_fillVar(value, variable, f.getFeatureStructure());
			}
			if (!f.getValue().isTerminal())
				continue;
			if (f.getValue().getTerminal().equals(variable)) {
				fs.set(new Feature(s, value));
			}
		}
	}

	public static ArrayList<Word> buildWords(String[] lemmas, FeatStruct syntax, FeatStruct semmantics) {
		ArrayList<Word> ret = new ArrayList<Word>(lemmas.length);
		for (String s : lemmas) {
			ret.add(buildWord(s, syntax, semmantics));
		}
		return ret;
	}

}
