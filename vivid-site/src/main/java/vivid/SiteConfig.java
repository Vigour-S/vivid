package vivid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.http.impl.client.HttpClientBuilder;
import org.h2.server.web.WebServlet;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.social.SocialWebAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.DispatcherServlet;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

import javax.sql.DataSource;

/**
 * Main configuration resource for the Vivid web application. The use of @ComponentScan
 * here ensures that other @Configuration classes such as {@link MvcConfig} and
 * {@link SecurityConfig} are included as well.
 *
 * @see SiteMain#main(String[])
 */
@EnableAutoConfiguration(exclude=SocialWebAutoConfiguration.class)
@Configuration
@ComponentScan
@EntityScan
@EnableJpaRepositories
public class SiteConfig {

}