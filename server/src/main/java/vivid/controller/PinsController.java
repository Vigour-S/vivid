package vivid.controller;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.DriverException;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.RowMapper;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import vivid.feed.Followers;
import vivid.feed.Followings;
import vivid.feed.Pins;
import vivid.feed.TimeLine;
import vivid.feed.compositekey.FollowersKey;
import vivid.feed.compositekey.FollowingsKey;
import vivid.feed.compositekey.TimeLineKey;
import vivid.repository.UserRepository;
import vivid.repository.cassandra.FollowersRepository;
import vivid.repository.cassandra.FollowingsRepository;
import vivid.repository.cassandra.PinsRepository;
import vivid.repository.cassandra.TimeLineRepository;

import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by xiezhuohan on 15-6-10.
 */

@Controller
public class PinsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowersRepository followersRepository;

    @Autowired
    private FollowingsRepository followingsRepository;

    @Autowired
    private PinsRepository pinsRepository;

    @Autowired
    private TimeLineRepository timeLineRepository;

    @Autowired
    private CassandraOperations cassandraOperations;

    static final long PEROID = 30;

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public String postPin(@RequestParam String username, @RequestParam String description) {
        UUID userId = userRepository.findByUsername(username).getId();
        UUID pinId = UUID.randomUUID();
        Date date = new Date();
        pinsRepository.save(new Pins(pinId, userId, date,description));
        Select s = QueryBuilder.select().from("followers");
        s.where(QueryBuilder.eq("user_id", userId));
        List<Followers> results = cassandraOperations.query(s, new RowMapper<Followers>() {
            @Override
            public Followers mapRow(Row row, int rowNum) throws DriverException {
                Followers f = new Followers(row.getUUID("user_id"), row.getUUID("follower_id"), row.getDate("since"));
                return f;
            }
        });
        //push
        for (Followers f : results) {
            UUID tempId = f.getPk().getFollowerId();
            Duration duration = Duration.between(ZonedDateTime.of(LocalDateTime.now(),ZoneId.of("UTC+08:00")),
                    userRepository.findById(tempId).getLastLoginDate()
            );
            if(duration.toDays()>-PEROID) {
                TimeLine timeLine = new TimeLine(new TimeLineKey(tempId, date), pinId);
                timeLineRepository.save(timeLine);
            }
        }
        timeLineRepository.save(new TimeLine(new TimeLineKey(userId, date), pinId));
        return null;
    }

    @RequestMapping(value = "/follow", method = RequestMethod.POST)
    public String follow(@RequestParam String username, @RequestParam String usernameToFollow) {
        UUID userId = userRepository.findByUsername(username).getId();
        UUID userIdToFollow = userRepository.findByUsername(usernameToFollow).getId();
        Date since = new Date();
        Followers followers = new Followers(new FollowersKey(userIdToFollow, userId), since);
        followersRepository.save(followers);
        Followings followings = new Followings(new FollowingsKey(userId, userIdToFollow), since);
        followingsRepository.save(followings);
        return null;
    }

    @RequestMapping(value = "/unFollow", method = RequestMethod.POST)
    public String unFollow(@RequestParam String username, @RequestParam String usernameToFollow) {
        UUID userId = userRepository.findByUsername(username).getId();
        UUID userIdToFollow = userRepository.findByUsername(usernameToFollow).getId();
        followersRepository.delete(new FollowersKey(userIdToFollow, userId));
        followingsRepository.delete(new FollowingsKey(userId, userIdToFollow));
        return null;
    }

    @RequestMapping(value = "/timeLine", method = RequestMethod.POST)
    public String showTimeLine(@RequestParam String username) {
        UUID userId = userRepository.findByUsername(username).getId();
        Duration duration = Duration.between(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC+08:00")),
                userRepository.findById(userId).getLastLoginDate()
        );
        //pull
        if(duration.toDays()<=-PEROID){
            Select s = QueryBuilder.select().from("followings");
            s.where(QueryBuilder.eq("user_id", userId));
            List<Followings> results = cassandraOperations.query(s, new RowMapper<Followings>() {
                @Override
                public Followings mapRow(Row row, int rowNum) throws DriverException {
                    Followings f = new Followings(row.getUUID("user_id"), row.getUUID("following_id"), row.getDate("since"));
                    return f;
                }
            });
            for (Followings f : results) {
                UUID tempId = f.getPk().getFollowingId();
                //TODO:
            }
        }
        Select s = QueryBuilder.select().from("timeline");
        s.where(QueryBuilder.eq("user_id", userId));
        List<TimeLine> results = cassandraOperations.query(s, new RowMapper<TimeLine>() {
            @Override
            public TimeLine mapRow(Row row, int rowNum) throws DriverException {
                TimeLine timeLine = new TimeLine(row.getUUID("user_id"), row.getDate("time"), row.getUUID("pin_id"));
                return timeLine;
            }
        });

        return null;
    }

}
