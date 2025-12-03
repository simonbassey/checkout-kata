package com.haiilo.checkout.api;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiilo.checkout.core.domain.model.CheckoutRequest;
import com.haiilo.checkout.core.domain.model.LineItem;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CheckoutResourceIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void calculateTotal_ShouldSucceedWith200StatusCode() throws Exception {
        CheckoutRequest request = createCheckoutRequest(
            new LineItem("Apple", 3),
            new LineItem("Banana", 2)
        );

        mockMvc.perform(post("/api/checkout/calculate-total")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalPrice", is(175.0)));
    }

    @Test
    void calculateTotal_ShouldHandleMultipleLineItems() throws Exception {
        CheckoutRequest request = createCheckoutRequest(
            new LineItem("Apple", 5),
            new LineItem("Banana", 3),
            new LineItem("Peach", 1)
        );

        mockMvc.perform(post("/api/checkout/calculate-total")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalPrice", is(310.0)));
    }

    @Test
    void calculateTotal_ShouldFailWithBadRequest_WhenItemsListIsEmpty() throws Exception {
        CheckoutRequest request = new CheckoutRequest();
        request.setItems(Collections.emptyList());

        mockMvc.perform(post("/api/checkout/calculate-total")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void calculateTotal_ShouldFailWithBadRequest_WhenItemsListIsNull() throws Exception {
        CheckoutRequest request = new CheckoutRequest();
        request.setItems(null);

        mockMvc.perform(post("/api/checkout/calculate-total")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void calculateTotal_ShouldFailWithBadRequestWhenQuantityIsLessThanOne() throws Exception {
        CheckoutRequest request = new CheckoutRequest();
        request.setItems(List.of(new LineItem("Apple", 0)));

        mockMvc.perform(post("/api/checkout/calculate-total")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }


    @Test
    void calculateTotal_ShouldFailWhenPriceNotFound() throws Exception {
        CheckoutRequest request = createCheckoutRequest(new LineItem("Unknown", 1));

        mockMvc.perform(post("/api/checkout/calculate-total")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString("unknown")));
    }


    @Test
    void shouldReturn400WhenRequestBodyIsInvalid() throws Exception {
        String invalidJson = "{\"items\": \"not-a-list\"}";

        mockMvc.perform(post("/api/checkout/calculate-total")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
            .andExpect(status().isBadRequest());
    }

    private CheckoutRequest createCheckoutRequest(LineItem... items) {
        CheckoutRequest request = new CheckoutRequest();
        request.setItems(Arrays.asList(items));
        return request;
    }
}
