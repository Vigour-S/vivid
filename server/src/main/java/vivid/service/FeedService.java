package vivid.service;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.DriverException;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.RowMapper;
import org.springframework.data.cassandra.core.CassandraOperations;
import vivid.feed.Followers;
import vivid.feed.TimeLine;

import java.util.List;
import java.util.UUID;

/**
 * Created by wujy on 15-6-11.
 */
public class FeedService {

    @Autowired
    private CassandraOperations cassandraOperations;

    public List<Followers> findFollowersByUserId(UUID userId) {
        Select select = QueryBuilder.select().from("followers");
        select.where(QueryBuilder.eq("user_id", userId));
        return cassandraOperations.query(select, new RowMapper<Followers>() {
            @Override
            public Followers mapRow(Row row, int rowNum) throws DriverException {
                return new Followers(row.getUUID("user_id"), row.getUUID("follower_id"), row.getDate("since"));
            }
        });
    }

    public List<TimeLine> findTimelineByUserId(UUID userId) {
        Select select = QueryBuilder.select().from("timeline");
        select.where(QueryBuilder.eq("user_id", userId));
        return cassandraOperations.query(select, new RowMapper<TimeLine>() {
            @Override
            public TimeLine mapRow(Row row, int rowNum) throws DriverException {
                return new TimeLine(row.getUUID("user_id"), row.getDate("time"), row.getUUID("pin_id"));
            }
        });
    }

}
