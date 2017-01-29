package co.com.security.model.repository.dao;

import co.com.security.model.entities.Role;
import co.com.security.model.generic.GenericDao;

import java.util.List;

public interface RoleDao extends GenericDao<Role, Long> {

    public List<Role> getRoleListNq();

}
