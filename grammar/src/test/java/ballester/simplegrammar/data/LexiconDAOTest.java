/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.simplegrammar.data;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ballester.grammar.simplegrammar.types.Lexicon;
import ballester.grammar.simplegrammar.types.Word;

/**
 * @author hugoz
 *
 */
public class LexiconDAOTest {

	@Test
	public void test1() {
		// TODO
		Lexicon lex = Lexicon.getLexicon();
		Word w = lex.find("dragon");
		Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
		System.out.println(gson.toJson(w));
	}

}
