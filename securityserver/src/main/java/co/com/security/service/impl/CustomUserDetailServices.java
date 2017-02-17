package co.com.security.service.impl;

import co.com.security.utils.annotations.InfoLogger;
import co.com.security.model.entities.Role;
import co.com.security.model.repository.dao.UserDao;
import co.com.security.service.dao.UserServicesDao;
import co.com.security.service.utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Service("customUserDetailsService")
@Transactional
public class CustomUserDetailServices implements UserDetailsService, UserServicesDao {

    @Resource
    private Environment env;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @InfoLogger(origen = "loadUserByUsername")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        co.com.security.model.entities.User user = userDao.loadUserByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException(String.format(env.getRequiredProperty(Constantes.MSG_ERROR_USUARIO_NO_REGISTRADO_KEY), username));

        return new User(user.getLogin(),
                user.getPassword(),
                Boolean.valueOf(user.getEnable()),
                Boolean.valueOf(user.getAccountNonExpired()),
                Boolean.valueOf(user.getCredentialNonExpired()),
                Boolean.valueOf(user.getAccountNonLocked()),
                getAuthorities(user.getRoleList())
        );
    }

    public Collection<? extends GrantedAuthority> getAuthorities(List<Role> role) {
        List<GrantedAuthority> authoritiesList = new ArrayList<GrantedAuthority>(2);

        Iterator<Role> iterRole = role.iterator();

        while (iterRole.hasNext()) {
            Role rol = iterRole.next();
            authoritiesList.add(new SimpleGrantedAuthority(rol.getRole()));
            System.out.println("ROLES --> " + rol.getRole());
        }

        return authoritiesList;
    }

    @Override
    public boolean isUserAvailable(String username) {

        boolean available = userDao.isUserAvailable(username);

        return available;
    }

    @Override
    public co.com.security.model.entities.User getUserByLogin(String login) {
        return userDao.loadUserByUsername(login);
    }

    @Override
    public co.com.security.model.entities.User actualizar(co.com.security.model.entities.User u) {
        userDao.update(u);
        return u;
    }

    @Override
    public void eliminar(co.com.security.model.entities.User u) {
        userDao.delete(u.getIdUser().longValue());
    }

    @Override
    public void createUser(co.com.security.model.entities.User u, List<Role> role) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        u.setRoleList(role);
        userDao.save(u);
    }

}