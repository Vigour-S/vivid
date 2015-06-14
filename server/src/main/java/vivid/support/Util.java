package vivid.support;

import com.domingosuarez.boot.autoconfigure.jade4j.JadeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import vivid.service.UserService;

/**
 * Created by wujy on 15-6-14.
 */
@JadeHelper
public class Util {

    @Autowired
    private UserService userService;

    public Object getCurrentUser() {
        return userService.getCurrentUser();
    }

}
