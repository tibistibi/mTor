package nl.bhit.mtor.client.provider;

import java.io.File;

import junit.framework.TestCase;
import nl.bhit.mtor.model.Status;
import nl.bhit.mtor.model.soap.SoapMessage;
import nl.bhit.mtor.util.ReflectionUtils;

import org.junit.Assert;

public class DiskSpaceMTorMessageProviderTest extends TestCase {

    public void testDiskSpace() throws Exception {
        File tmp = new File("/");
        long free = tmp.getFreeSpace();
        
        //Using reflection trick to keep constants final.
        ReflectionUtils.modifyFinalStaticLongValue(FreeMemoryMTorMessageProvider.class.getCanonicalName(), "ERROR_LIMIT", free - 100);
        ReflectionUtils.modifyFinalStaticLongValue(FreeMemoryMTorMessageProvider.class.getCanonicalName(), "WARN_LIMIT", free - 100);
        
        SoapMessage message = DiskSpaceMTorMessageProvider.getDiskSpaceMessage();
        Assert.assertNull(message);

        ReflectionUtils.modifyFinalStaticLongValue(FreeMemoryMTorMessageProvider.class.getCanonicalName(), "ERROR_LIMIT", free + 100);
        ReflectionUtils.modifyFinalStaticLongValue(FreeMemoryMTorMessageProvider.class.getCanonicalName(), "WARN_LIMIT", free + 100);

        message = DiskSpaceMTorMessageProvider.getDiskSpaceMessage();
        Assert.assertNotNull(message);
        Assert.assertEquals(Status.ERROR, message.getStatus());

        ReflectionUtils.modifyFinalStaticLongValue(FreeMemoryMTorMessageProvider.class.getCanonicalName(), "ERROR_LIMIT", free - 100);
        ReflectionUtils.modifyFinalStaticLongValue(FreeMemoryMTorMessageProvider.class.getCanonicalName(), "WARN_LIMIT", free + 100);

        message = DiskSpaceMTorMessageProvider.getDiskSpaceMessage();
        Assert.assertNotNull(message);
        Assert.assertEquals(Status.WARN, message.getStatus());

    }

}