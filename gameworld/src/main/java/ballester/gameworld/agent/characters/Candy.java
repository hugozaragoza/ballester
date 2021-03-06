package ballester.gameworld.agent.characters;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentProperties;
import ballester.gameworld.agent.AgentProperties.PropName;
import ballester.gameworld.agent.AgentWorld;
import ballester.gameworld.agent.Consumable;
import ballester.gameworld.agent.StillAgent;
import ballester.gameworld.agent.GarphicInfo.Shape;

public class Candy extends Agent implements Consumable, StillAgent {

    double normalScale = .2; // shrinking Agent

    public Candy() {
	super();
	// this.graphicInfo.shape = Shape.CIRCL;
    }

    @Override
    public void createInWorld(AgentWorld world) {
	super.createInWorld(world);
	double normalHealth = 20.0;
	this.props.set(AgentProperties.PropName.HEALTH_NORMAL, normalHealth);
	this.props.set(AgentProperties.PropName.HEALTH, normalHealth);
    }

    @Override
    protected void setSize() {
	props.setSizeRelative(normalScale, normalScale / 2);
	this.graphicInfo.drawTitle = false;
    }

    @Override
    public void endOfTurn() {
	double d = this.props.getDouble(PropName.HEALTH);
	d /= this.props.getDouble(PropName.HEALTH_NORMAL);
	d = normalScale * d;
	props.setSizeRelative(d, d / 2);
    }

}
