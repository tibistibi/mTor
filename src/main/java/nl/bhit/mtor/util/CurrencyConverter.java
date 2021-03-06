package nl.bhit.mtor.util;

import java.text.DecimalFormat;
import java.text.ParseException;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * This class is converts a Double to a double-digit String
 * (and vise-versa) by BeanUtils when copying properties.
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class CurrencyConverter implements Converter {
	
	private static final transient Logger LOG = Logger.getLogger(CurrencyConverter.class);
    
    private DecimalFormat formatter = new DecimalFormat("###,###.00");

    public void setDecimalFormatter(DecimalFormat df) {
        this.formatter = df;
    }

    /**
     * Convert a String to a Double and a Double to a String
     * 
     * @param type
     *            the class type to output
     * @param value
     *            the object to convert
     * @return object the converted object (Double or String)
     */
    public final Object convert(final Class type, final Object value) {
        // for a null value, return null
        if (value == null) {
            return null;
        } else {
            if (value instanceof String) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("value (" + value + ") instance of String");
                }

                try {
                    if (StringUtils.isBlank(String.valueOf(value))) {
                        return null;
                    }

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("converting '" + value + "' to a decimal");
                    }
                    
                    Number num = formatter.parse(String.valueOf(value));

                    return num.doubleValue();
                } catch (ParseException pe) {
                	LOG.error("Error parsing " + value, pe);
                }
            } else if (value instanceof Double) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("value (" + value + ") instance of Double");
                    LOG.debug("returning double: " + formatter.format(value));
                }

                return formatter.format(value);
            }
        }

        throw new ConversionException("Could not convert " + value + " to " + type.getName() + "!");
    }
}
