package vivid.controller;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vivid.bean.CommentBean;
import vivid.bean.PostBean;
import vivid.entity.User;
import vivid.feed.*;
import vivid.feed.compositekey.FollowersKey;
import vivid.feed.compositekey.FollowingsKey;
import vivid.feed.compositekey.TimeLineKey;
import vivid.repository.UserRepository;
import vivid.repository.cassandra.*;
import vivid.service.FeedService;

import javax.servlet.http.HttpServletRequest;
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
    public String follow(@RequestParam String usernameToFollow, HttpServletRequest request) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
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
        return "redirect:" + request.getHeader("Referer");
    }

    @RequestMapping(value = "/un_follow", method = RequestMethod.POST)
    public String unFollow(@RequestParam String usernameToUnFollow, HttpServletRequest request) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        UUID userId = userRepository.findByUsername(username).getId();
        UUID userIdToFollow = userRepository.findByUsername(usernameToUnFollow).getId();
        followersRepository.delete(new FollowersKey(userIdToFollow, userId));
        followingsRepository.delete(new FollowingsKey(userId, userIdToFollow));
        return "redirect:" + request.getHeader("Referer");
    }

    @RequestMapping(value = "/timeline", method = RequestMethod.GET)
    public
    @ResponseBody
    Map showTimeLine(@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") @RequestParam("last_updated_till") Date lastUpdatedTill, @RequestParam int count) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        List<TimeLine> timeLines = feedService.findTimeLineByUsernameAndTimeAndCount(username, lastUpdatedTill, count);
        List<PostBean> result = new LinkedList<PostBean>();
        for (TimeLine timeLine : timeLines) {
            Pins pins = feedService.findPinsByPinId(timeLine.getPinId()).get(0);
            User user = userRepository.findById(pins.getPk().getUserId());
            PostBean postBean = new PostBean();
            postBean.setUsername(user.getUsername());
            postBean.setAvatar(user.getAvatar());
            postBean.setTimestamp(timeLine.getPk().getTime());
            postBean.setUrl("/detail/" + pins.getPk().getPinId());
            postBean.setTitle(null);  // TODO:
            postBean.setDescription(pins.getBody());
            postBean.setIsVideo(false);  // TODO:
            result.add(postBean);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("timeline", result);
        return map;
    }

    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public String createComment(@RequestParam UUID pinId, @RequestParam String body, HttpServletRequest request) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        UUID userId = userRepository.findByUsername(username).getId();
        Date date = new Date();
        commentRepository.save(new Comment(pinId, date, userId, body));
        return "redirect:" + request.getHeader("Referer");
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

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable UUID id, Model model) {
        Pins pins = feedService.findPinsByPinId(id).get(0);
        model.addAttribute("pins", pins);

        List<Comment> comments = feedService.findCommentByPinId(id);
        List<CommentBean> commentBeans = new LinkedList<CommentBean>();
        for (Comment comment : comments) {
            CommentBean commentBean = new CommentBean();
            commentBean.setUser(userRepository.findById(comment.getUserId()));
            commentBean.setComment(comment);
            commentBeans.add(commentBean);
        }
        model.addAttribute("comments", commentBeans);
        return "feeds/detail";
    }

}

