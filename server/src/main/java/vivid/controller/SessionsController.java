package vivid.controller;

import de.neuland.jade4j.JadeConfiguration;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import vivid.entity.User;
import vivid.repository.RoleRepository;
import vivid.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

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

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        final Subject subject = SecurityUtils.getSubject();
        model.addAttribute("user", subject.getPrincipal());
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "sessions/new";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "sessions/new";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String authenticate(@RequestParam String username, @RequestParam String password, @RequestParam(defaultValue = "false") Boolean rememberMe) {
        UsernamePasswordToken credentials = new UsernamePasswordToken(username, password);
        credentials.setRememberMe(rememberMe);
        log.info("Authenticating {}", credentials.getUsername());
        final Subject subject = SecurityUtils.getSubject();
        subject.login(credentials);
        return "redirect:/";
    }

}
