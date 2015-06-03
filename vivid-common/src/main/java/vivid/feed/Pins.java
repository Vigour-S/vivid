package vivid.feed;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.UUID;

/**
 * Created by xiezhuohan on 15-5-27.
 */
@Table(value = "pins")
public class Pins {

    @PrimaryKey(value = "pin_id")
    private UUID pinId;

    @Column(value = "user_id")
    private UUID userId;

    @Column(value = "body")
    private String body;

    public Pins(UUID pinId, UUID userId, String body) {
        this.pinId = pinId;
        this.userId = userId;
        this.body = body;
    }

    public UUID getPinId() {
        return pinId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getBody() {
        return body;
    }

}
