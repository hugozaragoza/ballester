/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.grammar.examples;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import ballester.datastructures.tree.OrderedTree;
import ballester.grammar.simplegrammar.parse.SimpleParser;
import ballester.grammar.simplegrammar.types.Lexicon;
import ballester.grammar.simplegrammar.types.Word;
import ballester.grammar.simplegrammar.types.WordTree;

/**
 * @author hugoz
 *
 */
public class ExampleApplication {
    public static String line1 = "===============================================";

    public static void main(String[] args) {
	initLogs();

	OrderedTree.displayNodeIndices = true;
	Word.displayNodeIndices = false;

	Lexicon lexicon = Lexicon.getLexicon();

	String[] story = { "a green dragon", "The knight was bored", "The dragon attacked the princess" };

	WordTree sw = new WordTree();
	for (String s : story) {
	    System.out.println("\n\n" + line1 + "\n" + s + "\n");

	    // Parse 0 and 1
	    sw = SimpleParser.parse(s.split(" "), lexicon);
	    System.out.println(sw);

	    // Parse 2
	    // TODO parse2

	    // Parse 3
	    // TODO parse3

	}

    }

    public static void initLogs() {
	ConsoleAppender console = new ConsoleAppender(); // create appender
	// configure the appender
	String PATTERN = "%d [%p|%c|%C{1}] %m%n";
	console.setLayout(new PatternLayout(PATTERN));
	console.setThreshold(Level.FATAL);
	console.activateOptions();
	// add appender to any Logger (here is root)
	Logger.getRootLogger().addAppender(console);

	FileAppender fa = new FileAppender();
	fa.setName("FileLogger");
	fa.setFile("mylog.log");
	fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
	fa.setThreshold(Level.DEBUG);
	fa.setAppend(true);
	fa.activateOptions();

	// add appender to any Logger (here is root)
	Logger.getRootLogger().addAppender(fa);
	// repeat with all other desired appenders
    }

}
