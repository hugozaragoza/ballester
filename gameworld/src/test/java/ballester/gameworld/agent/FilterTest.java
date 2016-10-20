package ballester.gameworld.agent;

import org.junit.Assert;
import org.junit.Test;

import ballester.gameworld.agent.characters.Candy;
import ballester.gameworld.agent.characters.Dragon;
import ballester.gameworld.agent.characters.Princess;

public class FilterTest {

	class Princess2 extends Princess {

		public Princess2() {
			super();
		}
	}

	@Test
	public void test1_class() {

		Dragon d = new Dragon();

		Princess p = new Princess();

		AgentFilter f = new AgentFilter(Dragon.class, null);
		f.agentClass = Dragon.class;
		Assert.assertTrue(!f.test(p));
		Assert.assertTrue(f.test(d));

		f.agentClass = Princess.class;

		Assert.assertTrue(f.test(p));
		Assert.assertTrue(!f.test(d));

		// Accept subclasses of filter
		Princess2 pp = new Princess2();
		Assert.assertTrue(f.test(p));
		Assert.assertTrue(f.test(pp));

		// Restrict to subclass
		f.agentClass = Princess2.class;
		Assert.assertFalse(f.test(p));
		Assert.assertTrue(f.test(pp));

	}

	@Test
	public void test1_interface() {

		Candy c = new Candy();
		Princess p = new Princess();

		AgentFilter f = new AgentFilter(null, Consumable.class);
		Assert.assertTrue(!f.test(p));
		Assert.assertTrue(f.test(c));

	}
}
