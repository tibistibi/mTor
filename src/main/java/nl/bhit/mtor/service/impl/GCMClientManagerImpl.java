package nl.bhit.mtor.service.impl;

import javax.jws.WebService;

import nl.bhit.mtor.dao.GCMClientDao;
import nl.bhit.mtor.model.GCMClient;
import nl.bhit.mtor.service.GCMClientManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("gCMClientManager")
@WebService(
        serviceName = "GCMClientService",
        endpointInterface = "nl.bhit.mtor.service.GCMClientManager")
public class GCMClientManagerImpl extends GenericManagerImpl<GCMClient, Long> implements GCMClientManager {
    GCMClientDao gCMClientDao;

    @Autowired
    public GCMClientManagerImpl(GCMClientDao gCMClientDao) {
        super(gCMClientDao);
        this.gCMClientDao = gCMClientDao;
    }

    @Override
    public GCMClient getByRegistratoinId(Long registratoinId) {
        return gCMClientDao.getByRegistratoinId(registratoinId);
    }
}