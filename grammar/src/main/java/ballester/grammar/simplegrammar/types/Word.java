/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.grammar.simplegrammar.types;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.esotericsoftware.kryo.Kryo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ballester.datastructures.Copyable;
import ballester.datastructures.fs.FeatStruct;
import ballester.grammar.simplegrammar.types.Lexicon.Pos;

/**
 * Draft and place holder, TODO this seriously
 * 
 * @author hugoz
 *
 */
public class Word {
    public static boolean displayNodeIndices = true;
    public String lemma;
    public ArrayList<String> aliases;
    public FeatStruct syntax;
    public FeatStruct semmantics;
    public Object obj; // object in the world (e.g. grounding)

    // private static final Kryo kryo = new Kryo();
    private static final Gson gson = new Gson();

    public Word() {
    }

    public String getLemma() {
	return lemma;
    }

    public String getPos() {
	return (String) syntax.getFeatureTerminalValue(Lexicon.FEATNAME_POS);
    }

    /**
     * @param string
     */
    public Word(String string) {
	this.lemma = string;
	this.aliases = new ArrayList<String>();
	this.aliases.add(string);
	this.syntax = new FeatStruct();
	this.semmantics = new FeatStruct();

    }

    /**
     * Deep copy of w, except .obj which points to same reference
     * 
     * FIXME: using Gson for this now (superslow) becuase Kryo.copy was
     * returning shallow
     * 
     * @param w
     * @return
     */
    public Word copy() {
	return gson.fromJson(gson.toJson(this), Word.class);

	// return kryo.copy(this);
    }

    @Override
    public String toString() {
	int tab = 1;
	String ret = "";
	if (syntax.size() > 0) {
	    ret += "*SYN: " + syntax.toString(tab, displayNodeIndices);
	}
	if (semmantics.size() > 0) {
	    ret += "*SEM: " + semmantics.toString(tab, displayNodeIndices);
	}
	return ("'" + lemma + "'\t\t {" + StringUtils.join(aliases, ", ") + "}\n" + ret);

    }
}
