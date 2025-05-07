package io.ajl.nutriplus.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "nutriplus.external")
public record ExternalProperties(
        Map<String, ApiProperties> apis
) {

    public record ApiProperties(
            String url
    ) {}
}
