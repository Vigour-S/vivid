package vivid.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import vivid.entity.User;
import vivid.repository.RoleRepository;
import vivid.repository.UserRepository;

/**
 * Created by fantasticfears on 15-5-21.
 */
@Controller
public class SessionsController {

    private static final Logger log = LoggerFactory.getLogger(SessionsController.class);

    @Autowired
    private DefaultPasswordService passwordService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "sessions/new";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup() {
        return "users/new";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        final Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "sessions/new";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void authenticate(@RequestParam String username, @RequestParam String password) {
        UsernamePasswordToken credentials = new UsernamePasswordToken(username, password);
        log.info("Authenticating {}", credentials.getUsername());
        final Subject subject = SecurityUtils.getSubject();
        subject.login(credentials);
        // set attribute that will allow session querying
        subject.getSession().setAttribute("email", credentials.getUsername());
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public void register(@RequestParam String email, @RequestParam String username, @RequestParam String password) {
        User user = new User(username, passwordService.encryptPassword(password), email);
        user.getRoles().add(roleRepository.findByName("USER"));
        userRepository.save(user);
    }

}
