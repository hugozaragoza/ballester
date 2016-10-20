package featureTypedSequenceTrees;
import java.util.*;
import java.util.HashMap;


/**
 * Typed Feature Structure (TFS) Node
 * A node with a name, features, and pointers to words and objects.
 * @author hugoz
 *
 */
public class TFSNode  {
    public String name; // typ e
    public HashMap<String,String> f; // features
        
    public int tmpPosition;

    public TFSNode (String t) {
        name=t;        
        f = new HashMap<String,String>(0);
    }

    public TFSNode (String t, String feat1, String value) {
        name=t;
        f = new HashMap<String,String>(2);
        f.put(feat1,value);
    }
    
       public TFSNode (String t, String feat1, String value, String feat2, String value2) {
        this.name=t;
        f = new HashMap<String,String>(2);
        f.put(feat1,value);
        f.put(feat2,value2);    
    }
    
    public TFSNode (String t, String[] featValues) {
        this.name=t;
        if (featValues==null) {
            f = new HashMap<String,String>(0);
        	return;
        }
        f = new HashMap<String,String>(featValues.length);
        for(int i=0; i<featValues.length;i+=2)
            f.put(featValues[i],featValues[i+1]);
    }
    
    public TFSNode (String t, HashMap<String,String> featValues) {
        this.name=t;
        f = (HashMap<String,String>)featValues.clone();
    }
    
    public TFSNode (TFSNode clone) {
        this.name=clone.name;
        this.f = new HashMap<String,String>();
        for(String s: clone.f.keySet()) {
            f.put(s, clone.f.get(s));        
        }
        this.tmpPosition = clone.tmpPosition;
    }

    
    /**
     * Assigns values in:
     * @param assignmts: type_$n => value
     */
    public void assign(HashMap<String,String> assignmts) {
        for(String featName: f.keySet()) {
            String featVal = this.f.get(featName);            
            if ( featVal.startsWith("$") && assignmts.containsKey(featVal) )
                this.f.put(featName,assignmts.get(featVal));
    }
   }

    /**
     * Cheks is this node has the features in other.
     * (this is a quick check for a possible matchAndBind but witout actually binding.)
     * @param other
     * @return
     */
    public boolean isContainedIn(TFSNode  other) {
        if (!other.name.equals(name)) return false;
        for(String var: f.keySet()) {
            String val = f.get(var);
            if ( val.startsWith("$") )
                continue;
            else if (
            		other.f.containsKey(var) && 
            		( other.f.get(var).equals(val) || other.f.get(var).startsWith("$") )
            		)
                continue;
            else
                return false;            
        }
        return true;
    }

    /**
     * Basic node matching function.
     * Matches this FTItem with a (possibly more general) matchItem pattern and possibly $feat values in binds. 
     * => Node matched = this. Pattern matched=matchItem
     * this must have all features in matchItem (not viceversa)
     * for now no $n values allowed in this
     * if a feature in this has a $n value in matchItem,
     *   if the feature is in binds the value is taken from it and checked against,
     *   otherwsie it mathces, the value is put from this into binds
     * it modified the binds HashMap, not the rule.
     *   
     * @param matchPattern 
     * @param binds 
     * @return false if no match possible, true if match done (binds will be updated with variable binds)
    */
    public boolean matchNodeAndBind (TFSNode matchPattern, HashMap<String,String> binds) {
        if (matchPattern.name.equals(this.name)==false) 
        	return false;
        
        for(String featName: f.keySet()) {
            if ( this.f.containsKey(featName) == false ) return false;
            
            String featValThis = f.get(featName);
            String featValMatch = matchPattern.f.get(featName);
                       
            if (featValMatch.startsWith("$")) {
            	if (binds.containsKey(featValMatch))
                    featValMatch =binds.get(featValMatch);            
                else {
                    binds.put(featValMatch, featValThis);            
                    featValMatch = featValThis;
                }
            }
            
            if (!featValMatch.equals(featValThis) )
                return false;                        
        }
        return true;
    }



    
    public boolean unifiable(TFSNode  other) {
        if (other.name!=name) return false;
        for(String s: f.keySet()) {
            if ( other.f.containsKey(s) != false && 
                    other.f.get(s) != f.get(s) )
                return false;            
        }
        for(String s: other.f.keySet()) {
            if ( f.containsKey(s) != false && 
                    other.f.get(s) != f.get(s) )
                return false;            
        }
        return true;
    }

 
    public TFSNode unify (TFSNode b) {
        if (this.name != b.name) return null;
        TFSNode result = new TFSNode(this.name);
 
        for(String featName: f.keySet()) {
            if ( this.f.containsKey(featName) == false ) continue;
            
            String aFeatVal = f.get(featName);
            String bFeatVal = b.f.get(featName);
                       
            if (aFeatVal.startsWith("$")) {
                    aFeatVal = bFeatVal;                    
            }
            else if (bFeatVal.startsWith("$")) {
                    bFeatVal = aFeatVal;                    
            }
            
            if (aFeatVal!=bFeatVal)
                return null;                       
            else
                result.f.put(featName,aFeatVal);
        }
        return result;
    }

    public String toString() {
        String r = this.name+"{"+Stringu Tools.join(f.keySet())+"}";
        return r;
    }
    
    public static void TestTFSNodes() {
    	TFSNode a1 = new TFSNode("a", "p1", "v1");
    	System.out.println(a1);
    	TFSNode a2 = new TFSNode("a");
    	System.out.println(a2);
    	TFSNode a3 = new TFSNode("a", "p1", "$1");
    	System.out.println(a3);    	
    	HashMap<String, String> binds = new HashMap<String, String>();
    	
    	System.out.println("a1 match a2 : "+a1.matchNodeAndBind(a2, binds));    	
    	System.out.println("a2 match a1 : "+a2.matchNodeAndBind(a1, binds));    	
    	System.out.println("a1 match a3 : "+a1.matchNodeAndBind(a3, binds));
    	System.out.println("binds: "+binds);
    	System.out.println("a1 match a3 : "+a1.matchNodeAndBind(a3, binds));
    	a3.assign(binds);
    	System.out.println(a3);    	
    	
    	
    	
    }
    
}
        
