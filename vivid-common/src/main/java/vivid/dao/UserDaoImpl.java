package vivid.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import vivid.entity.User;

import java.util.List;

/**
 * Created by wujy on 15-5-12.
 */
@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    @Override
    public User findByUsername(String username) {
        List<User> users = sessionFactory.getCurrentSession().createQuery("from User where username = ?")
                .setParameter(0, username).list();
        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public User findByEmail(String email) {
        List<User> users = sessionFactory.getCurrentSession().createQuery("from User where email = ?")
                .setParameter(0, email).list();
        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public User findByUsernameOrEmail(String usernameOrEmail) {
        List<User> users = sessionFactory.getCurrentSession().createQuery("from User where username = ? or email = ?")
                .setParameter(0, usernameOrEmail).setParameter(1, usernameOrEmail).list();
        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

}
