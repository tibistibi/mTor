package nl.bhit.mtor.client.provider;

import java.io.File;

import junit.framework.TestCase;
import nl.bhit.mtor.model.Status;
import nl.bhit.mtor.model.soap.SoapMessage;

import org.junit.Assert;

public class DiskSpaceMTorMessageProviderTest extends TestCase {

    public void testDiskSpace() throws Exception {
        File tmp = new File("/");
        long free = tmp.getFreeSpace();
        
        //Using reflection trick to keep constants final.
        DiskSpaceMTorMessageProvider.setErrorLimit(free - 100);
        DiskSpaceMTorMessageProvider.setWarnLimit(free - 100);
        
        SoapMessage message = DiskSpaceMTorMessageProvider.getDiskSpaceMessage();
        Assert.assertNull(message);

        DiskSpaceMTorMessageProvider.setErrorLimit(free + 100);
        DiskSpaceMTorMessageProvider.setWarnLimit(free + 100);

        message = DiskSpaceMTorMessageProvider.getDiskSpaceMessage();
        Assert.assertNotNull(message);
        Assert.assertEquals(Status.ERROR, message.getStatus());

        DiskSpaceMTorMessageProvider.setErrorLimit(free - 100);
        DiskSpaceMTorMessageProvider.setWarnLimit(free + 100);
        
        message = DiskSpaceMTorMessageProvider.getDiskSpaceMessage();
        Assert.assertNotNull(message);
        Assert.assertEquals(Status.WARN, message.getStatus());

    }

}