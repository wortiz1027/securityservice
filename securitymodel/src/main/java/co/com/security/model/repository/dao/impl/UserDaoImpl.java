package co.com.security.model.repository.dao.impl;

import co.com.security.utils.annotations.InfoLogger;
import co.com.security.model.entities.User;
import co.com.security.model.generic.GenericDaoImpl;
import co.com.security.model.repository.dao.UserDao;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.NoResultException;

@Repository("userDao")
@Transactional
public class UserDaoImpl extends GenericDaoImpl<User, Long> implements UserDao {

    public UserDaoImpl() {
        super(User.class);
    }

    @Override
    public boolean isUserAvailable(String login) {
        Assert.notNull(login);

        StringBuilder sQuery = new StringBuilder();

        sQuery.append("SELECT count(*) \n");
        sQuery.append("FROM ");
        sQuery.append(getClazz().getSimpleName());
        sQuery.append(" u \n");
        sQuery.append("WHERE u.login = :ipLogin");

        Query query = getSessionFactory().getCurrentSession().createQuery(sQuery.toString());
        query.setParameter("ipLogin", login);

        Long count = (Long) query.list().get(0);

        return count < 1;
    }

    @Override
    @InfoLogger(origen = "loadUserByUsername")
    public User loadUserByUsername(String username) {
        Assert.notNull(username);

        User user = null;

        StringBuilder sQuery = new StringBuilder();

        sQuery.append("SELECT u \n");
        sQuery.append("FROM ");
        sQuery.append(getClazz().getSimpleName());
        sQuery.append(" u \n");
        sQuery.append("WHERE u.login = :ipLogin");

        try{
            user = (User) getSessionFactory()
                    .getCurrentSession()
                    .createQuery(sQuery.toString())
                    .setParameter("ipLogin", username)
                    .uniqueResult();
        }catch (NoResultException nre){
            System.out.println("No se encontro el usuario --> " + nre.toString());
        }

        return user;
    }

}