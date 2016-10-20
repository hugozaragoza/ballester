package featureTypedSequenceTrees;

public class Test_FTST {
    
    
    public static void main(String[] args) {
    	TFSNode.TestTFSNodes();
 //   	Test_FTST.testTreeSeq();
  //  	Test_FTST.testParse();
    	TFSRewriteRule.TestRewriteRule();
    }
    
    public static void testTreeSeq() {

        TreeSeq<String> t = new TreeSeq<String>("A");
        t.addChild(0, "A1");
        t.addChild(0, "A2");    
        t.addChild(0, "A3");    
        System.out.println("1 : " + t.toString());
        
        t.pushDownSequence(1, 2, "TMP");
        System.out.println("2 : " + t.toString());

        t.pushDownSequence(4, 2, "ON_TMP");
        System.out.println("3 : " + t.toString());

        t.removeHead(5);
        System.out.println("4 : " + t.toString());

        t = new TreeSeq<String>("A");
        t.addChild(0, "A1");
        t.addChild(0, "A2");    
        t.addChild(0, "A3");    
        t.addChild(0, "A4");    
        t.addChild(0, "A5");            
        
        t.pushDownSequence(1, 2, "B6");

        System.out.println("4a : " + t.toString());

        t.pushDownSequence(3, 2, "B7");
        
        System.out.println("4b : " + t.toString());

    }
    
}