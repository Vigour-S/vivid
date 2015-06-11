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
import vivid.entity.User;
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

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public String postPin(@RequestParam String username) {
//        UUID userId = userRepository.findByUsername(username).getId();
//        pinsRepository.save(new Pins(pinId, userId, description));
//        Date date = new Date();
//        //TODO: 拉取
//        List<Followers> followerses = followersRepository.findByUserId(userId);
//        for( Followers followers : followerses){
//            UUID tempId = followers.getPk().getUserId();
//            TimeLine timeLine = new TimeLine(new TimeLineKey(tempId, date), pinId);
//            timeLineRepository.save(timeLine);
//        }
        UUID userId = userRepository.findByUsername(username).getId();
        //String cql = "select * from followers where user_id = bfd2c7d4-8cf4-4933-88e7-f2792d075540";
        //List<Followers> followerses = followersRepository.findByUserId(userId);
       // Select s = QueryBuilder.select().from("followers");
        //s.where(QueryBuilder.eq("user_id", userId));
        //List<Followers> followerses = cassandraOperations.queryForList(cql, Followers.class);
        String cqlAll = "select * from followers";
        List<Followers> results = cassandraOperations.query(cqlAll, new RowMapper<Followers>() {
            @Override
            public Followers mapRow(Row row, int rowNum) throws DriverException {
                Followers f = new Followers(row.getUUID("user_id"), row.getUUID("follower_id"),row.getDate("since"));
                return f;
            }
        });
        for (Followers f : results) {
            System.out.println(f.getPk().getUserId());
        }
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

    public String showTimeLine(@RequestParam String username){
//        List<Pins> pinses;
//        timeLineRepository.findByUserId(userRepository.findByUsername(username).getId());
        Iterable<Followers> followerses = followersRepository.findAll();
        for (Followers followers : followerses) {
            System.out.println(followers.getPk().getUserId());
        }
        return null;
    }

}
