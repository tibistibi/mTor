package nl.bhit.mtor.service.impl;

import nl.bhit.mtor.dao.GCMClientDao;
import nl.bhit.mtor.model.GCMClient;
import nl.bhit.mtor.service.GCMClientManager;
import nl.bhit.mtor.service.impl.GenericManagerImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import javax.jws.WebService;

@Service("gCMClientManager")
@WebService(serviceName = "GCMClientService", endpointInterface = "nl.bhit.mtor.service.GCMClientManager")
public class GCMClientManagerImpl extends GenericManagerImpl<GCMClient, Long> implements GCMClientManager {
    GCMClientDao gCMClientDao;

    @Autowired
    public GCMClientManagerImpl(GCMClientDao gCMClientDao) {
        super(gCMClientDao);
        this.gCMClientDao = gCMClientDao;
    }
}