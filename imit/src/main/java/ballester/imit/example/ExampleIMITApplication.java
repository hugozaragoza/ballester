/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.imit.example;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import ballester.datastructures.tree.OrderedTree;
import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentProperties;
import ballester.gameworld.agent.AgentWorld;
import ballester.gameworld.agent.AgentProperties.PropName;
import ballester.gameworld.agent.actions.Action;
import ballester.gameworld.agent.actions.ActionSequence;
import ballester.gameworld.agent.characters.Candy;
import ballester.gameworld.agent.characters.Dragon;
import ballester.gameworld.control.Script;
import ballester.gameworld.renderer.GraphicRenderer1;
import ballester.gameworld.renderer.Narrator;
import ballester.grammar.simplegrammar.parse.SimpleParser;
import ballester.grammar.simplegrammar.types.Lexicon;
import ballester.grammar.simplegrammar.types.Word;
import ballester.imit.game.ImitGameController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.web.WebViewBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 * @author hugoz
 *
 */
@SuppressWarnings("restriction")
public class ExampleIMITApplication extends Application {
    final private static Logger logger = Logger.getLogger(ExampleIMITApplication.class);

    {

	Logger.getRootLogger().setLevel(Level.INFO);
	// Logger.getLogger(SimpleParser.class).setLevel(Level.TRACE);
	// Logger.getLogger(Action.class).setLevel(Level.DEBUG);
	Logger.getLogger(Action.class.getPackage().getName()).setLevel(Level.DEBUG);
	Logger.getLogger(ActionSequence.class).setLevel(Level.TRACE);
	// Logger.getLogger(AgentProperties.class).setLevel(Level.DEBUG);
	// Logger.getLogger(GraphicRenderer1.class).setLevel(Level.DEBUG);
	// Logger.getLogger(Narrator.class).setLevel(Level.DEBUG);
    }

    private static final double TOT_W = 1200;
    private static final double TOT_H = 650;
    private static final double INTERVAL_BACKGROUNDGAME1 = 2000;

    private boolean display_fullScreen = false;

    private static boolean debug = true;
    private static boolean paused = false;

    ImitGameController game;
    private ArrayList<String> prestory;
    private int epoch = 0;
    int runClycle = 0;
    private Pane gamePane;
    private Pane sentenceWorldPane;
    private GraphicRenderer1 rendererL;
    private GraphicRenderer1 rendererC;
    private WebView narratePane;
    private TextField sentenceText;
    private Timeline backgroundGame;
    private WebView lexicPane;
    private KeyFrame kf;
    private double backgroundGame_originalRate;

    {
	OrderedTree.displayNodeIndices = true;
	Word.displayNodeIndices = true;
    }

    void restartGame() {
	game.restart();
    }

    /**
     * @throws Exception
     * 
     */
    void initGame() throws Exception {
	rendererL = new GraphicRenderer1(sentenceWorldPane, narratePane);
	rendererC = new GraphicRenderer1(gamePane, narratePane);
	rendererC.setDebugOff();
	debug = false;
	double shr = 6. / 10.;
	game = new ImitGameController(rendererC, new MyScript(), TOT_W * shr, TOT_H * shr);

	// DISPLAY WORDS:
	lexicPane.getEngine().loadContent(game.lexicon.info1());

	// GAME ANIMATION:
	kf = new KeyFrame(Duration.millis(INTERVAL_BACKGROUNDGAME1), new EventHandler<ActionEvent>() {

	    @Override
	    public void handle(ActionEvent event) {
		System.out.println("RUN CYCLE E" + epoch + " C" + (++runClycle));
		try {
		    game.runCycle();
		} catch (Exception e) {
		    logger.error(e, e);
		}
	    }
	});
	backgroundGame = new Timeline(kf);
	backgroundGame.setCycleCount(Timeline.INDEFINITE);
	backgroundGame_originalRate = backgroundGame.getRate();
	if (!paused) {
	    backgroundGame.play();
	}
    }

    synchronized void interpret() throws Exception {
	if (!paused) {
	    backgroundGame.pause();
	}

	String storyLine = sentenceText.getText();
	game.interpret(storyLine, rendererL);

	if (!paused) {
	    backgroundGame.play();
	}
    }

