package vivid.entity;

import javax.persistence.*;

/**
 * Created by wujy on 15-5-12.
 */
@Entity
@Table(name = "user_roles", uniqueConstraints = @UniqueConstraint(columnNames = {"role", "username"}))
public class UserRole extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @Column(name = "role", nullable = false, length = 45)
    private String role;

    public UserRole() {

    }

    public UserRole(User user, String role) {
        this.user = user;
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
