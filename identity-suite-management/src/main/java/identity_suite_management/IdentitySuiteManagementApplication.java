package identity_suite_management;

import identity_suite_management.config.TelecomConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TelecomConfig.class)
public class IdentitySuiteManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdentitySuiteManagementApplication.class, args);
	}

}
