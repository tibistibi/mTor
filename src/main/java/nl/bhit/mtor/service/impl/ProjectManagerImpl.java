package nl.bhit.mtor.service.impl;

import java.util.List;

import nl.bhit.mtor.dao.ProjectDao;
import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.User;
import nl.bhit.mtor.service.ProjectManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("projectManager")
public class ProjectManagerImpl extends GenericManagerImpl<Project, Long> implements ProjectManager {
    ProjectDao projectDao;

    @Autowired
    public ProjectManagerImpl(ProjectDao projectDao) {
        super(projectDao);
        this.projectDao = projectDao;
    }

    @Override
    public List<Project> getWithNonResolvedMessages(User user) {
        return projectDao.getWithNonResolvedMessages(user);
    }
}