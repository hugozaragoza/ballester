package ballester.gameworld.renderer;

import org.apache.commons.lang3.StringUtils;

public class ConsoleWriter  {
	private int level;

	public ConsoleWriter(int level) {
		this.level=level;
	}

	public ConsoleWriter() {
		this.level=1;
	}

	int shift = 0;
	
	public ConsoleWriter print(String msg) {
		if (level==0) return this;
		System.out.println(StringUtils.repeat(" ", shift) +msg);
		return this;
	}
	
	public ConsoleWriter indent() {		
		shift+=2;
		return this;
	}

	public ConsoleWriter deIndent() {
		shift-=2;
		return this;
	}

}
