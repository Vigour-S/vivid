package vivid.feed;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;
import vivid.feed.compositekey.FollowingsKey;

import java.util.Date;
import java.util.UUID;

/**
 * Created by xiezhuohan on 15-5-27.
 */
@Table(value = "followings")
public class Followings {

    @PrimaryKey
    private FollowingsKey pk;

    @Column(value = "since")
    private Date since;

    public Followings(FollowingsKey pk, Date since) {
        this.pk = pk;
        this.since = since;
    }

    public Followings(UUID userId, UUID followingId, Date since) {
        this.pk = new FollowingsKey(userId, followingId);
        this.since = since;
    }

    public FollowingsKey getPk() {
        return pk;
    }

    public void setPk(FollowingsKey pk) {
        this.pk = pk;
    }

    public Date getSince() {
        return since;
    }

    public void setSince(Date since) {
        this.since = since;
    }

}
