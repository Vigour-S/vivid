package vivid.controller;


import org.springframework.beans.factory.annotation.Autowired;
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
import vivid.service.FeedService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
    private FeedService feedService;

    @Autowired
    private TimeLineRepository timeLineRepository;

    @Autowired
    private CassandraOperations cassandraOperations;

    static final long PERIOD = 30;

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public String postPin(@RequestParam String username, @RequestParam String description) {
        //save post
        UUID userId = userRepository.findByUsername(username).getId();
        UUID pinId = UUID.randomUUID();
        Date date = new Date();
        pinsRepository.save(new Pins(pinId, userId, date, description));
        //find followers
        List<Followers> followers = feedService.findFollowersByUserId(userId);
        //push
        for (Followers f : followers) {
            UUID tempId = f.getPk().getUserId();
            Duration duration = Duration.between(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC+08:00")),
                    userRepository.findById(tempId).getLastLoginDate()
            );
            if (duration.toDays() > -PERIOD) {
                TimeLine timeLine = new TimeLine(new TimeLineKey(tempId, date), pinId);
                timeLineRepository.save(timeLine);
            }

            timeLineRepository.save(new TimeLine(new TimeLineKey(userId, date), pinId));
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

    @RequestMapping(value = "/timeLine", method = RequestMethod.POST)
    public String showTimeLine(@RequestParam String username) {
        UUID userId = userRepository.findByUsername(username).getId();
        Duration duration = Duration.between(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC+08:00")),
                userRepository.findById(userId).getLastLoginDate()
        );
        //pull
        if (duration.toDays() <= -PERIOD) {
            List<Followings> followings = feedService.findFollowingsByUserId(userId);
            for (Followings f : followings) {
                UUID tempId = f.getPk().getUserId();
                List<Pins> pins = pinsRepository.findByUserId(tempId);
                for (Pins p : pins) {
                    Duration d = Duration.between(userRepository.findById(userId).getLastLoginDate(),
                            ZonedDateTime.of(LocalDateTime.ofInstant(p.getTime().toInstant(),
                                            ZoneId.systemDefault()), ZoneId.of("UTC+08:00")
                            )
                    );
                    if (d.toDays() >= PERIOD)
                        timeLineRepository.save(new TimeLine(userId, p.getTime(), p.getPinId()));
                }
            }
        }
        feedService.findTimelineByUserId(userId);
        return null;
    }

}
