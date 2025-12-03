package com.haiilo.checkout.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI checkoutServiceOpenAPI() {

        Contact contact = new Contact();
        contact.setName("Checkout Service Team");
        contact.setEmail("checkout@service.com");


        Info info = new Info()
            .title("Checkout Service API")
            .version("1.0.0")
            .description("A RESTful API for calculating checkout totals with promotional offers. " +
                "This service handles shopping cart calculations, applying special pricing rules " +
                "and promotional offers (e.g., '2 for 45').")
            .contact(contact);

        return new OpenAPI()
            .info(info);
    }
}
