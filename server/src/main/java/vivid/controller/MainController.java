package vivid.controller;

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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vivid.entity.User;
import vivid.repository.RoleRepository;
import vivid.repository.UserRepository;

import javax.validation.Valid;

/**
 * Created by fantasticfears on 15-5-21.
 */
@Controller
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

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

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup(User user) {
        return "users/new";
    }

    @Transactional
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String register(@ModelAttribute("user") @Valid User user, BindingResult result, @RequestParam String confirm, RedirectAttributes redirectAttributes) {
        user = (User) result.getTarget();
        if (confirm == null || !user.getPassword().equals(confirm)) {
            result.addError(new FieldError("user", "password", "The two passwords are not match."));
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            result.addError(new FieldError("user", "email", "The email has been used."));
        }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            result.addError(new FieldError("user", "username", "The username has been used."));
        }
        if (result.hasErrors()) {
            return "users/new";
        }

        user.setPassword(passwordService.encryptPassword(user.getPassword()));
        user.getRoles().add(roleRepository.findByName("USER"));
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("message", "Sign up successfully.");
        return "redirect:/";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(Model model) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);
        return "users/profile";
    }

}
