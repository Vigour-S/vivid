package vivid.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wujy on 15-6-4.
 */
@Entity
@Table(name = "resources")
public class Resource extends BaseEntity {

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "digest", nullable = false, length = 255)
    private String digest;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = true, length = 255)
    private String description;

    public Resource() {

    }

    public Resource(Long size, String digest, String name, String description) {
        this.size = size;
        this.digest = digest;
        this.name = name;
        this.description = description;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
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

}
