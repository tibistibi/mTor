package nl.bhit.mtor.util;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;

public class DateUtilTest extends TestCase {
	
	private static final transient Logger LOG = Logger.getLogger(DateUtilTest.class);

    // ~ Constructors ===========================================================

    public DateUtilTest(String name) {
        super(name);
    }

    public void testGetInternationalDatePattern() {
        LocaleContextHolder.setLocale(new Locale("nl"));
        assertEquals("dd-MMM-yyyy", DateUtil.getDatePattern());

        LocaleContextHolder.setLocale(Locale.FRANCE);
        assertEquals("dd/MM/yyyy", DateUtil.getDatePattern());

        LocaleContextHolder.setLocale(Locale.GERMANY);
        assertEquals("dd.MM.yyyy", DateUtil.getDatePattern());

        // non-existant bundle should default to default locale
        LocaleContextHolder.setLocale(new Locale("fi"));
        String fiPattern = DateUtil.getDatePattern();
        LocaleContextHolder.setLocale(Locale.getDefault());
        String defaultPattern = DateUtil.getDatePattern();

        assertEquals(defaultPattern, fiPattern);
    }

    public void testGetDate() throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("db date to convert: " + new Date());
        }

        String date = DateUtil.getDate(new Date());

        if (LOG.isDebugEnabled()) {
            LOG.debug("converted ui date: " + date);
        }

        assertTrue(date != null);
    }

    public void testGetDateTime() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("entered 'testGetDateTime' method");
        }
        String now = DateUtil.getTimeNow(new Date());
        assertTrue(now != null);
        LOG.debug(now);
    }

    public void testGetDateWithNull() {
        final String date = DateUtil.getDate(null);
        assertEquals("", date);
    }

    public void testGetDateTimeWithNull() {
        final String date = DateUtil.getDateTime(null, null);
        assertEquals("", date);
    }

    public void testGetToday() throws ParseException {
        assertNotNull(DateUtil.getToday());
    }
}
