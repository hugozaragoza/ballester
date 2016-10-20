package ballester.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import ballester.datastructures.fs.FSDao;

public class UnitTestUtils {
	final private static Logger logger = Logger.getLogger(FSDao.class);

	public static String readResourceAsString(Class classForNamespace, String name) throws IOException {
		URL resourceUrl = classForNamespace.getResource(name);

		if (resourceUrl == null) {
			logger.error("Could not find resource: " + name);
		}
		InputStream input = resourceUrl.openStream();
		String ret = IOUtils.toString(input, "UTF8");
		IOUtils.closeQuietly(input);
		return ret;
	}
}
