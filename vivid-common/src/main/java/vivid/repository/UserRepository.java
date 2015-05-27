package vivid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vivid.entity.User;

/**
 * Created by wujy on 15-5-14.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findById(Long id);

    User findByUsername(String username);

    User findByEmail(String email);

    User findByUsernameOrEmail(String username, String email);

}
