package vivid.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by wujy on 15-5-25.
 */
@Configuration
@ComponentScan(basePackages = {"vivid.controller"})
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/login").setViewName("sessions/new");
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        registry.addViewController("signup").setViewName("signup");
//        registry.addViewController("login").setViewName("login");
//        registry.addViewController("welcome").setViewName("welcome");
//        registry.addViewController("admin").setViewName("admin");
    }

}