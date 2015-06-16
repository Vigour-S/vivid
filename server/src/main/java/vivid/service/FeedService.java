package vivid.service;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.DriverException;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.RowMapper;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;
import vivid.feed.*;
import vivid.feed.compositekey.TimeLineKey;
import vivid.repository.UserRepository;
import vivid.repository.cassandra.PinsRepository;
import vivid.repository.cassandra.TimeLineRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by wujy on 15-6-11.
 */
@Service
public class FeedService {

    @Autowired
    private CassandraOperations cassandraOperations;

    @Autowired
    private PinsRepository pinsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeLineRepository timeLineRepository;

    static final long PERIOD = 30;

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

    public List<Followings> findFollowingsByUserId(UUID userId) {
        Select select = QueryBuilder.select().from("followings");
        select.where(QueryBuilder.eq("user_id", userId));
        return cassandraOperations.query(select, new RowMapper<Followings>() {
            @Override
            public Followings mapRow(Row row, int rowNum) throws DriverException {
                return new Followings(row.getUUID("user_id"), row.getUUID("following_id"), row.getDate("since"));
            }
        });
    }

    public List<Pins> findPinsByUserId(UUID userId) {
        Select select = QueryBuilder.select().from("pins");
        select.where(QueryBuilder.eq("user_id", userId));
        return cassandraOperations.query(select, new RowMapper<Pins>() {
            @Override
            public Pins mapRow(Row row, int rowNum) throws DriverException {
                return new Pins(row.getUUID("user_id"), row.getUUID("pin_id"), row.getDate("time"), row.getString("body"));
            }
        });
    }

    public List<TimeLine> findTimeLineByUserIdAndTimeAndCount(UUID userId, Date lastUpdatedTill, int count) {
        Select select = QueryBuilder.select().from("timeline");
        select.where(QueryBuilder.eq("user_id", userId));
        select.where(QueryBuilder.lt("time", lastUpdatedTill));
        select.limit(count);
        return cassandraOperations.query(select, new RowMapper<TimeLine>() {
            @Override
            public TimeLine mapRow(Row row, int rowNum) throws DriverException {
                return new TimeLine(row.getUUID("user_id"), row.getDate("time"), row.getUUID("pin_id"));
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

    public List<Comment> findCommentByPinId(UUID pinId) {
        Select select = QueryBuilder.select().from("comment");
        select.where(QueryBuilder.eq("pin_id", pinId));
        return cassandraOperations.query(select, new RowMapper<Comment>() {
            @Override
            public Comment mapRow(Row row, int rowNum) throws DriverException {
                return new Comment(row.getUUID("pin_id"), row.getDate("time"), row.getUUID("user_id"), row.getString("body"));
            }
        });
    }

    public void saveResourcePin(String username, String body) {
        //save post
        UUID userId = userRepository.findByUsername(username).getId();
        UUID pinId = UUID.randomUUID();
        Date date = new Date();
        pinsRepository.save(new Pins(userId, pinId, date, body));

        //find followers
        List<Followers> followers = findFollowersByUserId(userId);

        //push
        for (Followers f : followers) {
            UUID tempId = f.getPk().getFollowerId();
            Duration duration = Duration.between(
                    userRepository.findById(tempId).getLastLoginDate(),
                    ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault())
            );
            if (duration.toDays() < PERIOD) {
                TimeLine timeLine = new TimeLine(new TimeLineKey(tempId, date), pinId);
                timeLineRepository.save(timeLine);
            }
        }
        timeLineRepository.save(new TimeLine(new TimeLineKey(userId, date), pinId));
    }

    public List<TimeLine> findTimeLineByUsernameAndTimeAndCount(String username, Date lastUpdatedTill, int count) {
        UUID userId = userRepository.findByUsername(username).getId();
        Duration duration = Duration.between(
                userRepository.findById(userId).getLastLoginDate(),
                ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault())
        );

        //pull
        if (duration.toDays() >= PERIOD) {
            //find followers
            List<Followings> followings = findFollowingsByUserId(userId);
            for (Followings f : followings) {
                UUID tempId = f.getPk().getFollowingId();
                List<Pins> pins = findPinsByUserId(tempId);
                for (Pins p : pins) {
                    Duration d = Duration.between(userRepository.findById(userId).getLastLoginDate(),
                            ZonedDateTime.of(LocalDateTime.ofInstant(p.getTime().toInstant(),
                                            ZoneId.systemDefault()), ZoneId.systemDefault()
                            )
                    );
                    if (d.toDays() >= PERIOD)
                        timeLineRepository.save(new TimeLine(userId, p.getTime(), p.getPinId()));
                }
            }
        }
        return findTimeLineByUserIdAndTimeAndCount(userId, lastUpdatedTill, count);
    }

}
