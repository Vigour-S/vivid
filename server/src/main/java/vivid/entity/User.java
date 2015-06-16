package vivid.entity;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by wujy on 15-5-12.
 */
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Length(min = 4, max = 20)
    @NotEmpty
    @Column(name = "username", unique = true, nullable = false, length = 45)
    private String username;

    @Length(min = 6, max = 256)
    @NotEmpty
    @Column(name = "password", nullable = false)
    private String password;

    @Email
    @NotEmpty
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "last_login_date")
    private ZonedDateTime lastLoginDate;

    @ManyToMany
    private List<Role> roles;

    public User() {

    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String username, String password, String email, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void addRole(Set<Role> roles) {
        this.roles.addAll(roles);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        if (avatar == null) {
            avatar = "/public/default-avatar.png";
        }
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public ZonedDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(ZonedDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public List<Role> getRoles() {
        if (roles == null) {
            this.roles = new ArrayList<Role>();
        }
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

}
