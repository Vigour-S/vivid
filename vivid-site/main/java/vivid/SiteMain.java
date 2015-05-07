package vivid;

/**
 * The entry point for the Vivid web application.
 */
public class SiteMain {

    public static void main(String[] args) {
        new VividApplication(SiteConfig.class).run(args);
    }
}