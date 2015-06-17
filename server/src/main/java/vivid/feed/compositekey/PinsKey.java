package vivid.feed.compositekey;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import vivid.feed.Pins;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by xiezhuohan on 15-6-16.
 */
@PrimaryKeyClass
public class PinsKey implements Serializable {

    @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID userId;

    @PrimaryKeyColumn(name = "pin_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private UUID PinId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getPinId() {
        return PinId;
    }

    public void setPinId(UUID PinId) {
        this.PinId = PinId;
    }

    public PinsKey(UUID userId, UUID PinId) {
        this.userId = userId;
        this.PinId = PinId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((PinId == null) ? 0 : PinId.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
        PinsKey other = (PinsKey) obj;
        if (PinId == null) {
            if (other.PinId != null)
                return false;
        } else if (!PinId.equals(other.PinId))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

}