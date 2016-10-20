package featureTypedSequenceTrees;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import parser.Lexicon;
import parser.WObject;

public class Word  extends TFSNode{
	
	// Syntax Info:
	public String ort;
    public String lemma; //

    // Semantic Info
	public boolean checked;
    public Object obj1; // refernces to things out there if any
    public Object obj2; // refernces to things out there if any

	public Word(Word clone) {
		super(clone);
		this.ort = clone.ort;
		this.lemma = clone.lemma;
		this.checked = clone.checked;
	}
	
    public Word(String ort, String lemma, String pos, String[] featureVals) {        
        super(pos,featureVals);
        this.ort = ort;
        this.lemma = lemma.toUpperCase();        
    }
    
    public Word(String lemma, String pos, String[] featureVals) {        
        super(pos,featureVals);
        this.ort = lemma;
        this.lemma = lemma.toUpperCase();        
    }

    public Word(String pos, String[] featureVals) {        
        super(pos,featureVals);
        this.ort = null;
        this.lemma = null;        
    }
    
    public Word(String pos) {        
        super(pos);
        this.ort = null;
        this.lemma = null;        
    }

    public boolean featuresContain(String featName) {
    	return f.containsKey(featName);
    }
    
    public String featuresGet(String featName) {
    	return f.get(featName);
    }

    public boolean featuresMatches(String featName, String featVal) {
    	boolean ret = false;
    	ret = featuresContain(featName) && featuresGet(featName).equals(featVal);
    	return ret;
    }

    public static Word createFromString(String stringWord) {
		// umbrella	: N , OBJ=1 NUM=s 
		String[] s = stringWord.split(":");
		String ort = s[0].trim();
		String pos = s[1].trim();
		int i = pos.indexOf(",");
		if (i>=0) {
			String feats = pos.substring(i+1).trim();
			pos = pos.substring(0,i).trim();
			if (feats.equals(""))
				s = null;
			else
				s = feats.split("[\t ,=]+");
		}
		else 
			s = null;
		Word w = new Word(ort,pos,s);
		if (w==null)
			System.err.println("COULD NOT PARSE WORD: "+stringWord);
		return w;
		}

    public String toString() {
    	String ret = super.toString();
    	if (ort!=null)
    		ret = ort+"<"+lemma+"> "+ret;
    	if (checked)
    		ret += "+";
    	if (obj1!=null)
    		ret += "OBJ1={"+obj1.toString()+"}";
    	if (obj2!=null)
    		ret += "OBJ2={"+obj2.toString()+"}";
    	
    	return ret;
    }
    
    
    public static void main (String[] args) throws Exception {
    	String s = "umbrella	: N , OBJ=1 NUM=s";
    	System.err.println(s);
    	Word w = Word.createFromString(s);
    	System.err.println(w);
    	s = "blue : Adj";
    	System.err.println(s);
    	w = Word.createFromString(s);
    	System.err.println(w);
    	

    }
}
