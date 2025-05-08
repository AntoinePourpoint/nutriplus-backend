package io.ajl.nutriplus.external;

import io.ajl.nutriplus.external.model.OpenFoodFactResponse;
import io.ajl.nutriplus.util.Ean13;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OpenFoodFactsApi {

    private static final String API_NAME = "openfoodfacts";
    private static final String PRODUCT_ENDPOINT = "product/%s";

    private final RestClientHelper restClientHelper;
    private final RestClient restClient;

    public OpenFoodFactsApi(RestClientHelper restClientHelper, RestClient restClient) {
        this.restClientHelper = restClientHelper;
        this.restClient = restClient;
    }

    public Optional<OpenFoodFactResponse> scanProduct(Ean13 ean) {
        var product =
                restClient
                        .get()
                        .uri(restClientHelper.formatUri(API_NAME, PRODUCT_ENDPOINT).formatted(ean.getCode()))
                        .retrieve()
                        .onStatus(status -> status.equals(HttpStatus.NOT_FOUND), (_, _) -> {})
                        .body(OpenFoodFactResponse.class);

        return Optional.ofNullable(product);
    }
}
