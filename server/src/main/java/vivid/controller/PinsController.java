package vivid.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public String postPin(@RequestParam String username,
                          @RequestParam UUID pinId,
                          @RequestParam String description) {
        UUID userId = userRepository.findByUsername(username).getId();
        pinsRepository.save(new Pins(pinId, userId, description));
        Date date = new Date();
        //TODO: 拉取
        List<Followers> followerses = followersRepository.findByUserId(userId);
        for( Followers followers : followerses){
            UUID tempId = followers.getPk().getUserId();
            TimeLine timeLine = new TimeLine(new TimeLineKey(tempId, date), pinId);
            timeLineRepository.save(timeLine);
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
        List<Pins> pinses;
        timeLineRepository.findByUserId(userRepository.findByUsername(username).getId());
        return null;
    }

}
