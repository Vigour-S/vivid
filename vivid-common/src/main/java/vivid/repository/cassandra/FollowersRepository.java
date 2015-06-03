package vivid.repository.cassandra;

import org.springframework.data.cassandra.repository.TypedIdCassandraRepository;
import vivid.feed.compositekey.FollowersKey;
import vivid.feed.Followers;

/**
 * Created by xiezhuohan on 15-6-3.
 */
public interface FollowersRepository extends
        TypedIdCassandraRepository<Followers,FollowersKey> {
}
