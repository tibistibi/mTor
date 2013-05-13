package nl.bhit.mtor.service;

import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import nl.bhit.mtor.dao.UserDao;
import nl.bhit.mtor.model.User;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *         Modified by <a href="mailto:dan@getrolling.com">Dan Kibler </a>
 */
@WebService
@Path("/users")
public interface UserManager extends GenericManager<User, Long> {
    /**
     * Convenience method for testing - allows you to mock the DAO and set it on an interface.
     * 
     * @param userDao
     *            the UserDao implementation to use
     */
    void setUserDao(UserDao userDao);

    /**
     * Retrieves a user by userId. An exception is thrown if user not found
     * 
     * @param userId
     *            the identifier for the user
     * @return User
     */
    User getUser(String userId);

    /**
     * Finds a user by their username.
     * 
     * @param username
     *            the user's username used to login
     * @return User a populated user object
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException
     *             exception thrown when user not found
     */
    User getUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * Retrieves a list of all users.
     * 
     * @return List
     */
    List<User> getUsers();

    /**
     * Saves a user's information.
     * 
     * @param user
     *            the user's information
     * @throws UserExistsException
     *             thrown when user already exists
     * @return user the updated user object
     */
    User saveUser(User user) throws UserExistsException;

    /**
     * Removes a user from the database
     * 
     * @param user
     *            the user to remove
     */
    void removeUser(User user);

    /**
     * Removes a user from the database by their userId
     * 
     * @param userId
     *            the user's id
     */
    void removeUser(String userId);

    /**
     * Searches a user for search terms.
     * 
     * @param searchTerm
     *            the search terms.
     * @return a list of matches, or all if no searchTerm.
     */
    List<User> search(String searchTerm);
    
    /**
     * Searches a user who has assigned the QR token. As a security check, it will check if the QR token of the retrieved user fits in a window frame time.
     * 
     * @param token
     * 				QR token we want to search.
     * @return
     * 			User that has assigned this token. Null if no user has this token or the window frame time has been exceeded. 
     */
    User getAndCheckQRUser(String token);
    
    /**
     * Updates a QR token & QR timestamp of a user.
     * 
     * @param username
     * 				Username which it will retrieve the user.
     * @param tokenQR
     * 				Token will be updated in the retrieved user.
     * @return
     */
    @GET
    @Path("/qr/{username}/{token}")
    void updateQRUser(@PathParam("username") String username, @PathParam("token") String tokenQR);
}
