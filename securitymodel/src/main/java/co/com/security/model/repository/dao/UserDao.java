package co.com.security.model.repository.dao;

import co.com.security.model.entities.User;
import co.com.security.model.generic.GenericDao;

public interface UserDao extends GenericDao<User, Long> {

    public boolean isUserAvailable(String login);

    public User loadUserByUsername(String username);

}