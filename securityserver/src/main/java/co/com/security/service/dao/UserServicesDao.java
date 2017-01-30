package co.com.security.service.dao;

import co.com.security.model.entities.Role;
import co.com.security.model.entities.User;

import java.util.List;

public interface UserServicesDao {

    public void createUser(User u, List<Role> role);

    public boolean isUserAvailable(String username);

    public User getUserByLogin(String login);

    public User actualizar(User u);

    public void eliminar(User u);

}