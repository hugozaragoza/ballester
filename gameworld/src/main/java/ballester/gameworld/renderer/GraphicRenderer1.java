package ballester.gameworld.renderer;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentProperties;
import ballester.gameworld.agent.AgentWorld;
import ballester.gameworld.agent.StillAgent;
import ballester.gameworld.agent.actions.Action;
import ballester.gameworld.physics.Geometry;
import ballester.gameworld.physics.HPoint;
import ballester.gameworld.physics.Point;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

@SuppressWarnings("restriction")
public class GraphicRenderer1 extends Renderer {

    private static Logger logger = Logger.getLogger(GraphicRenderer1.class);

    // GLOBAL CONSTANTS:
    private static final Font debugFont = Font.font("Courier", 10);
    private static final Font actionNoiseFont = Font.font("Courier", 14);

    // DRAWING AGENTS:
    private static final double AGENT_bw = 100;
    private static final double AGENT_bh = 60;
    private static final double AGENT_OPACITY = 0.5;
    private static final double COLORLESS_GREY = 0.5;
    public final double GAP = 5;
    private static final double LINEMARGIN = 2;

    // SCENE:
    double SCENE_SCALE = 1.0;
    Point2D SCENE_OFFSET = new Point2D(10.0, 10.0);

    // DEBUG
    public boolean DISPLAY_RANGES = false;
    public boolean DISPLAY_MOOD = false;
    public boolean DISPLAY_STATS = false;
    public boolean DISPLAY_EMMOTSTATE = false;
    public boolean DISPLAY_INVENTORY = false;

    Pane gamePane;
    WebView narratePane;

    public GraphicRenderer1(Pane pane, WebView narratePane) {
	this.gamePane = pane;
	this.narratePane = narratePane;
    }

    @Override
    public void drawBox(AgentWorld w) {
	Point2D tl = topleft(new Point(0, 0));
	Rectangle r = new Rectangle(tl.getX(), tl.getY(), w.width, w.height);
	r.setStroke(Color.BLUE);
	r.setStrokeWidth(1);
	r.setFill(null);
	gamePane.getChildren().add(r);
    }

    @Override
    public void initScene() {
	gamePane.getChildren().clear(); // MAP and MOVE instead of clearing
					// every
    }

    @Override
    public void draw(Agent a) {
	if (a.point == null) {
	    return;
	}
	if (a.isInInventory()) {
	    return;
	}

	if (logger.isDebugEnabled()) {
	    String str = "RENDERER [" + name + "]: DRAW AGENT [" + a.getClass().getSimpleName() + "] at [" + a.point
		    + "]" + //
		    "  HP=" + String.format("%3.1f", a.props.getHealth());
	    logger.debug(str);
	}

	// int pos;
	// if (screen.size() < 1)
	// screen.add(new String[LEN]);
	//

	Double w = a.props.getSize().getX();
	Double h = a.props.getSize().getY();
	Point2D tlB = topleft(a.point, w, h);
	Point2D tlC = topleft(a.point, 0, 0);

	// // TESTING:
	// Shape ss = new Rectangle(tl.getX(), tl.getY(), 20, 30);
	// ss.setFill(null);
	// ss.setStroke(Color.RED);
	// ss.setStrokeWidth(1.0);
	// gamePane.getChildren().add(ss);
	//
	// ss = new Ellipse(tl.getX(), tl.getY(), 20, 30);
	// ss.setFill(null);
	// ss.setStroke(Color.GREEN);
	// ss.setStrokeWidth(1.0);
	// gamePane.getChildren().add(ss);

	Shape r;
	switch (a.graphicInfo.shape) {
	case CIRCL:
	    r = new Ellipse(tlC.getX(), tlC.getY(), w, h);
	    break;

	case RECT: // default
	    r = new Rectangle(tlB.getX(), tlB.getY(), w, h);
	    break;
	default:
	    logger.error("UNKNOWN shape: " + a.graphicInfo.shape.name());
	    r = new Rectangle(tlB.getX(), tlB.getY(), w, h);
	    break;
	}
	Color color = color(a.props.getColor());
	if (color == null) {
	    color = Color.gray(COLORLESS_GREY);
	}
	r.setFill(color);
	r.setOpacity(AGENT_OPACITY);
	// r.setStroke(Color.BLUE);
	// r.setStrokeWidth(1);
	gamePane.getChildren().add(r);

	// HEALTH BAR
	Shape healthBar = drawHealthBath(tlB, w, h, a.props.getHealth(), a.props.getHealthNormal());
	gamePane.getChildren().add(healthBar);

	boolean debugThisObject = true;// !(a instanceof StillAgent);
	if (DISPLAY_RANGES && debugThisObject) {
	    Point2D c = topleft(a.point, 0, 0);
	    ArrayList<Shape> ranges = new ArrayList<>();
	    ranges.add(new Ellipse(c.getX(), c.getY(), Action.D_VERYNEAR, Action.D_VERYNEAR));
	    ranges.add(new Ellipse(c.getX(), c.getY(), Action.D_NEAR, Action.D_NEAR));
	    ranges.add(new Ellipse(c.getX(), c.getY(), Action.D_FAR, Action.D_FAR));
	    ranges.add(new Ellipse(c.getX(), c.getY(), Action.D_VERYFAR, Action.D_VERYFAR));

	    for (Shape s : ranges) {
		s.setFill(null);
		s.setStroke(Color.LIGHTBLUE);
		gamePane.getChildren().add(s);
	    }
	}
	Bounds sb = r.getBoundsInLocal();
	double y = sb.getMaxY();
	double x = sb.getMinX();

	if (a.graphicInfo.drawTitle) {
	    // String title = a.getClass().getSimpleName();
	    // Text text = new Text(x, y, title);
	    // gamePane.getChildren().add(text);
	    // y += GAP * 2;
	    gamePane.getChildren().add(oneLetterIcon(tlC, a));
	}

	if (a.lastAction != null && a.lastAction.getDisplay_action_noise()) {
	    Text text = new Text(x, y, a.lastAction.display_action);
	    Bounds b = text.getBoundsInLocal();
	    text.setY(text.getY() + b.getHeight() + LINEMARGIN);
	    text.setFont(actionNoiseFont);
	    text.setFill(Color.gray(.5));
	    gamePane.getChildren().add(text);
	    y += GAP * 2;
	}

	String debug = "";
	if (DISPLAY_STATS && debugThisObject && a.props.getDouble(AgentProperties.PropName.HEALTH) != null) {
	    debug += "[HP:" + String.format("%3.1f", a.props.getDouble(AgentProperties.PropName.HEALTH)) + "] ";
	}
	// EMOTIONAL STATE:
	if (DISPLAY_EMMOTSTATE && debugThisObject) {
	    String es = (a.emotionalState == null ? "-" : a.emotionalState.getClass().getSimpleName().toLowerCase());
	    debug += " (" + es + ") ";
	}

	if (debug.length() > 0) {
	    Text text = new Text(x, y, debug);
	    text.setFont(debugFont);
	    text.setOpacity(0.5);
	    gamePane.getChildren().add(text);
	    y += GAP * 2;
	}

	// HAS:
	if (DISPLAY_INVENTORY && debugThisObject && a.getInventory().size() > 0) {
	    ArrayList<String> lines = new ArrayList<>();
	    lines.add("HAS:");
	    for (Agent ai : a.getInventory()) {
		lines.add(ai.getClass().getSimpleName().toLowerCase());
	    }
	    String s = StringUtils.join(lines);
	    Text text = new Text(x, y, s);
	    gamePane.getChildren().add(text);
	}

	return;

    }

