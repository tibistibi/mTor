package nl.bhit.mtor.dao.hibernate;

import nl.bhit.mtor.model.GCMClient;
import nl.bhit.mtor.dao.GCMClientDao;
import nl.bhit.mtor.dao.hibernate.GenericDaoHibernate;
import org.springframework.stereotype.Repository;

@Repository("gCMClientDao")
public class GCMClientDaoHibernate extends GenericDaoHibernate<GCMClient, Long> implements GCMClientDao {

    public GCMClientDaoHibernate() {
        super(GCMClient.class);
    }
}
