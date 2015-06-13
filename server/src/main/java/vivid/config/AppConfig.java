package vivid.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by wujy on 15-5-21.
 */
@Configuration
@ComponentScan(basePackages = {"vivid"},
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = {"vivid.controller.*"}))
@PropertySource(value = {"classpath:application.yml"})
@EnableScheduling
@EnableAspectJAutoProxy
@EnableCaching
public class AppConfig {

    @Autowired
    private Environment env;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }
//
//    @Bean
//    public JavaMailSenderImpl javaMailSenderImpl() {
//        JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
//        mailSenderImpl.setHost(env.getProperty("smtp.host"));
//        mailSenderImpl.setPort(env.getProperty("smtp.port", Integer.class));
//        mailSenderImpl.setProtocol(env.getProperty("smtp.protocol"));
//        mailSenderImpl.setUsername(env.getProperty("smtp.username"));
//        mailSenderImpl.setPassword(env.getProperty("smtp.password"));
//
//        Properties javaMailProps = new Properties();
//        javaMailProps.put("mail.smtp.auth", true);
//        javaMailProps.put("mail.smtp.starttls.enable", true);
//
//        mailSenderImpl.setJavaMailProperties(javaMailProps);
//
//        return mailSenderImpl;
//    }

}