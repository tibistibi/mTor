package nl.bhit.dao.hibernate;

import nl.bhit.model.MTorMessage;
import nl.bhit.dao.MessageDao;
import nl.bhit.dao.hibernate.GenericDaoHibernate;
import org.springframework.stereotype.Repository;

@Repository("messageDao")
public class MessageDaoHibernate extends GenericDaoHibernate<MTorMessage, Long> implements MessageDao {

    public MessageDaoHibernate() {
        super(MTorMessage.class);
    }
}
