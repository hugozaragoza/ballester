package featureTypedSequenceTrees;

import java.util.*;


    
/**
 * tree with ordered children
 * @author hugoz
 *
 * @param <T>
 */
public class TreeSeq<T> {    
    
    Hashtable<Integer, T> nodes;
    Hashtable<Integer,Integer> parent;
    Hashtable<Integer,Integer> nextChild;
    Hashtable<Integer,Integer> prevChild;
    Hashtable<Integer,Integer> firstChild;
    int lastIndex;
    int root;
    int lastMove;
    
    boolean displayTree = true;
    
    
    public int Root() {
    	return root;
    }

    public TreeSeq (T rootNode) {
        nodes = new Hashtable<Integer, T>();
        nodes.put(0, rootNode);
        lastIndex = 0;
        root = 0;
        lastMove = 0;
        parent = new Hashtable<Integer,Integer>();
        nextChild = new Hashtable<Integer,Integer>();
        firstChild = new Hashtable<Integer,Integer>();
        prevChild = new Hashtable<Integer,Integer>();
    }
    
    public String toString() {
    	int depth=0;
    	StringBuffer so = new StringBuffer();
    	so.append("\n");

    	int dl= depthLastNext(-1, root);
        do {
            if (lastMove>0) {
                for(int l=0;l<lastMove;l++) {                	
                	depth++;
            		for(int j=0;j<depth;j++) so.append("\t");
                	so.append("<\n");
                }
            }
            else {
                for(int l=0;l<-lastMove;l++) {
            		for(int j=0;j<depth;j++) so.append("\t");
            		so.append(">\n");
                	depth--;
            }

            }

    		for(int j=0;j<depth;j++) so.append("\t");
    		T ws = node(dl);
    		if (ws==null)    			
                so.append( "NULL node\n");
    		else
    			so.append( ws.toString()+ "\n");
            dl = depthLastNext(dl, root);
        } while(dl>=0);
        
        for(int l=0;l<-lastMove;l++) {
    		for(int j=0;j<depth;j++) so.append("\t");
    		so.append(">\n");
        	depth--;
        }
        return so.toString();
    }
    
    public T node(int nodeIndx) {
        return nodes.get(nodeIndx);
    }

    public T nodeRoot() {
        return nodes.get(root);
    }
    
    public int lastChild(Integer parent) {
        Integer node = firstChild.get(parent);
        if (node==null) 
        	return -1;

        while(nextChild.containsKey(node)){
            node = nextChild.get(node);
        }
        return node;
    }
      
    public int addNode(T node) {
        Integer gindx = ++lastIndex;
        nodes.put(gindx, node);    
        return gindx;
    }
        
    public int addChild(Integer parentNode, T node){
        int gindx = addNode(node);
        parent.put(gindx, parentNode); 
        if (firstChild.containsKey(parentNode)==false) {
            firstChild.put(parentNode,gindx);
        }        
        else {
            Integer last = lastChild(parentNode);
            nextChild.put(last,gindx);
            prevChild.put(gindx,last);
        }            
        return gindx;        
    }
    
      public int addSequence(Integer parentNode, Collection<T> sequence){
          int last=-1, first=-1;
          for(T s : sequence) {
              int i = addNode(s);
              parent.put(i,parentNode);
              if (last>=0) {
                  prevChild.put(i,last);
                  nextChild.put(last,i);
              }
              else
              {
                  first = i;
                  int l = lastChild(parentNode);
                  if (l<0)
                      firstChild.put(parentNode,i);
                  else {
                      nextChild.put(l, i);
                      prevChild.put(i,l);
              }
              }
              last = i;
          }
          return first;
      }
    
    public int move_bottomLeft(int node) {
        while(firstChild.containsKey(node)) {
            lastMove++;
            node = firstChild.get(node);
        }
        return node;
    }
    
    public int move_up(int node){
    	if (parent.containsKey(node))
    		return parent.get(node);
    	else
    		return -1;
    }
    
