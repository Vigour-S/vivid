package vivid.feed;

import java.util.UUID;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by xiezhuohan on 15-5-27.
 */

@Table(value = "timeline")
public class TimeLine{

    @PrimaryKey
    private TimeLineKey pk;

    @Column(value = "pin_id")
    private UUID pinId;

    public TimeLineKey getPk() {
        return pk;
    }

    public void setPk(TimeLineKey pk) {
        this.pk = pk;
    }

    public UUID getPinId() {
        return pinId;
    }

    public void setPinId(UUID pinId) {
        this.pinId = pinId;
    }

}
