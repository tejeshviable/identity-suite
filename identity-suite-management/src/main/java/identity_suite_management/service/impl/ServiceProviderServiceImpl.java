package identity_suite_management.service.impl;

import identity_suite_management.config.TelecomConfig;
import identity_suite_management.service.ServiceProviderService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ServiceProviderServiceImpl implements ServiceProviderService {
    @Autowired
    private TelecomConfig telecomConfig;

    @Override
    public String getServiceProviderName(String mobileNumber) {
        log.info("Config :: {}",telecomConfig);
        String prefix = mobileNumber.substring(0, 4);
        for (TelecomConfig.Provider provider : telecomConfig.getProviders()) {
            if(prefix.startsWith("(")){
                prefix=mobileNumber.substring(0,5);
                if(provider.getPrefixes().contains(prefix)){
                    return provider.getProvider();
                }
            }
            else if (provider.getPrefixes().contains(prefix)) {
                return provider.getProvider();
            }

        }
        return "Unknown provider";
    }
}
