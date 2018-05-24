package app;

import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class AppConf extends Configuration {
    @NotEmpty
    private String serviceUrl;

    @NotEmpty
    private String casServerLoginUrl;

    @NotEmpty
    private String casServerUrlPrefix;

    @NotEmpty
    private String casServerLogoutUrl;

    public String getServiceUrl() {
        return serviceUrl;
    }

    public String getCasServerLoginUrl() {
        return casServerLoginUrl;
    }

    public String getCasServerUrlPrefix() {
        return casServerUrlPrefix;
    }

    public String getCasServerLogoutUrl() {
        return casServerLogoutUrl;
    }
}
