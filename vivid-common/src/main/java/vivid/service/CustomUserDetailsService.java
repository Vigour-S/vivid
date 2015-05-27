package vivid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import vivid.entity.SecurityUser;
import vivid.entity.User;

/**
 * Created by wujy on 15-5-12.
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userService.findByUsernameOrEmail(username, username);
        if (user == null) {
            throw new UsernameNotFoundException("UserName " + username + " not found");
        }
        return new SecurityUser(user);
    }

}