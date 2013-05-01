package nl.bhit.mtor.service;

import javax.jws.WebService;

import nl.bhit.mtor.model.GCMClient;

@WebService
public interface GCMClientManager extends GenericManager<GCMClient, Long> {

    /**
     * @param registratoinId
     *            used to retrieve the GCMClient
     * @return the found GCMClient
     */
    GCMClient getByRegistratoinId(Long registratoinId);

}