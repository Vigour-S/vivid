package vivid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import vivid.entity.User;
import vivid.service.UserService;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by fantasticfears on 15-5-21.
 */
@Controller
public class SessionsController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = GET)
    public String login() {
        System.out.println("!!!!!");
        User user = new User("wujy", "123456", "123@456.com");
        userService.save(user);
        return "sessions/new";
    }

}
