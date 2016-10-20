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
	Point2D tl = absPoint(new Point(0, 0));
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
	Double h = a.props.getDouble(AgentProperties.PropName.SIZEH);
	Double w = a.props.getDouble(AgentProperties.PropName.SIZEH);
	if (h == null)
	    h = AGENT_bh;
	if (w == null)
	    w = AGENT_bw;
	Point2D tl = absCenter(a.point, w, h);

	double y = tl.getY();
	double x = tl.getX();
	Shape r;
	switch (a.graphicInfo.shape) {
	case CIRCL:
	    r = new Ellipse(x, y, w, h);
	    break;

	case RECT: // default
	    r = new Rectangle(x, y, w, h);
	    break;

	default:
	    logger.error("UNKNOWN shape: " + a.graphicInfo.shape.name());
	    r = new Rectangle(tl.getX(), tl.getY(), w, h);
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

	boolean debugThisObject = !(a instanceof StillAgent);
	if (DISPLAY_RANGES && debugThisObject) {
	    Point2D c = absCenter(a.point, 0, 0);
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
	y = sb.getMaxY();

	if (a.graphicInfo.drawTitle) {
	    // String title = a.getClass().getSimpleName();
	    // Text text = new Text(x, y, title);
	    // gamePane.getChildren().add(text);
	    // y += GAP * 2;
	    gamePane.getChildren().add(oneLetterIcon(tl, sb, a));
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
     * @param point
     * @return
     */
    private Point2D absCenter(Point point, double w, double h) {
	Point2D p = new Point2D(point.getX(), point.getY());
	p = p.add(-w / 2.0, -h / 2.0); // go to center
	p = p.multiply(SCENE_SCALE);
	p = p.add(SCENE_OFFSET);
	return p;
    }

    /**
     * @param hPoint
     * @return
     */
    private Point2D absPoint(Point hPoint) {
	return absCenter(hPoint, 0.0, 0.0);
    }

    /**
     * @param tl
     * @param s
     * @param alias
     */
    private Text oneLetterIcon(Point2D tl, Bounds sb, Agent a) {
	String alias = a.getClass().getSimpleName().substring(0, 1).toUpperCase();
	Text textAlias = new Text(sb.getMinX(), sb.getMaxY(), alias);

	// double scalex = tb.getWidth() / sb.getWidth();
	// textAlias.setScaleX(scalex);
	// textAlias.setScaleY(scalex);
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