    void endGame() {
	game.endWorld();
	System.out.println("END OF GAME");
	Platform.exit();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
	Button btn;

	prestory = new ArrayList<>();
	prestory.add("a green princess");
	prestory.add("the princess was hungry");

	prestory.add("a blue princess");
	prestory.add("a green princess");

	prestory.add("the princess is red");
	prestory.add("there was a princess");
	prestory.add("the princess was hungry");
	prestory.add("there was a dragon");
	prestory.add("the princess had a bow");

	// SETUP SCENE:
	primaryStage.setTitle("IMIT 0.0.0");
	BorderPane mainPane = new BorderPane();

	// MAIN PANE

	TabPane tabPane = new TabPane();
	mainPane.setCenter(tabPane);

	tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

	narratePane = new WebView();
	Tab tab;

	gamePane = new Pane();
	tab = new Tab();
	tab.setText("Game World");
	tab.setContent(gamePane);
	tabPane.getTabs().add(tab);

	tab = new Tab();
	tab.setText("Narrator");
	tab.setContent(narratePane);
	tabPane.getTabs().add(tab);

	sentenceWorldPane = new Pane();
	tab = new Tab();
	tab.setText("Sentence World");
	tab.setContent(sentenceWorldPane);
	tabPane.getTabs().add(tab);

	lexicPane = new WebView();
	tab = new Tab();
	tab.setText("Dictionary");
	tab.setContent(lexicPane);
	tabPane.getTabs().add(tab);

	// TOP PANE:
	BorderPane topPane = new BorderPane();
	mainPane.setTop(topPane);

	// TOP PANE: LEFT : Buttons and Sentence
	HBox leftTop = new HBox();
	topPane.setLeft(leftTop);

	// Sentence pane:
	sentenceText = new TextField(prestory.remove(0));
	sentenceText.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		try {
		    interpret();

		    if (prestory.size() > 0) {
			sentenceText.setText(prestory.remove(0));
		    } else {
			sentenceText.setText("");
		    }

		    epoch++;
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	});
	leftTop.getChildren().add(sentenceText);

	// TOP PANE: RIGHT : Buttons

	HBox buttons = new HBox();
	topPane.setRight(buttons);

	ToggleButton tbtn = new ToggleButton("►");
	tbtn.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		try {
		    paused = !paused;
		    backgroundGame.setRate(backgroundGame_originalRate);
		    if (paused) {
			backgroundGame.pause();
		    } else {
			backgroundGame.play();
		    }
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	});
	tbtn.setSelected(!paused);
	buttons.getChildren().add(tbtn);

	btn = new Button("►►");
	btn.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		try {
		    backgroundGame.setRate(backgroundGame.getRate() * 2.0);
		    if (paused) {
			paused = false;
			backgroundGame.play();
		    }

		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	});
	buttons.getChildren().add(btn);

	// btn = new Button();
	// btn.setText("❚❚");
	// btn.setOnAction(new EventHandler<ActionEvent>() {
	// @Override
	// public void handle(ActionEvent event) {
	// try {
	// backgroundGame.pause();
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// });
	// buttons.getChildren().add(btn);

	btn = new Button();
	btn.setText("▣");
	btn.setOnAction(new EventHandler<ActionEvent>() {

	    @Override
	    public void handle(ActionEvent event) {
		try {
		    display_fullScreen = !display_fullScreen;
		    primaryStage.setFullScreen(display_fullScreen);
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	});
	buttons.getChildren().add(btn);

	tbtn = new ToggleButton("DEBUG");
	tbtn.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		try {
		    debug = !debug;
		    rendererC.DISPLAY_EMMOTSTATE = debug;
		    rendererC.DISPLAY_INVENTORY = debug;
		    rendererC.DISPLAY_STATS = debug;
		    rendererC.DISPLAY_RANGES = debug;

		    rendererL.DISPLAY_EMMOTSTATE = debug;
		    rendererL.DISPLAY_INVENTORY = debug;
		    rendererL.DISPLAY_STATS = debug;

		    game.draw();

		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	});
	buttons.getChildren().add(tbtn);

	btn = new Button();
	btn.setText("RESTART");
	btn.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		try {
		    restartGame();
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	});
	buttons.getChildren().add(btn);

	btn = new Button();
	btn.setText("END");
	btn.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		endGame();
	    }
	});
	buttons.getChildren().add(btn);

	// INIT MAIN SCENE
	Scene scene = new Scene(mainPane, TOT_W, TOT_H);
	URL url = getClass().getClassLoader().getResource("main.css");
	scene.getStylesheets().add(url.toString());

	primaryStage.setScene(scene);
	primaryStage.show();
	primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	    @Override
	    public void handle(WindowEvent event) {
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
			System.out.println("Application ended by closing the window");
			Platform.exit();
		    }
		});
	    }
	});

	initGame();

    }

}

class MyScript implements Script {

    @Override
    public void addCharacters(AgentWorld world, int epoch) {
	Random rnd = new Random();
	if (epoch == 0) {
	    for (int i = 0; i < 100; i++) {
		Candy c = new Candy();
		world.createAgent(c, true);
		int ci = rnd.nextInt(Lexicon.colorNames.length);
		c.props.set(PropName.COLOR, "orange");
	    }
	    Dragon d = new Dragon();
	    world.createAgent(d, true);
	}
    }

}
