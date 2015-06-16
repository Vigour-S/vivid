package vivid.controller;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vivid.entity.User;
import vivid.feed.*;
import vivid.feed.compositekey.FollowersKey;
import vivid.feed.compositekey.FollowingsKey;
import vivid.feed.compositekey.TimeLineKey;
import vivid.repository.UserRepository;
import vivid.repository.cassandra.*;
import vivid.service.FeedService;

import java.util.*;

/**
 * Created by xiezhuohan on 15-6-10.
 */
@Controller
public class FeedsController {

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
    private CommentRepository commentRepository;

    @Autowired
    private TimeLineRepository timeLineRepository;

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
        //pull
        List<Pins> pins = feedService.findPinsByUserId(userIdToFollow);
        for (Pins p : pins) {
            timeLineRepository.save(new TimeLine(new TimeLineKey(userIdToFollow, p.getTime()), p.getPk().getPinId()));
        }
    }

    @RequestMapping(value = "/un_follow", method = RequestMethod.POST)
    public void unFollow(@RequestParam String username, @RequestParam String usernameToUnFollow) {
        UUID userId = userRepository.findByUsername(username).getId();
        UUID userIdToFollow = userRepository.findByUsername(usernameToUnFollow).getId();
        followersRepository.delete(new FollowersKey(userIdToFollow, userId));
        followingsRepository.delete(new FollowingsKey(userId, userIdToFollow));
    }

    @RequestMapping(value = "/timeline", method = RequestMethod.GET)
    public
    @ResponseBody
    Map showTimeLine(@DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ") @RequestParam("last_updated_till") Date lastUpdatedTill, @RequestParam int count) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        List<TimeLine> timeLines = feedService.findTimeLineByUsernameAndTimeAndCount(username, lastUpdatedTill, count);
        //List<TimeLine> timeLines = feedService.findTimeLineByUsername(username);
        List<Post> result = new LinkedList<Post>();
        for (TimeLine timeLine : timeLines) {
            User user = userRepository.findById(timeLine.getPk().getUserId());
            Pins pins = pinsRepository.findOne(timeLine.getPinId());
            Post post = new Post();
            post.setUsername(user.getUsername());
            post.setAvatar(user.getAvatar());
            post.setTimestamp(timeLine.getPk().getTime());
            post.setUrl(pins.getBody());
            post.setTitle("");  // TODO:
            post.setDescription(pins.getBody());
            post.setIsVideo(false);  // TODO:
            result.add(post);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("timeline", result);
        return map;
    }

    @RequestMapping(value = "/add_comment", method = RequestMethod.POST)
    public void createComment(@RequestParam UUID pinId, @RequestParam String username, @RequestParam String body) {
        UUID userId = userRepository.findByUsername(username).getId();
        Date date = new Date();
        commentRepository.save(new Comment(pinId, date, userId, body));
    }

    @RequestMapping(value = "/list_comment", method = RequestMethod.POST)
    public
    @ResponseBody
    Map showComment(@RequestParam UUID pinId) {
        List<Comment> comments = feedService.findCommentByPinId(pinId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("comments", comments);
        return map;
    }

}

class Post {
    private String username;
    private String avatar;
    private String url;
    private String title;
    private String description;
    private Date timestamp;
    private Boolean isVideo;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(Boolean isVideo) {
        this.isVideo = isVideo;
    }
}
