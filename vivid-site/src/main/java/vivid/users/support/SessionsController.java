package vivid.users.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import vivid.entity.User;
import vivid.repository.UserRepository;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by fantasticfears on 15-5-21.
 */
@Controller
public class SessionsController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/login", method = GET)
    public String login() {
        System.out.print("12214");
        User user = new User("wujy", "123456", "123@456.com", true);
        userRepository.save(user);
        return "sessions/new";
    }

}
