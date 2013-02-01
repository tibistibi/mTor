package nl.bhit.webapp.util;

import nl.bhit.model.User;

import org.apache.log4j.Logger;
/*import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;*/

/**
 * A utility class for user retrieval from the session (the logged in user).
 * 
 * @author <a href="mailto:tstrausz@tryllian.nl">Tibor Strausz</a>
 */
public final class UserManagementUtils {

    private static final transient Logger log = Logger.getLogger(UserManagementUtils.class);

    private UserManagementUtils() {
        // hide me (utility class)
    }

    /**
     * @return the logged in user.
     */
    public static User getAuthenticatedUser() {
        log.trace("retrieving the, loggid in, user from the session.");
        User result = null;
        final org.springframework.security.core.context.SecurityContext ctx = org.springframework.security.core.context.SecurityContextHolder.getContext();
        if (ctx.getAuthentication() != null) {
            final org.springframework.security.core.Authentication auth = ctx.getAuthentication();
            if (auth.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
                result = (User) auth.getPrincipal();
            } else if (auth.getDetails() instanceof org.springframework.security.core.userdetails.UserDetails) {
                result = (User) auth.getDetails();
            }
        }
        log.trace("Logged in user:" + result);
        return result;
    }
}