package vivid.bean;

import vivid.entity.User;
import vivid.feed.Comment;

/**
 * Created by wujy on 15-6-17.
 */
public class CommentBean {

    private User user;

    private Comment comment;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

}
