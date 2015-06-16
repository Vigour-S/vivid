package vivid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vivid.feed.Comment;
import vivid.feed.Followers;
import vivid.feed.Followings;
import vivid.feed.TimeLine;
import vivid.feed.compositekey.FollowersKey;
import vivid.feed.compositekey.FollowingsKey;
import vivid.repository.UserRepository;
import vivid.repository.cassandra.CommentRepository;
import vivid.repository.cassandra.FollowersRepository;
import vivid.repository.cassandra.FollowingsRepository;
import vivid.service.FeedService;

import java.util.*;

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
    private FeedService feedService;

    @Autowired
    private CommentRepository commentRepository;

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public void postPin(@RequestParam String username, @RequestParam String description) {
        feedService.saveResourcePin(username, description);
    }

    @RequestMapping(value = "/follow", method = RequestMethod.POST)
    public void follow(@RequestParam String username, @RequestParam String usernameToFollow) {
        UUID userId = userRepository.findByUsername(username).getId();
        UUID userIdToFollow = userRepository.findByUsername(usernameToFollow).getId();
        Date since = new Date();
        Followers followers = new Followers(new FollowersKey(userIdToFollow, userId), since);
        followersRepository.save(followers);
        Followings followings = new Followings(new FollowingsKey(userId, userIdToFollow), since);
        followingsRepository.save(followings);
    }

    @RequestMapping(value = "/unFollow", method = RequestMethod.POST)
    public void unFollow(@RequestParam String username, @RequestParam String usernameToFollow) {
        UUID userId = userRepository.findByUsername(username).getId();
        UUID userIdToFollow = userRepository.findByUsername(usernameToFollow).getId();
        followersRepository.delete(new FollowersKey(userIdToFollow, userId));
        followingsRepository.delete(new FollowingsKey(userId, userIdToFollow));
    }

    @RequestMapping(value = "/timeLine", method = RequestMethod.GET)
    public
    @ResponseBody
    Map showTimeLine(@RequestParam String username) {
        List<TimeLine> timeLines = feedService.findTimeLineByUsername(username);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("timeLines", timeLines);
        return map;
    }

    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    public void createComment(@RequestParam UUID pinId, @RequestParam String username, @RequestParam String body) {
        UUID userId = userRepository.findByUsername(username).getId();
        Date date = new Date();
        commentRepository.save(new Comment(pinId, date, userId, body));
    }

    @RequestMapping(value = "/listComment", method = RequestMethod.GET)
    public
    @ResponseBody
    Map showComment(@RequestParam UUID pinId) {
        List<Comment> comments = feedService.findCommentByPinId(pinId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("comments", comments);
        return map;
    }
}
