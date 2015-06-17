package vivid.repository.cassandra;

import org.springframework.data.cassandra.repository.TypedIdCassandraRepository;
import vivid.feed.Pins;
import vivid.feed.compositekey.PinsKey;

import java.util.UUID;

/**
 * Created by xiezhuohan on 15-5-28.
 */
public interface PinsRepository extends TypedIdCassandraRepository<Pins, PinsKey> {
}
