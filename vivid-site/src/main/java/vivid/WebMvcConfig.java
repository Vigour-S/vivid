package vivid;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import vivid.support.DefaultLayoutInterceptor;

/**
 * Created by fantasticfears on 15-5-21.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(new DefaultLayoutInterceptor());
    }

}
