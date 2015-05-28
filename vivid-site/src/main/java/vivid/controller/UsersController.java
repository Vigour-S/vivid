package vivid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import vivid.repository.UserRepository;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by fantasticfears on 15-5-21.
 */
@Controller
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/signup", method = GET)
    public String signUp() {
        return "users/new";
    }

}
