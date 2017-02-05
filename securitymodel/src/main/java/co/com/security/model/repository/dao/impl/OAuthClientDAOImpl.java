package co.com.security.model.repository.dao.impl;

import co.com.security.model.entities.OauthClientDetails;
import co.com.security.model.generic.GenericDaoImpl;
import co.com.security.model.repository.dao.OAuthClientDAO;
import co.com.security.utils.annotations.InfoLogger;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.NoResultException;

@Transactional
@Repository("oauthClientDAO")
public class OAuthClientDAOImpl  extends GenericDaoImpl<OauthClientDetails, String> implements OAuthClientDAO {

    public OAuthClientDAOImpl() {
        super(OauthClientDetails.class);
    }

    @Override
    @InfoLogger(origen = "isClientAvailable")
    public boolean isClientAvailable(String clientId) {
        Assert.notNull(clientId);

        StringBuilder sQuery = new StringBuilder();

        sQuery.append("SELECT count(*) \n");
        sQuery.append("FROM ");
        sQuery.append(getClazz().getSimpleName());
        sQuery.append(" c \n");
        sQuery.append("WHERE c.clientId = :ipClientId");

        Query query = getSessionFactory().getCurrentSession().createQuery(sQuery.toString());
        query.setParameter("ipClientId", clientId);

        Long count = (Long) query.list().get(0);

        return count >= 1 ? true : false;
    }

    @Override
    public OauthClientDetails loadClientById(String clientId) {
        Assert.notNull(clientId);

        OauthClientDetails client = null;

        StringBuilder sQuery = new StringBuilder();

        sQuery.append("SELECT c \n");
        sQuery.append("FROM ");
        sQuery.append(getClazz().getSimpleName());
        sQuery.append(" c \n");
        sQuery.append("WHERE c.clientId = :ipClientId");


        try{
            Query query = getSessionFactory().getCurrentSession().createQuery(sQuery.toString());
            query.setParameter("ipClientId", clientId);

            if (query.list().get(0) instanceof OauthClientDetails){
                client = (OauthClientDetails) query.list().get(0);
            }

        }catch (NoResultException nre){
            System.out.println("No se encontro el cliente --> " + nre.toString());
        }

        return client;
    }

}
