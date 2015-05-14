package vivid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vivid.dao.UserDao;
import vivid.entity.UserRole;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wujy on 15-5-12.
 */
@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    /**
     * Locates the user based on the username. In the actual implementation, the search may possibly be case
     * sensitive, or case insensitive depending on how the implementation instance is configured. In this case, the
     * <code>UserDetails</code> object that comes back may have a username that is of a different case than what was
     * actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no GrantedAuthority
     */
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        vivid.entity.User user = userDao.findByUsernameOrEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        List<GrantedAuthority> authorities = buildUserAuthority(user.getUserRoles());
        return buildUserForAuthentication(user, authorities);
    }

    /**
     * Converts vivid.entity.User to org.springframework.security.core.userdetails.User
     */
    private UserDetails buildUserForAuthentication(vivid.entity.User user, List<GrantedAuthority> authorities) {
        return new User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, authorities);
    }

    private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles) {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        for (UserRole userRole : userRoles) {
            authorities.add(new SimpleGrantedAuthority(userRole.getRole()));
        }
        return new ArrayList<GrantedAuthority>(authorities);
    }

}
