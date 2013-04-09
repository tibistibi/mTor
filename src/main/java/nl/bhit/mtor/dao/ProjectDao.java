package nl.bhit.mtor.dao;

import java.util.List;

import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.User;

/**
 * An interface that provides a data management interface to the Project table.
 */
public interface ProjectDao extends GenericDao<Project, Long> {

    /**
     * Get project with non resolved messages
     * 
     * @param user
     *            to filter on
     * @return all projects with messages for this user.
     */
    List<Project> getWithNonResolvedMessages(User user);

}