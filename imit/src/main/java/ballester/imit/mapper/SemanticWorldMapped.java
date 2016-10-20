/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.imit.mapper;

import java.util.ArrayList;

import com.google.common.collect.HashBiMap;

import ballester.gameworld.agent.Agent;
import ballester.grammar.simplegrammar.types.WordTree;

/**
 * @author hugoz
 *
 */
public class SemanticWorldMapped {

	WordTree semworld;
	ArrayList<Agent> agents;
	HashBiMap<Integer, Integer> hb;// semworld nodenumber <-> Agent

	public SemanticWorldMapped(WordTree sw) {
		semworld = sw;
		agents = new ArrayList<Agent>();
		hb = HashBiMap.create();
	}

}
