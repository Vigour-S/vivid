package vivid.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by wujy on 15-6-4.
 */
@Entity
@Table(name = "resources")
@JsonIgnoreProperties({"id", "createdDate", "modifiedDate", "digest", "description", "contentType"})
public class Resource extends BaseEntity {

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "digest", nullable = false, length = 255)
    private String digest;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = true, length = 255)
    private String description;

    @Column(name = "content_type", nullable = true, length = 255)
    private String contentType;

    @Transient
    private String url;

    public Resource() {

    }

    public Resource(Long size, String digest, String name, String description, String contentType) {
        this.size = size;
        this.digest = digest;
        this.name = name;
        this.description = description;
        this.contentType = contentType;
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
