package io.ajl.nutriplus.external;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import io.ajl.nutriplus.config.ExternalProperties;
import io.ajl.nutriplus.config.ExternalProperties.ApiProperties;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestClientHelperTest {

    @Mock private ExternalProperties externalProperties;

    @InjectMocks private RestClientHelper restClientHelper;

    @Test
    void formatUri_shouldProperlyFormat() {
        // Arrange
        Map<String, ApiProperties> apiPropertiesMap =
                Map.of("API_NAME", new ApiProperties("https://my-domain.com"));
        when(externalProperties.apis()).thenReturn(apiPropertiesMap);

        // Act
        String uri = restClientHelper.formatUri("API_NAME", "my/endpoint");

        assertEquals("https://my-domain.com/my/endpoint", uri);
    }
}
