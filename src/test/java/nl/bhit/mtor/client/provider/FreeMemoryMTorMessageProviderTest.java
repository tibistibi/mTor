package nl.bhit.mtor.client.provider;

import java.io.File;

import junit.framework.TestCase;
import nl.bhit.mtor.model.Status;
import nl.bhit.mtor.model.soap.SoapMessage;

import org.junit.Assert;

public class FreeMemoryMTorMessageProviderTest extends TestCase {

    public void testVirtualMemory() throws Exception {
        final long free = Runtime.getRuntime().freeMemory();

        FreeMemoryMTorMessageProvider.ERROR_LIMIT = free - 52428800L; // enters (free > ERROR_LIMIT, WARN_LIMIT)
        FreeMemoryMTorMessageProvider.WARN_LIMIT = free - 52428800L;

        SoapMessage message = FreeMemoryMTorMessageProvider.getVirtualMemoryMessage();
        Assert.assertNull(message);

        FreeMemoryMTorMessageProvider.ERROR_LIMIT = free + 52428800L; // enters (free < ERROR_LIMIT)
        FreeMemoryMTorMessageProvider.WARN_LIMIT = free + 52428800L;

        message = FreeMemoryMTorMessageProvider.getVirtualMemoryMessage();
        Assert.assertNotNull(message);
        Assert.assertEquals(Status.ERROR, message.getStatus());

        FreeMemoryMTorMessageProvider.ERROR_LIMIT = free - 157286400;
        FreeMemoryMTorMessageProvider.WARN_LIMIT = free + 157286400; // enters (free < WARN_LIMIT)

        message = FreeMemoryMTorMessageProvider.getVirtualMemoryMessage();
        Assert.assertNotNull(message);
        Assert.assertEquals(Status.WARN, message.getStatus());

    }

}
