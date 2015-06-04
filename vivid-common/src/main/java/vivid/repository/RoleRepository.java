package vivid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vivid.entity.Role;

import java.util.UUID;

/**
 * Created by wujy on 15-5-27.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Role findByName(String name);

}
