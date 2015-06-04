package vivid.repository.cassandra;

import org.springframework.data.cassandra.repository.TypedIdCassandraRepository;
import vivid.feed.Followings;
import vivid.feed.compositekey.FollowingsKey;

/**
 * Created by xiezhuohan on 15-6-3.
 */
public interface FollowingsRepository extends TypedIdCassandraRepository<Followings, FollowingsKey> {

}
