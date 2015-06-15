package vivid.support;

import com.domingosuarez.boot.autoconfigure.jade4j.JadeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import vivid.service.UserService;

/**
 * Created by wujy on 15-6-14.
 */
@JadeHelper("currentUser")
public class CurrentUser {

    @Autowired
    private UserService userService;

    public Object now() {
        return userService.getCurrentUser();
    }

}
