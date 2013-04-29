package nl.bhit.mtor.service;

import nl.bhit.mtor.service.GenericManager;
import nl.bhit.mtor.model.GCMClient;

import java.util.List;
import javax.jws.WebService;

@WebService
public interface GCMClientManager extends GenericManager<GCMClient, Long> {
    
}