package vivid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vivid.entity.User;
import vivid.entity.UserRole;

/**
 * Created by wujy on 15-5-27.
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    UserRole findByUser(User user);

    UserRole findByRole(String role);

}
