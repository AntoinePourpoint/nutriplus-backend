package io.ajl.nutriplus.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ajl.nutriplus.exception.ProductNotFoundException;
import io.ajl.nutriplus.external.OpenFoodFactsApi;
import io.ajl.nutriplus.external.model.OpenFoodFactResponse;
import io.ajl.nutriplus.service.converter.OpenFoodFactToProductConverter;
import io.ajl.nutriplus.service.model.Product;
import io.ajl.nutriplus.testingutil.DefaultObjects;
import io.ajl.nutriplus.util.Ean13;
import java.time.Duration;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
class ScanServiceTest {

    @Mock private ObjectMapper objectMapper;
    @Mock private OpenFoodFactsApi openFoodFactsApi;
    @Mock private StringRedisTemplate redis;
    @Mock private OpenFoodFactToProductConverter openFoodFactToProductConverter;

    @InjectMocks private ScanService scanService;

    @Test
    void scan_shouldReturnResultFromApi_whenKeyIsNotCached() throws Exception {
        // Arrange
        ValueOperations<String, String> valueOperationsMock = mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(valueOperationsMock);

        when(valueOperationsMock.get(anyString())).thenReturn(null);
        when(openFoodFactsApi.scanProduct(any(Ean13.class)))
                .thenReturn(Optional.of(DefaultObjects.defaultOpenFoodFactResponse()));
        when(objectMapper.writeValueAsString(any())).thenReturn("");
        doNothing().when(valueOperationsMock).set(anyString(), anyString(), any(Duration.class));
        when(openFoodFactToProductConverter.convert(any(OpenFoodFactResponse.class)))
                .thenReturn(DefaultObjects.defaultProduct());

        // Act
        Product product = scanService.scanProduct(Ean13.of("3017620421006"));

        // Assert
        assertNotNull(product);

        verify(openFoodFactsApi).scanProduct(any(Ean13.class));
        verify(openFoodFactToProductConverter).convert(any(OpenFoodFactResponse.class));
    }

    @Test
    void scan_shouldReturnResultFromRedis_whenKeyIsCached() throws Exception {
        // Arrange
        ValueOperations<String, String> valueOperationsMock = mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(valueOperationsMock);
        when(valueOperationsMock.get(anyString())).thenReturn("cachedValue");
        when(objectMapper.readValue(anyString(), eq(OpenFoodFactResponse.class)))
                .thenReturn(DefaultObjects.defaultOpenFoodFactResponse());
        when(openFoodFactToProductConverter.convert(any())).thenReturn(DefaultObjects.defaultProduct());

        // Act
        Product product = scanService.scanProduct(Ean13.of("3017620421006"));

        // Assert
        assertNotNull(product);
        verifyNoInteractions(openFoodFactsApi);
        verify(openFoodFactToProductConverter).convert(any(OpenFoodFactResponse.class));
    }

    @Test
    void scan_shouldReturnResultFromApi_whenKeyIsCachedButCannotBeRead() throws Exception {
        // Arrange
        ValueOperations<String, String> valueOperationsMock = mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(valueOperationsMock);
        when(valueOperationsMock.get(anyString())).thenReturn("cachedValue");
        doThrow(JsonProcessingException.class)
                .when(objectMapper)
                .readValue(anyString(), eq(OpenFoodFactResponse.class));
        when(redis.delete(anyString())).thenReturn(true);
        when(openFoodFactsApi.scanProduct(any(Ean13.class)))
                .thenReturn(Optional.of(DefaultObjects.defaultOpenFoodFactResponse()));
        when(openFoodFactToProductConverter.convert(any())).thenReturn(DefaultObjects.defaultProduct());

        // Act
        Product product = scanService.scanProduct(Ean13.of("3017620421006"));

        // Assert
        assertNotNull(product);
        verify(openFoodFactsApi).scanProduct(any(Ean13.class));
        verify(redis).delete(anyString());
    }

    @Test
    void scan_shouldThrowProductNotFoundException_whenKeyIsNotCachedAndApiReturnsNothing()
            throws Exception {
        // Arrange
        ValueOperations<String, String> valueOperationsMock = mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(valueOperationsMock);
        when(valueOperationsMock.get(anyString())).thenReturn(null);
        when(openFoodFactsApi.scanProduct(any(Ean13.class))).thenReturn(Optional.empty());

        // Act & assert
        Ean13 ean13 = DefaultObjects.defaultEan13();
        assertThrows(ProductNotFoundException.class, () -> scanService.scanProduct(ean13));
        verify(openFoodFactsApi).scanProduct(any(Ean13.class));
        verifyNoInteractions(openFoodFactToProductConverter);
    }
}
