package identity_suite_management.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "telecom")
public class TelecomConfig {

    private List<Provider> providers;

    public List<Provider> getProviders() {
        return providers;
    }

    public void setProviders(List<Provider> providers) {
        this.providers = providers;
    }
    @PostConstruct
    public void postConstruct() {
        System.out.println("TelecomConfig initialized with providers: " + providers.toString());
    }


    public static class Provider {
        private String provider;
        private List<String> prefixes;
        private String type;

        // Getters and Setters
        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public List<String> getPrefixes() {
            return prefixes;
        }

        public void setPrefixes(List<String> prefixes) {
            this.prefixes = prefixes;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}

