package vivid.repository.cassandra;

import org.springframework.data.cassandra.repository.TypedIdCassandraRepository;
import vivid.feed.Followers;
import vivid.feed.compositekey.FollowersKey;

import java.util.List;
import java.util.UUID;

/**
 * Created by xiezhuohan on 15-6-3.
 */
public interface FollowersRepository extends TypedIdCassandraRepository<Followers, FollowersKey> {
    List<Followers> findByUserId(UUID uuid);

}
