package co.com.security.service.dao;

import co.com.security.model.entities.Role;

import java.util.List;

public interface RoleServicesDao {

    public List<Role> getAllRoles();

    public Role getInfoRole();

    public void deleteRole();

    public void updateRole();

}