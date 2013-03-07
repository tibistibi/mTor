package nl.bhit.mtor.dao.hibernate;

import nl.bhit.mtor.dao.ProjectDao;
import nl.bhit.mtor.model.Project;

import org.springframework.stereotype.Repository;

@Repository("projectDao")
public class ProjectDaoHibernate extends GenericDaoHibernate<Project, Long> implements ProjectDao {

    public ProjectDaoHibernate() {
        super(Project.class);
    }
}
