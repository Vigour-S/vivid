package vivid.feed;

import java.util.Date;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by xiezhuohan on 15-5-27.
 */

@Table(value = "followings")
public class Followings{

    @PrimaryKey
    private FollowingsKey pk;

    @Column(value = "since")
    private Date since;

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
