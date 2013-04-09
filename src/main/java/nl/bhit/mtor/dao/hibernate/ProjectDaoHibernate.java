package nl.bhit.mtor.dao.hibernate;

import java.util.List;

import nl.bhit.mtor.dao.ProjectDao;
import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.User;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository("projectDao")
public class ProjectDaoHibernate extends GenericDaoHibernate<Project, Long> implements ProjectDao {

    public ProjectDaoHibernate() {
        super(Project.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Project> getWithNonResolvedMessages(User user) {
        Query query = getSession()
                .createQuery(
                        "select p as project from MTorMessage as m left join m.project as p left join p.users as u where m.resolved = :resolved AND u = :user");
        query.setLong("user", user.getId());
        query.setBoolean("resolved", false);
        return query.list();
    }
}
