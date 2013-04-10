package nl.bhit.mtor.dao.hibernate;

import java.util.HashSet;
import java.util.List;

import nl.bhit.mtor.dao.ProjectDao;
import nl.bhit.mtor.model.MTorMessage;
import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.User;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository("projectDao")
public class ProjectDaoHibernate extends GenericDaoHibernate<Project, Long> implements ProjectDao {

    public ProjectDaoHibernate() {
        super(Project.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Project> getWithNonResolvedMessages(User user) {
        Query query = getSession().createQuery(
                "select p as project from Project as p left join p.users as u where u = :user");
        query.setLong("user", user.getId());
        List<Project> result = query.list();
        addNonResolved(result);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Project> getWithNonResolvedMessages() {
        String queryString = "from Project";
        log.debug("getting all projects: " + queryString);
        Query query = getSession().createQuery(queryString);
        List<Project> result = query.list();
        addNonResolved(result);
        return result;
    }

    @SuppressWarnings("unchecked")
    private void addNonResolved(List<Project> projects) {
        String queryString = "select m as message from MTorMessage as m where m.project.id  = :projectId AND  m.resolved = :resolved";
        log.trace("retrieving messages with query: " + queryString);
        if (projects == null) {
            return;
        }
        for (Project project : projects) {
            log.trace("retrieving messages for project: " + project);
            Query query = getSession().createQuery(queryString);
            query.setLong("projectId", project.getId());
            query.setBoolean("resolved", false);
            HashSet<MTorMessage> messges = new HashSet<MTorMessage>(query.list());
            project.setMessages(messges);
        }
    }
}