    /**
     * @param tlB
     * @param w
     * @param h
     * @param health
     * @param healthNormal
     * @return
     */
    private Shape drawHealthBath(Point2D tlB, Double w, Double h, Double health, Double healthNormal) {
	double HEALTHBAR_H = 5;
	double ratio = health / healthNormal;
	double healthw = ratio * w;
	Rectangle r = new Rectangle(tlB.getX(), tlB.getY() - HEALTHBAR_H, healthw, HEALTHBAR_H);
	Color c = ratio > 0.5 ? Color.GREEN : (ratio > 0.3 ? Color.ORANGE : Color.RED);
	r.setFill(c);
	r.setStrokeWidth(0.0);
	return r;
    }

    /**
     * @param point
     * @return
     */
    private Point2D topleft(Point point, double w, double h) {
	Point2D p = new Point2D(point.getX(), point.getY());
	p = p.add(-w / 2.0, -h / 2.0); // go to topleft
	p = p.multiply(SCENE_SCALE);
	p = p.add(SCENE_OFFSET);
	return p;
    }

    /**
     * @param hPoint
     * @return
     */
    private Point2D topleft(Point hPoint) {
	return topleft(hPoint, 0.0, 0.0);
    }

    /**
     * @param tl
     * @param s
     * @param alias
     */
    private Text oneLetterIcon(Point2D tl, Agent a) {
	String alias = a.getClass().getSimpleName().substring(0, 1).toUpperCase();
	Text textAlias = new Text(tl.getX(), tl.getY(), alias);
	Bounds sb = textAlias.getBoundsInLocal();
	textAlias.setX(textAlias.getX() - sb.getWidth() / 2.0);
	textAlias.setY(textAlias.getY() + sb.getHeight() / 2.0);
	return textAlias;
    }

    /**
     * @param color
     * @return
     */
    private Color color(String color) {
	if (color == null) {
	    return null;
	}
	Color c = null;
	try {
	    c = (Color) Color.class.getDeclaredField(color.toUpperCase()).get(null);
	} catch (Exception e) {
	    logger.error(e, e);
	}
	return c;
    }

    @Override
    public void render() {
	gamePane.requestLayout();
	narratePane.requestLayout();
    }

    @Override
    public void drawStory(String string) {
	string = string.replaceAll("\n", "</p>\n\n");
	narratePane.getEngine().loadContent(HTML_HEADER + string);
    }

    String HTML_HEADER = "<head><script language=\"javascript\" type=\"text/javascript\">" + //
	    "function toBottom(){ window.scrollTo(0, document.body.scrollHeight);  }" + //
	    "</script></head><body onload='toBottom()'>";

    public void setDebugOff() {
	DISPLAY_EMMOTSTATE = false;
	DISPLAY_INVENTORY = false;
	DISPLAY_STATS = false;
    }

}
