package myebay.store.configuration;

import myebay.store.constants.Constants;
import myebay.store.model.Item;
import org.json.JSONArray;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public static HttpEntity<?> getEntityWithHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, Constants.AUTH_KEY);
        headers.set(HttpHeaders.ACCEPT, "application/json");

       return new HttpEntity<>(headers);
    }
}