package featureTypedSequenceTrees;
       
public class TFSTreeIterator extends TreeSeqIterator<Word> {
    public Word lastNoun = null;
    public boolean trace=false;
	
    public TFSTreeIterator(TFSTree tree, boolean depthFirst) {
        super(tree,depthFirst);
    }
    
    public TFSTreeIterator(TFSTree tree, boolean depthFirst, int iterationStart) {
        super(tree,depthFirst,iterationStart);
    }
 
    /**
     * iterate until a node is found so that match.isContainedIn(p).
     * updates lastNoun pointer with last noun found.
     * @param match
     * @return
     */
    public int findNode(Word match) {        
        Word p;
    	startIfNeeded();

        do {            
            p = currentNode();
            if (trace)
            	System.out.println("iterator looking at: "+p);
            if (p==null) break;
            if (p.name.equals("N"))
            	lastNoun=p;
            if (match.isContainedIn(p))  {
            	if (trace)
            		System.out.println("iterator returning: "+current);
                return current;
            }
            next();
        } while(!done());
        return -1;        
    }

}