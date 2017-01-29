package co.com.security.model.repository.dao.impl;

import co.com.security.model.entities.Role;
import co.com.security.model.generic.GenericDaoImpl;
import co.com.security.model.repository.dao.RoleDao;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository("roleDao")
@Transactional
public class RoleDaoImpl extends GenericDaoImpl<Role, Long> implements RoleDao {

    public RoleDaoImpl() {
        super(Role.class);
    }

    @Override
    public List<Role> getRoleListNq() {

        StringBuilder sQuery = new StringBuilder();

        sQuery.append("SELECT r");
        sQuery.append("FROM ");
        sQuery.append(getClazz().getSimpleName());
        sQuery.append("r");

        Query query = getSessionFactory().getCurrentSession().createQuery(sQuery.toString());

        List<Role> roles = new ArrayList<Role>();

        for (Object object : query.list()){
            if (object instanceof Role){
                roles.add((Role)object);
            }
        }

        return roles;
    }

}
