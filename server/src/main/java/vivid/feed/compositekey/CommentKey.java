package vivid.feed.compositekey;

import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by xiezhuohan on 15-6-12.
 */
@PrimaryKeyClass
public class CommentKey implements Serializable {

    @PrimaryKeyColumn(name = "pin_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID pinId;

    @PrimaryKeyColumn(name = "time", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private Date time;

    public CommentKey(UUID pinId, Date time) {
        this.pinId = pinId;
        this.time = time;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((time == null) ? 0 : time.hashCode());
        result = prime * result + ((pinId == null) ? 0 : pinId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CommentKey other = (CommentKey) obj;
        if (time == null) {
            if (other.time != null)
                return false;
        } else if (!time.equals(other.time))
            return false;
        if (pinId == null) {
            if (other.pinId != null)
                return false;
        } else if (!pinId.equals(other.pinId))
            return false;
        return true;
    }

    public UUID getPinId() {
        return pinId;
    }

    public void setPinId(UUID pin_id) {
        this.pinId = pin_id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

}
