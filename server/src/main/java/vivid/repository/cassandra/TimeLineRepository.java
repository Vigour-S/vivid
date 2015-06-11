package vivid.repository.cassandra;

import org.springframework.data.cassandra.repository.TypedIdCassandraRepository;
import vivid.feed.TimeLine;
import vivid.feed.compositekey.TimeLineKey;

import java.util.UUID;

/**
 * Created by xiezhuohan on 15-6-3.
 */
public interface TimeLineRepository extends TypedIdCassandraRepository<TimeLine, TimeLineKey> {
    //TimeLine findByUserId(UUID uuid);
}
