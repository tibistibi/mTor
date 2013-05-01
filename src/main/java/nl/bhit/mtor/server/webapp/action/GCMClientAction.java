package nl.bhit.mtor.server.webapp.action;

import java.util.List;

import nl.bhit.mtor.dao.SearchException;
import nl.bhit.mtor.model.GCMClient;
import nl.bhit.mtor.service.GCMClientManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.Preparable;

public class GCMClientAction extends BaseAction implements Preparable {
    @Autowired
    private GCMClientManager gCMClientManager;
    private List gCMClients;
    private GCMClient gCMClient;
    private Long id;
    private String query;

    public void setGCMClientManager(GCMClientManager gCMClientManager) {
        this.gCMClientManager = gCMClientManager;
    }

    public List getGCMClients() {
        return gCMClients;
    }

    /**
     * Grab the entity from the database before populating with request parameters
     */
    @Override
    public void prepare() {
        if (getRequest().getMethod().equalsIgnoreCase("post")) {
            // prevent failures on new
            String gCMClientId = getRequest().getParameter("gCMClient.id");
            if (StringUtils.isNotBlank(gCMClientId)) {
                gCMClient = gCMClientManager.get(new Long(gCMClientId));
            } else {
                String gcmRegistrationId = getRequest().getParameter("gCMClient.gcmRegistrationId");
                gCMClient = gCMClientManager.getByRegistratoinId(new Long(gcmRegistrationId));

            }
        }
    }

    public void setQ(String q) {
        this.query = q;
    }

    public String list() {
        try {
            gCMClients = gCMClientManager.search(query, GCMClient.class);
        } catch (SearchException se) {
            addActionError(se.getMessage());
            gCMClients = gCMClientManager.getAll();
        }
        return SUCCESS;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GCMClient getGCMClient() {
        if (gCMClient == null) {
            gCMClient = new GCMClient();
        }
        return gCMClient;
    }

    public void setGCMClient(GCMClient gCMClient) {
        this.gCMClient = gCMClient;
    }

    public String delete() {
        gCMClientManager.remove(gCMClient.getId());
        saveMessage(getText("gCMClient.deleted"));

        return SUCCESS;
    }

    public String edit() {
        if (id != null) {
            gCMClient = gCMClientManager.get(id);
        } else {
            gCMClient = new GCMClient();
        }

        return SUCCESS;
    }

    public String save() throws Exception {
        if (cancel != null) {
            return "cancel";
        }

        if (delete != null) {
            return delete();
        }

        boolean isNew = (gCMClient.getId() == null);

        gCMClientManager.save(gCMClient);

        String key = (isNew) ? "gCMClient.added" : "gCMClient.updated";
        saveMessage(getText(key));

        if (!isNew) {
            return INPUT;
        } else {
            return SUCCESS;
        }
    }
}