package pharmacy.pharmacy;

import io.sentry.Sentry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class PharmacyApplication {

    public static void main(String[] args) {
        // Initialize Sentry
        Sentry.init(options -> {
            options.setDsn("https://b756c10294ad1a6d1f8c3d8a09c805e8@o4509248313032704.ingest.us.sentry.io/4509366128279552");
            options.setEnvironment("production");
            options.setRelease("sethma-app@1.0.0");
            // You can add more configuration here if needed
        });

        SpringApplication.run(PharmacyApplication.class, args);
    }
}