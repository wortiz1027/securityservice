package co.com.security.model.repository.dao;

import co.com.security.model.entities.OauthClientDetails;
import co.com.security.model.generic.GenericDao;

public interface OAuthClientDAO  extends GenericDao<OauthClientDetails, String> {

    public boolean isClientAvailable(String clientId);
    public OauthClientDetails loadClientById(String clientId);

}