package vivid;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.social.SocialWebAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by fantasticfears on 5/7/15.
 */
@EnableAutoConfiguration(exclude= SocialWebAutoConfiguration.class)
@Configuration
@ComponentScan
@EntityScan
@EnableJpaRepositories
public class SiteConfig {

}
