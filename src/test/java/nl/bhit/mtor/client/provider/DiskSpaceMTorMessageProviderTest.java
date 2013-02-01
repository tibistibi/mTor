package nl.bhit.mtor.client.provider;

import java.io.File;

import junit.framework.TestCase;
import nl.bhit.model.Status;
import nl.bhit.model.soap.SoapMessage;

import org.junit.Assert;

public class DiskSpaceMTorMessageProviderTest extends TestCase {

	public void testDiskSpace() throws Exception {
		File tmp = new File("/");
		long free = tmp.getFreeSpace();

		DiskSpaceMTorMessageProvider.ERROR_LIMIT = free - 100;
		DiskSpaceMTorMessageProvider.WARN_LIMIT = free - 100;

		SoapMessage message = DiskSpaceMTorMessageProvider.getDiskSpaceMessage();
		Assert.assertNull(message);

		DiskSpaceMTorMessageProvider.ERROR_LIMIT = free + 100;
		DiskSpaceMTorMessageProvider.WARN_LIMIT = free + 100;

		message = DiskSpaceMTorMessageProvider.getDiskSpaceMessage();
		Assert.assertNotNull(message);
		Assert.assertEquals(Status.ERROR, message.getStatus());

		DiskSpaceMTorMessageProvider.ERROR_LIMIT = free - 100;
		DiskSpaceMTorMessageProvider.WARN_LIMIT = free + 100;

		message = DiskSpaceMTorMessageProvider.getDiskSpaceMessage();
		Assert.assertNotNull(message);
		Assert.assertEquals(Status.WARN, message.getStatus());

	}

}