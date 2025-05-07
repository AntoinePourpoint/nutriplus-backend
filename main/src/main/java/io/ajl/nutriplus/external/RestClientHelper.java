package io.ajl.nutriplus.external;

import io.ajl.nutriplus.config.ExternalProperties;
import org.springframework.stereotype.Component;

@Component
public class RestClientHelper {

    private final ExternalProperties externalProperties;

    public RestClientHelper(ExternalProperties externalProperties) {
        this.externalProperties = externalProperties;
    }

    public String formatUri(String apiName, String endpoint) {
        return "%s/%s".formatted(
                externalProperties.apis().get(apiName).url(),
                endpoint
        );
    }
}
