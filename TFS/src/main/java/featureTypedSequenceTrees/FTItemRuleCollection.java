package featureTypedSequenceTrees;
import java.util.*;


public class FTItemRuleCollection {
    ArrayList<TFSRewriteRule> col;
    
    public FTItemRuleCollection(ArrayList<TFSRewriteRule> col){ 
        this.col = col;
    }

    public FTItemRuleCollection(){ 
        this.col = new ArrayList<TFSRewriteRule>(0);
    }
    
    public void addMatch (TFSRewriteRule match) {
        col.add(match);
    }

    public class ComparatorByPriority<E> implements Comparator<E>{
    	  public int compare( E o1, E o2 ) {
    		  TFSRewriteRule r1 = (TFSRewriteRule)o1;
    		  TFSRewriteRule r2 = (TFSRewriteRule)o2;
    		  if (r1.Priority() < r2.Priority()) {
    			  return 1;
    		  }
    		  else if (r1.Priority() > r2.Priority()) {
    			  return -1;
    		  }
    		  else return 0;
    	  }
    }

    public String toString() {
    	StringBuffer b = new StringBuffer();
    	for(TFSRewriteRule r : col)
    		b.append("\n"+r.toString());
    	return b.toString();
    }
    
    public void sortByLength() {	
    	Collections.sort(col, new ComparatorByPriority<TFSRewriteRule>());
    }
    
    /**
     * Assumes v has sequence of nodes under a root node labelled "WordSequence".
     * Attempts to apply all matching rules in order to the node sequence.
     * Rules with name "S" are matched only if they cover the entire top.level sequence (no partial matches with "S")
     * @return true if last substitution yields an "S", false otherwise
     */
    public boolean matchAllRules(TFSTree v) {
        Word replacement;
        Collections.sort(col, new ComparatorByPriority<TFSRewriteRule>());
        if (v.nodes.get(v.root).name != "WordSequence") {
        	return false;
        }        
        int firstWord = v.firstChild.get(v.root);
        int next = firstWord;
    	boolean change = false;
        for (int mCount = 0; mCount<col.size(); mCount++) {
            TFSRewriteRule m = col.get(mCount);               
        	change = false;
        	while(true) {        		        	
                // b = 0;                 
                if (m.name.name=="S") {
                    replacement = m.matchFull(v, next);                
                }
                else{
                    replacement = m.matchFirstLocation(v, next);                
                }

                if (replacement !=null) {
                	v.pushDownSequence(replacement.tmpPosition, m.pattern.length, replacement);
                	if (m.name.name=="S") { // quick exit    
                		v.removeHead(v.root);
                		return true;
                	}
        	            
                	next = v.move_right(v.parent.get(next));
                	change = true;
                }

                if (replacement==null || next<0) // leave if no replacement done or at end.
                	break;
        	}
        	
        	    	        	
        	if (change) { // restart rules
        		next = v.firstChild.get(v.root);
        		change = false;
        		mCount = -1;
        	}        	
        }
        return false;
    }
}