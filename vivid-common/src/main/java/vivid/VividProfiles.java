package vivid;

import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Spring profiles under which a {@link VividApplication} can be run. If no profiles are
 * specified, the {@link #STANDALONE} profile will be implicitly activated, making
 * available for example an in-memory database as opposed to expecting a full-blown
 * PostgreSQL instance to be available. This arrangement reflects the assumption that most
 * users of this reference application will want to clone the repo and run the app locally
 * before deploying to Cloud Foundry.
 *
 * Mutual exclusivity and implicit activation of profiles as described below is enforced
 * by {@link vivid.VividApplication} {@code configureProfiles} method.
 */
public final class VividProfiles {
    /**
     * When active, indicates that the application is being deployed to the "staging"
     * space of a Cloud Foundry instance. Implicitly activates {@link #CLOUDFOUNDRY}
     * profile, and is mutually exclusive with {@link #PRODUCTION} and {@link #STANDALONE}
     * profiles.
     */
    public static final String STAGING = "staging";

    /**
     * When active, indicates that the application is being deployed to the "production"
     * space of a Cloud Foundry instance. Implicitly activates {@link #CLOUDFOUNDRY}
     * profile, and is mutually exclusive with {@link #STAGING} and {@link #STANDALONE}
     * profiles.
     */
    public static final String PRODUCTION = "production";

    /**
     * The default profile for any {@link VividApplication}. Indicates that the
     * application is running locally, i.e. on a developer machine as opposed to running
     * on {@link #CLOUDFOUNDRY} and should expect to find data sources, etc in-memory as
     * opposed to finding them as Cloud Foundry services. This profile constant is named
     * "STANDALONE" to clearly communicate its role, but its value is actually "default",
     * as this is the "reserved default profile name" in Spring. This means that
     * "STANDALONE" will always be treated as the default profile without requiring any
     * code to programmatically activate it. This makes running integration tests that
     * expect in-memory resources simple to set up.
     *
     * @see vivid.StandaloneDatabaseConfig
     * @see org.springframework.core.env.AbstractEnvironment#RESERVED_DEFAULT_PROFILE_NAME
     */
    public static final String STANDALONE = "default";
}