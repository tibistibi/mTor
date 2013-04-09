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

    @SuppressWarnings("unchecked")
    public List<Project> getWithNonResolvedMessages() {
        Query query = getSession().createQuery("select p as project from Project");
        List<Project> result = query.list();
        addNonResolved(result);
        return result;
    }

    @SuppressWarnings("unchecked")
    private void addNonResolved(List<Project> result) {
        for (Project project : result) {
            Query query = getSession()
                    .createQuery(
                            "select m as message from MTorMessage as m where m.project.id  = :projectId AND  m.resolved = :resolved");
            query.setLong("projectId", project.getId());
            query.setBoolean("resolved", false);
            HashSet<MTorMessage> messges = new HashSet<MTorMessage>(query.list());
            project.setMessages(messges);
        }
    }
}
