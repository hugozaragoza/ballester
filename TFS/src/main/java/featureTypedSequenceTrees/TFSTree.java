package featureTypedSequenceTrees;
import java.util.*;

       
/**
 * A TFST tree (ordered chilfren, nodes are TFSNode.
 * @author hugoz
 *
 */
public class TFSTree extends TreeSeq<Word> {
    
    public TFSTree(Word label) {
        super(label);
    }
    
    public TFSTree(Word label, Vector<Word> nodeSequence) {
        super(label);
        addSequence(root,nodeSequence);
    }  
        
    public TFSTree(Word label, Word[] nodeSequence) {
        super(label);
        Vector<Word> v = new Vector<Word>();
        for (Word n : nodeSequence)
            v.add(n);        
        addSequence(root,v);
    }  

        
    
    
}  


 