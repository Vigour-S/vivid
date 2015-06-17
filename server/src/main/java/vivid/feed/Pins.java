package vivid.feed;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;
import vivid.feed.compositekey.PinsKey;

import java.util.Date;
import java.util.UUID;

/**
 * Created by xiezhuohan on 15-5-27.
 */
@Table(value = "pins")
public class Pins {

    @PrimaryKey
    private PinsKey pk;

    @Column(value = "body")
    private String body;

    @Column(value = "time")
    private Date time;

    public Pins(UUID userId, UUID pinId, Date time, String body) {
        this.pk = new PinsKey(userId, pinId);
        this.time = time;
        this.body = body;
    }

    public PinsKey getPk() {
        return pk;
    }

    public void setUserId(PinsKey pk) {
        this.pk = pk;
    }

    public Date getTime() {
        return time;
    }

    public String getBody() {
        return body;
    }


}
