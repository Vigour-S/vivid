package vivid.support;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.ZonedDateTime;

/**
 * Created by wujy on 15-5-14.
 */
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    Long id;

    @CreatedDate
    @Column(name = "created_date")
    ZonedDateTime createdDate;

    @LastModifiedDate
    @Column(name = "modified_date")
    ZonedDateTime modifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

}
