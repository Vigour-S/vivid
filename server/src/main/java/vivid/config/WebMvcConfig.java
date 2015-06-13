package vivid.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.AppCacheManifestTransformer;
import org.springframework.web.servlet.resource.VersionResourceResolver;

/**
 * Created by wujy on 15-5-25.
 */
@Configuration
@ComponentScan(basePackages = {"vivid.controller"})
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private Environment env;

    @Value("${resources.projectroot:}")
    private String projectRoot;

    @Value("${app.version:}")
    private String appVersion;


    private String getProjectRootRequired() {
        Assert.state(this.projectRoot != null, "Please set \"resources.projectRoot\" in application.yml");
        return this.projectRoot;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/login").setViewName("sessions/new");
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        registry.addViewController("signup").setViewName("signup");
//        registry.addViewController("login").setViewName("login");
//        registry.addViewController("welcome").setViewName("welcome");
//        registry.addViewController("admin").setViewName("admin");
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1000000);
        return multipartResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        boolean devMode = this.env.acceptsProfiles("development");

        String location = devMode ? "file:///" + getProjectRootRequired() + "/client/src/" : "classpath:static/";
        Integer cachePeriod = devMode ? 0 : null;
        boolean useResourceCache = !devMode;
        String version = getApplicationVersion();

        AppCacheManifestTransformer appCacheTransformer = new AppCacheManifestTransformer();
        VersionResourceResolver versionResolver = new VersionResourceResolver()
                .addFixedVersionStrategy(version, "/**/*.js", "/**/*.map")
                .addContentVersionStrategy("/**");

        registry.addResourceHandler("/**")
                .addResourceLocations(location)
                .setCachePeriod(cachePeriod)
                .resourceChain(useResourceCache)
                .addResolver(versionResolver)
                .addTransformer(appCacheTransformer);
    }

    protected String getApplicationVersion() {
        return this.env.acceptsProfiles("development") ? "dev" : this.appVersion;
    }
}

