package featureTypedSequenceTrees;

public class TreeSeqIterator<T> {
    int current;
    boolean depthFirst;
    TreeSeq<T> tree;
    int headOfIteration;
    boolean started = false;
    
    public TreeSeqIterator(TreeSeq<T> tree, boolean depthFirst) {
        this.tree = tree;
    	this.depthFirst = depthFirst;
        current = -1;
        headOfIteration = tree.root;        
    } 
    
    public TreeSeqIterator(TreeSeq<T> tree, boolean depthFirst, int headOfIteration) {
    	this.tree = tree;
    	this.depthFirst = depthFirst;
        current = -1;
        if (headOfIteration<0)
            this.headOfIteration = tree.root;
        else
        	this.headOfIteration = headOfIteration;
    } 

    public boolean done(){
    	startIfNeeded();
        return (current<0);
    }
    
    public boolean notDone(){
        return (current>=0);
    }

    public void startIfNeeded() {
      if (started!=true)
    	  next();
    }
    
    public int next() {
    	if (started!=true) started=true;
        if (depthFirst)
            current = tree.depthFirstNext(current,headOfIteration);
        else
            current = tree.depthLastNext(current,headOfIteration);
        return current;
    }

    public int nextBrother() {
    	startIfNeeded();
        if (tree.nextChild.containsKey(current))
            current = tree.nextChild.get(current);
        else 
        	current = -1;
        return current;
    }
    
    public int nextNotDown() {
    	startIfNeeded();
        if (depthFirst)
        {
            System.out.println("NOT POSSIBLE");
            return -1;
        }
                
        int n;
        if (tree.nextChild.containsKey(current))
            n = tree.nextChild.get(current);
        else if (tree.parent.containsKey(current))
            n = tree.parent.get(current);
        else
            return -1;
        
        current = n;
        return current;
    }
    

    public int current(){
        return current;
    }
    
    public T currentNode() {
        return tree.node(current);        
    }

            
    public void reset() {
        current=-1;
    }

   

}
    