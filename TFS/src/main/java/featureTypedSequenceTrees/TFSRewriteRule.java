package featureTypedSequenceTrees;
import java.io.File;
import java.util.*;

import parser.Lexicon;
import tools.TLog;
import tools.TLog.LogLevel;

    
public class TFSRewriteRule {
	public String title;
	public Word name;
	public Word[] pattern;
       
    public String toString() {
        String s = title+". "+name.toString()+" <- ";
        for(Word i : pattern) {
        	if (i==null)
        		System.err.println("NULL WORD IN RULE!");
            s+=i.toString();
        }
        return s;
    }
    
    public TFSRewriteRule(String title, Word name, Word[] pattern)
    {
    	this.title = title;
    	this.name=name;
        this.pattern=pattern;        
    }

    public TFSRewriteRule(TFSRewriteRule clone)
    {
    	this.title = clone.title;
        this.name=new Word(clone.name);
        this.pattern=new Word[clone.pattern.length];
        for(int i=0;i<clone.pattern.length;i++)
            this.pattern[i] = new Word(clone.pattern[i]);
    }

    public int Priority() {
    	return pattern.length;
    }
    
    /**
     * Matches a sequence of patterns (this TFSRewriteRule) on the sequence starting in startNode.
     * If match it returns a copy of the rule head, binded.
     * @param match
     * @param sentence
     * @param startNode
     * @return
     */
    public Word match(TFSTree sentence, int startNode) {
    	TLog.push("match",LogLevel.Finest);
    	TLog.log(this.toString());
    	int len=0;
        HashMap<String,String> assignments = new HashMap<String,String>();
        
        do{
            Word s = sentence.node(startNode);
            if (s.matchNodeAndBind(this.pattern[len], assignments) == false)
                break;

            len++;
            TLog.log("Matched "+this.pattern[len-1]+". Matching continues, len="+len);

            if (len == this.pattern.length)
                break;           

            if (sentence.nextChild.containsKey(startNode)==false)
                break;
            
            startNode = sentence.nextChild.get(startNode);           
        } while (true);
        
        Word ret = null;
        if (len==this.pattern.length)
        {
        	TLog.log("!Matched!");        	
        	ret=new Word(this.name);
        	ret.assign(assignments);       
        }
    	TLog.pop();
        return ret;
    }

    /**
     * Attempts to find a match (match function) somewhere in the sequence starting from startSearch
     * NOTE: It does not go "down" any node, only "right"
     * NOTE: not efficient implementation, could use string search techniques
     */
    public Word matchFirstLocation(TFSTree sentence, int startSearch) {        
    	Word replacement = null;
        int location=-1;
        
        do {
            replacement =   this.match(sentence,startSearch);
            
            if (replacement != null){
                location = startSearch;
                break;
            }
            
            if (sentence.nextChild.containsKey(startSearch))
                startSearch = sentence.nextChild.get(startSearch);
            else
                break;
            
        } while(true);
        
        if (location<0) return null;
        
        replacement.tmpPosition = location;
        return replacement;
    }
    
    /**
     * Attempts to match (match function) the entire sequence starting at startNode.
     * @param sentence
     * @param startNode
     * @return
     */
    public Word matchFull(TFSTree sentence, int startNode) {        
        
        Word replacement = null;
        replacement =  this.match(sentence, startNode);
        if (replacement != null){
            int pattlen = this.pattern.length;
            int seqlen = 1 + sentence.numOfNextNodes(startNode);                       
            if (pattlen != seqlen )
            	replacement = null;            
        }
        
        return replacement;
    }
            
    public static TFSRewriteRule createFromString(String s, Lexicon lexicon) {
// _NPobj <-  _Det , _Nobj # name
    	String[] ss = s.split(" *<- *");
    	String name = ss[0].trim();
    	if (!lexicon.containsWord(name)) 
    		TLog.exit("COULD NOT FIND WORD <"+name+"> IN LEXICON in rule:"+s);
    	String from = ss[1].trim();
    	String title = "r"+lexicon.lexicon.size();
    	if (from.contains("#")) {
    		ss = from.split("#");
    		from = ss[0].trim();
    		title=ss[1].trim();
    	}
    	ss = from.split("[ ,]+");
    	Word[] pattern = new Word[ss.length];
    	int i=0;
    	for(String p : ss) {
    		if (!lexicon.containsWord(p)) 
        		TLog.exit("COULD NOT FIND WORD <"+p+"> IN LEXICON in rule:\n\t"+s);
        	pattern[i++] = lexicon.getWord(p);
    	}
    	TFSRewriteRule r = new TFSRewriteRule(title,
    			lexicon.getWord(name),
    			pattern);
    	return r;    	
    }
    
    public static void TestRewriteRule() {
    	String sen = "a b c a b";
    	String[] s = sen.split(" ");
    	TFSTree sentence = new TFSTree(new Word("S"));
    	for (String w : s) {
    		int i = sentence.addChild(sentence.root, new Word(w,new String[]{"feat","val"}) );
    		if (w.equals("b"))
    			sentence.nodes.get(i).f.put("feat", "val2");
    	}
    	System.out.println(sentence);

    	TFSRewriteRule r1 = new TFSRewriteRule(
    			"tit",
    			new Word("ab1"),
    			new Word[] {new Word("a"), new Word("b")}
    			);
    	System.out.println(r1);
    	Word match = r1.match(sentence, sentence.firstChild.get(sentence.Root()));
    	System.out.println(match);

    	TFSRewriteRule r2 = new TFSRewriteRule(
    			"tit",
    			new Word("ab2"),
    			new Word[] {new Word("a",new String[]{ "feat","val" }), new Word("b")}
    			);
    	System.out.println(r2);
    	match = r2.match(sentence, sentence.firstChild.get(sentence.Root()));
    	System.out.println(match);

    	String[] f = new String[]{ "feat", "$1"};
    	TFSRewriteRule r3 = new TFSRewriteRule(
    			"tit",
    			new Word("ab3", f),
    			new Word[] {new Word("a", f), new Word("b"), new Word("c", f)}
    			);
    	System.out.println(r3);
    	match = r3.match(sentence, sentence.firstChild.get(sentence.Root()));
    	System.out.println(match);

    	// NOTICE THE PROBLEM: this works but $2 is not filled because it has a different variable name as "feat"
    	r3 = new TFSRewriteRule(
    			"tit",
    			new Word("ab3", new String[] {"feat", "$1", "feat2", "$2"}),
    			new Word[] {
    				new Word("a", new String[] {"feat","$1"}), 
    				new Word("b", new String[] {"feat", "$2"}),
    				new Word("c", new String[] {"feat", "$1"})}
    			);
    	System.out.println(r3);
    	match = r3.match(sentence, sentence.firstChild.get(sentence.Root()));
    	System.out.println(match);

    }
    
    public static void main(String[] args) throws Exception {
    	
    	
    }
}
