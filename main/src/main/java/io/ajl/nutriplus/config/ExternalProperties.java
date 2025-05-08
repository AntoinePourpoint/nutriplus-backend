package io.ajl.nutriplus.config;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "nutriplus.external")
public record ExternalProperties(Map<String, ApiProperties> apis) {

    public record ApiProperties(String url) {}
}
