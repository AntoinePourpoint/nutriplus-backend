package io.ajl.nutriplus.external;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.ajl.nutriplus.external.model.OpenFoodFactResponse;
import io.ajl.nutriplus.testingutil.DefaultObjects;
import io.ajl.nutriplus.util.Ean13;
import java.util.Optional;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@ExtendWith(MockitoExtension.class)
class OpenFoodFactsApiTest {

    private static final String API_NAME = "openfoodfacts";
    private static final String PRODUCT_ENDPOINT = "product/%s";

    @Mock RestClientHelper restClientHelper;

    // use deep stubs so we can mock get().uri(...).retrieve().body(...)
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    RestClient restClient;

    @InjectMocks OpenFoodFactsApi api;

    @Test
    void scanProduct_when200_returnsOptionalWithBody() {
        // Arrange
        Ean13 ean = DefaultObjects.defaultEan13();
        String uriTemplate = "https://fake.api/" + PRODUCT_ENDPOINT;
        String fullUri = uriTemplate.formatted(ean.getCode());

        when(restClientHelper.formatUri(API_NAME, PRODUCT_ENDPOINT)).thenReturn(uriTemplate);

        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(restClient
                        .get()
                        .uri(fullUri)
                        .retrieve()
                        .onStatus(any(Predicate.class), any(RestClient.ResponseSpec.ErrorHandler.class)))
                .thenReturn(responseSpec);

        OpenFoodFactResponse fakeResp = DefaultObjects.defaultOpenFoodFactResponse();
        when(responseSpec.body(OpenFoodFactResponse.class)).thenReturn(fakeResp);

        // Act
        Optional<OpenFoodFactResponse> result = api.scanProduct(ean);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(fakeResp, result.get());
        verify(restClientHelper).formatUri(API_NAME, PRODUCT_ENDPOINT);
    }

    @Test
    void scanProduct_whenHttpError_throwsRestClientException() {
        // Arrange
        Ean13 ean = DefaultObjects.defaultEan13();
        String fakeUri = "https://api/…/product/" + ean.getCode();
        when(restClientHelper.formatUri(any(), any())).thenReturn(fakeUri);

        when(restClient.get().uri(fakeUri).retrieve()).thenThrow(new RestClientException("down"));

        // Act & Assert
        assertThrows(RestClientException.class, () -> api.scanProduct(ean));
    }
}
