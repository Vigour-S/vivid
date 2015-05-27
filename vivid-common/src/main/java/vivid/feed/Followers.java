package vivid.feed;

import java.util.Date;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by xiezhuohan on 15-5-27.
 */

@Table(value = "followers")
public class Followers{

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

}
