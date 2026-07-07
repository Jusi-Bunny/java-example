package com.example.deploylab.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetSystemInfo() throws Exception {
        mockMvc.perform(get("/api/system/info"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Request-Id"))
                .andExpect(jsonPath("$.data.applicationName", is("deploy-lab")))
                .andExpect(jsonPath("$.data.instanceId", notNullValue()));
    }

    @Test
    void shouldMaskSensitiveHeadersWhenInspectingRequest() throws Exception {
        mockMvc.perform(post("/api/http/inspect?keyword=test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer secret")
                        .header("Cookie", "token=secret")
                        .content("{\"hello\":\"world\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.headers.Authorization[0]", is("******")))
                .andExpect(jsonPath("$.data.headers.Cookie[0]", is("******")))
                .andExpect(jsonPath("$.data.queryParameters.keyword[0]", is("test")))
                .andExpect(jsonPath("$.data.body", is("{\"hello\":\"world\"}")));
    }

    @Test
    void shouldCreateListAndDeleteMessage() throws Exception {
        String response = mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"hello\",\"content\":\"deploy\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id", notNullValue()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = response.replaceAll(".*\"id\":(\\d+).*", "$1");

        mockMvc.perform(get("/api/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title", is("hello")));

        mockMvc.perform(delete("/api/messages/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestForInvalidMessageTitle() throws Exception {
        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\",\"content\":\"deploy\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("消息标题不能为空")));
    }

    @Test
    void shouldRejectTooLongDelay() throws Exception {
        mockMvc.perform(get("/api/diagnostics/delay?milliseconds=5001"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectUnsupportedStatusCode() throws Exception {
        mockMvc.perform(get("/api/diagnostics/status?code=418"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldExposeActuatorHealth() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }
}
