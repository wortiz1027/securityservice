package co.com.security.service.dao;

import co.com.security.model.entities.OauthClientDetails;

public interface ClientServicesDao {

    public void createClient(OauthClientDetails client);

    public boolean isUserAvailable(String clientId);

    public OauthClientDetails getClientById(String clientId);

    public OauthClientDetails actualizar(OauthClientDetails client);

    public void eliminar(OauthClientDetails client);

}
