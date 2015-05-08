package vivid;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

public abstract class DatabaseConfig {

    @Bean
    public abstract DataSource dataSource();

    protected void configureDataSource(org.apache.tomcat.jdbc.pool.DataSource dataSource) {
        dataSource.setMaxActive(20);
        dataSource.setMaxIdle(20);
        dataSource.setMinIdle(5);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
    }
}

@Configuration
@Profile(VividProfiles.STANDALONE)
class StandaloneDatabaseConfig extends DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/vivid");
        dataSource.setUsername("vivid");
        dataSource.setPassword("vivid");
        dataSource.setValidationQuery("SELECT 1");

        configureDataSource(dataSource);

        return dataSource;
    }
}
