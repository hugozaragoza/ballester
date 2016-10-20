/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.gameworld.agent;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ballester.gameworld.agent.AgentProperties.PropName;
import ballester.gameworld.agent.emotionalstate.EmotionalState;

/**
 * 
 * Some properties are typed for programming ease, the rest go to a free
 * key,value store 'extra'.
 * 
 * @author hugoz
 *
 *
 */
public class AgentProperties {
    static Logger logger = Logger.getLogger(AgentProperties.class);

    public static final int DEFAULTDURATION_emotionalStateForced = 5;

    public enum PropName {
	HEALTH_NORMAL, HEALTH, SIZEH, SIZEW, COLOR, EXISTS
    };

    public HashMap<String, Object> properties;

    public AgentProperties() {
	properties = new HashMap<String, Object>();
    }

    /**
     * @param delta
     * @throws Exception
     */
    public void updateAgentProperties(AgentProperties delta) throws Exception {
	logger.debug("UPDATING THE FOLLOWING PROPERTIES:\n" + StringUtils.join(delta.properties, "\n", "=>"));
	this.properties.putAll(delta.properties);
	logger.debug("RESULTING PROPERTIES:\n" + StringUtils.join(this.properties, "\n", "=>"));
    }

    public void inc(String propName, double d) {
	Object o = properties.get(propName);
	if (o == null) {
	    properties.put((propName), d);
	} else {
	    d += (Double) o;
	    set(propName, d);
	}
    }

    public void inc(PropName propName, double d) {
	inc(name(propName), d);
    }

    private boolean getBoolean(PropName propName) {
	Boolean b = (Boolean) properties.get(name(propName));
	return b != null && b;
    }

    public Double getDouble(PropName propName) {
	return (Double) properties.get(name(propName));
    }

    public String getString(String propName) {
	return (String) properties.get((propName));
    }

    public String getString(PropName propName) {
	return getString(name(propName));
    }

    public void set(String propName, Object o) {
	properties.put(propName, o);
    }

    public void set(PropName prop, Object o) {
	properties.put(name(prop), o);
    }

    /**
     * @param prop
     * @return
     */
    private String name(PropName prop) {
	return prop.name().toLowerCase();
    }

    // DEDICATED:

    public boolean isDead() {
	return getDouble(PropName.HEALTH) <= 0;
    }

    public String getColor() {
	return getString(PropName.COLOR);
    }

    public Double getHealth() {
	return getDouble(PropName.HEALTH);
    }

    public boolean isExists() {
	return getBoolean(PropName.EXISTS);
    }

    /**
     * @param semGrounded
     */
    public void remove(String semGrounded) {
	properties.remove(semGrounded);
    }

}
