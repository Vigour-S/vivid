package vivid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vivid.entity.Permission;

/**
 * Created by wujy on 15-6-3.
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Permission findByName(String name);

}
