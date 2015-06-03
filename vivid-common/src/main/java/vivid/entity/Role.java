package vivid.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wujy on 15-5-12.
 */
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "description", nullable = true, length = 100)
    private String description;

    @ManyToMany
    private List<Permission> permissions;

    public Role() {

    }

    public Role(String name) {
        this.name = name;
    }

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

    public List<Permission> getPermissions() {
        if (permissions == null) {
            this.permissions = new ArrayList<Permission>();
        }
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

}
