package vivid.feed;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;
import vivid.feed.compositekey.CommentKey;

import java.util.Date;
import java.util.UUID;

/**
 * Created by xiezhuohan on 15-6-12.
 */
@Table(value = "comment")
public class Comment {

    @PrimaryKey
    private CommentKey pk;

    @Column(value = "user_id")
    private UUID userId;

    @Column(value = "body")
    private String body;

    public Comment(CommentKey pk, UUID userId, String body) {
        this.pk = pk;
        this.userId = userId;
        this.body = body;
    }

    public Comment(UUID pinId, Date time, UUID userId, String body) {
        this.pk = new CommentKey(pinId, time);
        this.userId = userId;
        this.body = body;
    }

    public CommentKey getPk() {
        return pk;
    }

    public void setPk(CommentKey pk) {
        this.pk = pk;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
