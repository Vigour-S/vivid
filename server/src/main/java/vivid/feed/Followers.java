package vivid.feed;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;
import vivid.feed.compositekey.FollowersKey;

import java.util.Date;

/**
 * Created by xiezhuohan on 15-5-27.
 */
@Table(value = "followers")
public class Followers {

    @PrimaryKey
    private FollowersKey pk;

    @Column(value = "since")
    private Date since;

    public FollowersKey getPk() {
        return pk;
    }

    public void setPk(FollowersKey pk) {
        this.pk = pk;
    }

    public Date getSince() {
        return since;
    }

    public void setSince(Date since) {
        this.since = since;
    }

    public  Followers(FollowersKey pk, Date since){
        this.pk = pk;
        this.since = since;
    }

}
