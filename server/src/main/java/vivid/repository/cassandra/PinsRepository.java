package vivid.repository.cassandra;

import org.springframework.data.cassandra.repository.TypedIdCassandraRepository;
import vivid.feed.Pins;

import java.util.List;
import java.util.UUID;

/**
 * Created by xiezhuohan on 15-5-28.
 */
public interface PinsRepository extends TypedIdCassandraRepository<Pins, UUID> {
    List<Pins> findByUserId(UUID userId);
}
