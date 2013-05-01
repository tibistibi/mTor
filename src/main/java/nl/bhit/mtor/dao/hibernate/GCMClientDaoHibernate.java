package nl.bhit.mtor.dao.hibernate;

import java.util.List;

import nl.bhit.mtor.dao.GCMClientDao;
import nl.bhit.mtor.model.GCMClient;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository("gCMClientDao")
public class GCMClientDaoHibernate extends GenericDaoHibernate<GCMClient, Long> implements GCMClientDao {

    public GCMClientDaoHibernate() {
        super(GCMClient.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public GCMClient getByRegistratoinId(Long gcmRegistrationId) {
        Query query = getSession().createQuery("from GCMClient where gcmRegistrationId = :gcmRegistrationId");
        query.setLong("gcmRegistrationId", gcmRegistrationId);
        List<GCMClient> results = query.list();
        if (results != null && results.size() != 0) {
            LOG.trace("found GCMClient" + results.get(0));
            return results.get(0);
        }
        return null;
    }
}
