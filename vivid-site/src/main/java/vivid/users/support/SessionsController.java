package vivid.users.support;

import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by fantasticfears on 15-5-21.
 */
public class SessionsController {

    @RequestMapping(value = "/login", method = GET)
    public String signUp() {
        return "templates/users/sessions/new";
    }

}