    public int move_down(int node){
    	if (firstChild.containsKey(node))
    		return firstChild.get(node);
    	else
    		return -1;
    }
    
    
    public int move_right(int node){
    	if (nextChild.containsKey(node))
    		return nextChild.get(node);
    	else
    		return -1;
    }
    
    public int move_left(int node){
    	if (prevChild.containsKey(node))
    		return prevChild.get(node);
    	else
    		return -1;
    }
    
    public int depthFirstNext(int node, int iterationStart) {
        if (node==iterationStart) return -1; // done.
        if (node==-1) return move_bottomLeft(0); // initial depth first.
                
        if (nextChild.containsKey(node)) {
            lastMove = 0;
            return move_bottomLeft(nextChild.get(node));
        }
        else {
            lastMove = -1;
            return parent.get(node);        
        }
    }
    
    public int depthLastNext(int node, int iterationStart) {
        lastMove=0;
        if (node<0) return iterationStart; // first time
        
        if (firstChild.containsKey(node)) {
            lastMove++;
            return firstChild.get(node);
        }
        
        else if (nextChild.containsKey(node)) {
            return nextChild.get(node);
        }
        
        else {
            
            do {
                node = parent.get(node);                                    
                lastMove--;                
                if (node == iterationStart) 
                    return -1;                
                else if (nextChild.containsKey(node))
                    return nextChild.get(node);                
            } while(true);            
        }    
}
    
    public Boolean removeHead (int headNode) {
    	
    	if (nextChild.containsKey(headNode))
    		return false;
    	    	
    	int parentN = move_up(headNode);
    	int childN  = move_down(headNode);

    	if (headNode == root) {
    		root = childN;    	
    		if (childN>0) 
    			parent.remove(childN);
    	}
    	else if (childN>0)
    		firstChild.put(parentN,childN);
    	
    	while(childN>0) {
    		parent.put(childN,parentN);
    		childN = move_right(childN);
    	}
    	return true;
    }
    
     /**
     * pushes under newHead the sub-sequence starting from startNode of length length
     * @param startNode
     * @param length
     * @param newHead
     * @return index of new node
     */
    public int pushDownSequence(int startNode, int length, T newHead) {    
        int leftNode = -1;
        int parentNode = -1;
        int rightNode = startNode;
        int lastSeqNode = startNode;
        
        if (prevChild.containsKey(startNode))
            leftNode = prevChild.get(startNode);
        if (parent.containsKey(startNode))
            parentNode = parent.get(startNode);
                 
        int gindx = ++lastIndex;
        nodes.put(gindx, newHead);       
        firstChild.put(gindx, startNode);
        for(int i=0;i<length;i++) {       	
            parent.put(rightNode, gindx);
            lastSeqNode = rightNode;
            rightNode = move_right(rightNode);                  
        }
        
        if (parentNode>=0) 
        	parent.put(gindx, parentNode);     
                                
        if (leftNode>=0) {
            nextChild.put(leftNode, gindx);
            prevChild.put(gindx, leftNode);
            prevChild.remove(startNode);
        }
        else if (parentNode>=0)
            firstChild.put(parentNode,  gindx);
        else
            root = gindx;
        
        if (rightNode >=0) {
        	nextChild.put(gindx, rightNode);
        	prevChild.put(rightNode, gindx);
        	nextChild.remove(lastSeqNode);
        }
                	
        return gindx;
    }
   
         
    public int nthChild(int father, int n) {
        if (firstChild.containsKey(father)==false)
            return -1;
        int node = firstChild.get(father);
        for(int i=0;i<n;n++) {
            if (nextChild.containsKey(node)==false)
                return -1;
            node = nextChild.get(node);
        }        
        return node;    
    }
    
    
    public int numOfChildren(int node) {
        int children=0;
        if (firstChild.containsKey(node)==false)
            return 0;
        node = firstChild.get(node);
        children = 1;
        while (nextChild.containsKey(node)==true) {
            node = nextChild.get(node);
            children++;
        }
        return children;
    }
    
    public int numOfNextNodes(int node) {
        int next=0;

        while (nextChild.containsKey(node)) {
            node = nextChild.get(node);
            next++;
        }
        return next;
    }

     
}  


