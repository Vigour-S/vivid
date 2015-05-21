package vivid.users.support;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by fantasticfears on 15-5-21.
 */
@Controller
public class UsersController {

    @RequestMapping(value = "/signup", method = GET)
    public String signUp() {
        return "users/new";
    }

}
