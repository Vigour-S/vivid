import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.ThreadState;
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
import vivid.VividApplication;
import vivid.config.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    private MockMvc mvc;

    protected Subject mockSubject;

    @Before
    public void setUp() {
        // for Shiro
        mockSubject = Mockito.mock(Subject.class);
        ThreadState threadState = new SubjectThreadState(mockSubject);
        threadState.bind();

        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void testHome() throws Exception {
        this.mvc.perform(get("/")).andExpect(status().isOk());
    }

}
