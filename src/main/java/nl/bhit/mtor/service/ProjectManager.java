package nl.bhit.mtor.service;

import java.util.List;

import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.User;

public interface ProjectManager extends GenericManager<Project, Long> {

    /**
     * Get project with non resolved messages
     * 
     * @param user
     *            to filter on
     * @return all projects with messages for this user.
     */
    List<Project> getWithNonResolvedMessages(User user);

}