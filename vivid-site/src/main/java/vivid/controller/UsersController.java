package vivid.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vivid.entity.Permission;
import vivid.entity.User;
import vivid.entity.Role;
import vivid.repository.PermissionRepository;
import vivid.repository.RoleRepository;
import vivid.repository.UserRepository;

import java.util.List;

/**
 * Created by wujy on 15-6-3.
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    private static final Logger log = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private DefaultPasswordService passwordService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public void authenticate(@RequestBody final UsernamePasswordToken credentials) {
        log.info("Authenticating {}", credentials.getUsername());
        final Subject subject = SecurityUtils.getSubject();
        subject.login(credentials);
        // set attribute that will allow session querying
        subject.getSession().setAttribute("email", credentials.getUsername());
    }

    @RequestMapping(method = RequestMethod.GET)
    @RequiresAuthentication
    @RequiresRoles("ADMIN")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @RequestMapping(value="do_something", method = RequestMethod.GET)
    @RequiresAuthentication
    @RequiresRoles("DO_SOMETHING")
    public List<User> dontHavePermission() {
        return userRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void initScenario() {
        log.info("Initializing scenario..");

        // clean-up users, roles and permissions
        userRepository.deleteAll();
        roleRepository.deleteAll();
        permissionRepository.deleteAll();

        // define permissions
        final Permission p1 = new Permission();
        p1.setName("VIEW_ALL_USERS");
        permissionRepository.save(p1);
        final Permission p2 = new Permission();
        p2.setName("DO_SOMETHING");
        permissionRepository.save(p2);

        // define roles
        final Role roleAdmin = new Role();
        roleAdmin.setName("ADMIN");
        roleAdmin.getPermissions().add(p1);
        roleRepository.save(roleAdmin);

        // define user
        final User user = new User();
        user.setEmail("wujysh@gmail.com");
        user.setUsername("Jiaye Wu");
        user.setPassword(passwordService.encryptPassword("123456"));
        user.getRoles().add(roleAdmin);
        userRepository.save(user);
        log.info("Scenario initiated.");
    }

}
