package io.ajl.nutriplus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.client.RestClient;

@SpringBootApplication
@ConfigurationPropertiesScan
public class NutriplusBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NutriplusBackendApplication.class, args);
    }

    @Bean
    public RestClient restClient() { // TODO improve the setup
        return RestClient.builder().build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder().addModule(new JavaTimeModule()).build();
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }
}
