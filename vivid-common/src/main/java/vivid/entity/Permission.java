package vivid.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wujy on 15-6-3.
 */
@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "description", nullable = true, length = 100)
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
