package vivid.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vivid.entity.Permission;
import vivid.entity.Resource;
import vivid.entity.Role;
import vivid.entity.User;
import vivid.repository.PermissionRepository;
import vivid.repository.ResourceRepository;
import vivid.repository.RoleRepository;
import vivid.repository.UserRepository;
import vivid.service.ResourceService;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by wujy on 15-6-3.
 */
@Controller
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

    @RequestMapping(value = "users", method = RequestMethod.GET)
    public @ResponseBody List<User> getAll() {
        SecurityUtils.getSubject().checkRole("ADMIN");
        return userRepository.findAll();
    }

    @RequestMapping(value = "users/do_something", method = RequestMethod.GET)
    public @ResponseBody List<User> dontHavePermission() {
        SecurityUtils.getSubject().checkPermission("DO_SOMETHING");
        return userRepository.findAll();
    }

    @Transactional
    @RequestMapping(value = "users", method = RequestMethod.PUT)
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
        p2.setName("UPLOAD");
        permissionRepository.save(p2);

        // define roles
        final Role roleAdmin = new Role();
        roleAdmin.setName("ADMIN");
        roleAdmin.getPermissions().add(p1);
        roleAdmin.getPermissions().add(p2);
        roleRepository.save(roleAdmin);
        final Role roleUser = new Role();
        roleUser.setName("USER");
        roleUser.getPermissions().add(p2);
        roleRepository.save(roleUser);

        // define user
        final User userAdmin = new User();
        userAdmin.setEmail("wujysh@gmail.com");
        userAdmin.setUsername("Jiaye Wu");
        userAdmin.setPassword(passwordService.encryptPassword("123456"));
        userAdmin.getRoles().add(roleAdmin);
        userRepository.save(userAdmin);
        final User userUser1 = new User();
        userUser1.setEmail("fantasticfears@gmail.com");
        userUser1.setUsername("Erick Guan");
        userUser1.setPassword(passwordService.encryptPassword("123456"));
        userUser1.getRoles().add(roleUser);
        userRepository.save(userUser1);
        final User userUser2 = new User();
        userUser2.setEmail("jasonxzh818@gmail.com");
        userUser2.setUsername("Jason Xie");
        userUser2.setPassword(passwordService.encryptPassword("123456"));
        userUser2.getRoles().add(roleUser);
        userRepository.save(userUser2);

        log.info("Scenario initiated.");
    }

    @RequestMapping(value = "users/{username}", method = RequestMethod.GET)
    public String picture(@PathVariable String username, Model model) {
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);
        return "users/profile";
    }


}
