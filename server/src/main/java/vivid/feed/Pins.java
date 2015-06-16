package vivid.feed;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.Date;
import java.util.UUID;

/**
 * Created by xiezhuohan on 15-5-27.
 */
@Table(value = "pins")
public class Pins {

    @Column(value = "user_id")
    private UUID userId;

    @PrimaryKey(value = "pin_id")
    private UUID pinId;

    @Column(value = "body")
    private String body;

    @Column(value = "time")
    private Date time;

    public Pins(UUID userId, UUID pinId, Date time, String body) {
        this.pinId = pinId;
        this.userId = userId;
        this.time = time;
        this.body = body;
    }

    public UUID getPinId() {
        return pinId;
    }

    public UUID getUserId() {
        return userId;
    }

    public Date getTime() {
        return time;
    }

    public String getBody() {
        return body;
    }


}
