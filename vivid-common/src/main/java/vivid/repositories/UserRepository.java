package vivid.repositories;

import org.springframework.stereotype.Repository;
import vivid.repositories.BaseRepository;
import vivid.user.User;

/**
 * Created by wujy on 15-5-14.
 */
@Repository
public interface UserRepository extends BaseRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findByUsernameOrEmail(String username, String email);

}
