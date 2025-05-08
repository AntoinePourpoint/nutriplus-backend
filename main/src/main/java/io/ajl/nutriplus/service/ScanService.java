package io.ajl.nutriplus.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ajl.nutriplus.exception.ProductNotFoundException;
import io.ajl.nutriplus.external.OpenFoodFactsApi;
import io.ajl.nutriplus.external.model.OpenFoodFactResponse;
import io.ajl.nutriplus.service.converter.OpenFoodFactToProductConverter;
import io.ajl.nutriplus.service.model.Product;
import io.ajl.nutriplus.util.Ean13;
import java.time.Duration;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ScanService {

    private static final Logger log = LoggerFactory.getLogger(ScanService.class);
    private static final Duration TTL = Duration.ofDays(7);

    private final OpenFoodFactsApi openFoodFactsApi;
    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper;
    private final OpenFoodFactToProductConverter openFoodFactToProductConverter;

    public ScanService(
            OpenFoodFactsApi openFoodFactsApi,
            StringRedisTemplate redis,
            ObjectMapper objectMapper,
            OpenFoodFactToProductConverter openFoodFactToProductConverter) {
        this.openFoodFactsApi = openFoodFactsApi;
        this.redis = redis;
        this.objectMapper = objectMapper;
        this.openFoodFactToProductConverter = openFoodFactToProductConverter;
    }

    public Product scanProduct(Ean13 ean) {
        String key = "food:barcode:" + ean.getCode();
        String cached = redis.opsForValue().get(key);
        Optional<OpenFoodFactResponse> dto;

        if (cached != null) {
            try {
                dto = Optional.of(objectMapper.readValue(cached, OpenFoodFactResponse.class));
            } catch (JsonProcessingException e) {
                log.error("Error parsing JSON from Redis cache", e);
                redis.delete(key);
                dto = openFoodFactsApi.scanProduct(ean);
            }
        } else {
            dto = openFoodFactsApi.scanProduct(ean);
            dto.ifPresent(resp -> addKeyInCache(resp, key));
        }

        return dto.map(openFoodFactToProductConverter::convert)
                .orElseThrow(() -> new ProductNotFoundException(ean));
    }

    private void addKeyInCache(OpenFoodFactResponse resp, String key) {
        try {
            redis.opsForValue().set(key, objectMapper.writeValueAsString(resp), TTL);
        } catch (JsonProcessingException e) {
            log.error("Error setting JSON into Redis cache", e);
        }
    }
}
