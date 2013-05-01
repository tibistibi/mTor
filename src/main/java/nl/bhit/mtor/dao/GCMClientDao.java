package nl.bhit.mtor.dao;

import nl.bhit.mtor.model.GCMClient;

/**
 * An interface that provides a data management interface to the GCMClient table.
 */
public interface GCMClientDao extends GenericDao<GCMClient, Long> {

    /**
     * @param registratoinId
     *            used to retrieve the GCMClient
     * @return the found GCMClient
     */
    GCMClient getByRegistratoinId(Long gcmRegistrationId);

}