import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.ThreadState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import vivid.VividApplication;
import vivid.config.*;
import vivid.entity.Resource;
import vivid.entity.User;
import vivid.repository.ResourceRepository;
import vivid.repository.UserRepository;
import vivid.service.ResourceService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by wujy on 15-6-21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {VividApplication.class, AppConfig.class, PersistenceConfig.class, CassandraConfig.class, ShiroConfig.class, WebMvcConfig.class})
@WebAppConfiguration
public class VividApplicationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DefaultPasswordService passwordService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ResourceRepository resourceRepository;

    private MockMvc mvc;

    @Before
    public void setUp() {
        // for Shiro
        Subject mockSubject = Mockito.mock(Subject.class);
        ThreadState threadState = new SubjectThreadState(mockSubject);
        threadState.bind();

        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void testHome() throws Exception {
        this.mvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    public void testUserRepository() throws Exception {
        this.mvc.perform(put("/users")).andExpect(status().isOk());

        User user1 = userRepository.findByEmail("wujysh@gmail.com");
        Assert.assertNotEquals(user1.getPassword(), passwordService.encryptPassword("123456"));

        User user2 = userRepository.findByUsername("ErickGuan");
        Assert.assertEquals(user2.getEmail(), "fantasticfears@gmail.com");

        User user3 = userRepository.findByUsernameLike("%Xie%").get(0);
        Assert.assertEquals(user3.getUsername(), "JasonXie");

        List<User> users = userRepository.findAll();
        assert (!users.get(0).equals(user1));
        assert (!users.get(1).equals(user2));
        assert (!users.get(2).equals(user3));
    }

    @Test
    public void testResourceUpload() throws Exception {
        // login first
        this.mvc.perform(post("/login").param("username", "wujysh@gmail.com").param("password", "123456"))
                .andExpect(status().isFound());

        String urlToDownload = "http://www.dhu.edu.cn/dhuzyimages/index_r1_c1.jpg";

        this.mvc.perform(post("/resources/upload_by_url").param("url", urlToDownload))
                .andExpect(status().isOk());
        List<Resource> resources = resourceRepository.findAll();
        Assert.assertNotEquals(resources.size(), 0);

        Resource resource = resources.get(resources.size() - 1);
        MultipartFile file = resourceService.downloadFile(urlToDownload);

        this.mvc.perform(get("/resources/view/" + resource.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(file.getContentType()))
                .andExpect(content().bytes(file.getBytes()));

        resourceRepository.delete(resource);
    }

}
