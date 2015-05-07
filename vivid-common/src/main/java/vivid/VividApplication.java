package vivid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;

import static java.lang.String.format;
import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;
import static vivid.VividProfiles.*;

/**
 * A specialized {@link SpringApplication} that enforces {@link VividProfiles} semantics.
 * See vivid.SiteMain and vivid.IndexerMain classes for usage examples.
 */
public class VividApplication extends SpringApplication {

    private static final Log logger = LogFactory.getLog(VividApplication.class);

    public VividApplication(Class<?> configClass) {
        super(configClass);
    }

    /**
     * Enforce mutual exclusivity and implicit activation of profiles as described in
     * {@link VividProfiles}.
     */
    @Override
    protected void configureProfiles(ConfigurableEnvironment environment, String[] args) {
        super.configureProfiles(environment, args);

        boolean standaloneActive = environment.acceptsProfiles(STANDALONE);
        boolean stagingActive = environment.acceptsProfiles(STAGING);
        boolean productionActive = environment.acceptsProfiles(PRODUCTION);

        if (stagingActive && productionActive) {
            throw new IllegalStateException(format("Only one of the following profiles may be specified: [%s]",
                    arrayToCommaDelimitedString(new String[] { STAGING, PRODUCTION })));
        }

        if (standaloneActive) {
            logger.info("The default 'standalone' profile is active because no other profiles have been specified.");
        }
        else {
            throw new IllegalStateException(format("Unknown profile(s) specified: [%s]. Valid profiles are: [%s]",
                    arrayToCommaDelimitedString(environment.getActiveProfiles()),
                    arrayToCommaDelimitedString(new String[] {
                            arrayToCommaDelimitedString(environment.getDefaultProfiles()), STAGING, PRODUCTION })));
        }
    }
}