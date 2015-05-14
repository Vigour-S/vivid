package vivid.dao;

import vivid.entity.User;

/**
 * Created by wujy on 15-5-12.
 */
public interface UserDao {

    User findByUsername(String username);

    User findByEmail(String email);

    User findByUsernameOrEmail(String usernameOrEmail);

}
