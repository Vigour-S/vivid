package vivid.repository.cassandra;

import org.springframework.data.cassandra.repository.TypedIdCassandraRepository;
import vivid.feed.compositekey.TimeLineKey;
import vivid.feed.TimeLine;

/**
 * Created by xiezhuohan on 15-6-3.
 */
public interface TimeLineRepository extends
        TypedIdCassandraRepository<TimeLine,TimeLineKey>{
}
