package vivid.service;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

/**
 * Created by wujy on 15-6-14.
 */
@Service
public class UserService {

    public Object getCurrentUser() {
        return SecurityUtils.getSubject().getPrincipal();
    }

}
