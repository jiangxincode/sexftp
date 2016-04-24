package sexftp.uils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class StringUtilTest {

	@Test
	public void testIso88591() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBakFromiso88591() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetBytes() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadExceptionDetailInfo() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReplaceAll() {
		// fail("Not yet implemented");
	}

	@Test
	public void testSplit() {
		// fail("Not yet implemented");
	}

	@Test
	public void testFileStyleMatchStringStringArray() {
		// fail("Not yet implemented");
	}

	@Test
	public void testFileStyleEIMatch() {
		// fail("Not yet implemented");
	}

	@Test
	public void testFileStyleMatchStringString() {
		// fail("Not yet implemented");
	}

	@Test
	public void testSimpString() {
		// fail("Not yet implemented");
	}

	@Test
	public void testDeepClone() {
		// fail("Not yet implemented");
	}

	@Test
	public void testElse() throws UnknownHostException {
		Set<Entry<Object, Object>> entrySet = System.getProperties().entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		System.out.println(InetAddress.getLocalHost().toString());
		System.out.println(FileUtils.byteCountToDisplaySize(1024));
	}

}
