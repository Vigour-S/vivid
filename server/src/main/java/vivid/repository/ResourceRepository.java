package vivid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vivid.entity.Resource;

import java.util.UUID;

/**
 * Created by wujy on 15-6-5.
 */
public interface ResourceRepository extends JpaRepository<Resource, UUID> {

    Resource findByDigest(String digest);

}
