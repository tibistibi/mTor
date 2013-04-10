package nl.bhit.mtor.server.webapp.util;

import nl.bhit.mtor.model.User;

import org.apache.log4j.Logger;

/**
 * A utility class for user retrieval from the session (the logged in user).
 * 
 * @author <a href="mailto:tstrausz@tryllian.nl">Tibor Strausz</a>
 */
public final class UserManagementUtils {

    private static final transient Logger LOG = Logger.getLogger(UserManagementUtils.class);

    private UserManagementUtils() {
        // hide me (utility class)
    }

    /**
     * @return the logged in user.
     */
    public static User getAuthenticatedUser() {
    	LOG.trace("retrieving the, loggid in, user from the session.");
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
        LOG.trace("Logged in user:" + result);
        return result;
    }
}