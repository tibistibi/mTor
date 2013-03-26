package nl.bhit.mtor.client.provider;

import junit.framework.TestCase;
import nl.bhit.mtor.model.Status;
import nl.bhit.mtor.model.soap.SoapMessage;
import nl.bhit.mtor.util.ReflectionUtils;

import org.junit.Assert;

public class FreeMemoryMTorMessageProviderTest extends TestCase {

    public void testVirtualMemory() throws Exception {
        final long free = Runtime.getRuntime().freeMemory();
        
        //Using reflection trick to keep constants final.
        //enters (free > ERROR_LIMIT, WARN_LIMIT)
        ReflectionUtils.modifyFinalStaticLongValue(FreeMemoryMTorMessageProvider.class.getCanonicalName(), "ERROR_LIMIT", free - 52428800L);
        ReflectionUtils.modifyFinalStaticLongValue(FreeMemoryMTorMessageProvider.class.getCanonicalName(), "WARN_LIMIT", free - 52428800L);
        
        SoapMessage message = FreeMemoryMTorMessageProvider.getVirtualMemoryMessage();
        Assert.assertNull(message);
        
        //enters (free < ERROR_LIMIT)
        ReflectionUtils.modifyFinalStaticLongValue(FreeMemoryMTorMessageProvider.class.getCanonicalName(), "ERROR_LIMIT", free + 52428800L);
        ReflectionUtils.modifyFinalStaticLongValue(FreeMemoryMTorMessageProvider.class.getCanonicalName(), "WARN_LIMIT", free + 52428800L);
        
        message = FreeMemoryMTorMessageProvider.getVirtualMemoryMessage();
        Assert.assertNotNull(message);
        Assert.assertEquals(Status.ERROR, message.getStatus());
        
        ReflectionUtils.modifyFinalStaticLongValue(FreeMemoryMTorMessageProvider.class.getCanonicalName(), "ERROR_LIMIT", free - 157286400);
        //enters (free < WARN_LIMIT)
        ReflectionUtils.modifyFinalStaticLongValue(FreeMemoryMTorMessageProvider.class.getCanonicalName(), "WARN_LIMIT", free + 157286400);
        
        message = FreeMemoryMTorMessageProvider.getVirtualMemoryMessage();
        Assert.assertNotNull(message);
        Assert.assertEquals(Status.WARN, message.getStatus());

    }
    
}
