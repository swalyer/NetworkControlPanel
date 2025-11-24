package ru.example.networkmonitoring;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.example.networkmonitoring.model.Host;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "monitoring.simulation-mode=true",
        "monitoring.timeout-millis=500"
})
@AutoConfigureMockMvc
class HostControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullLifecycleShouldSucceed() throws Exception {
        Host host = new Host();
        host.setName("Edge Router");
        host.setIpAddress("10.0.0.5");
        host.setRole("gateway");
        host.setDescription("Primary edge device");

        String body = objectMapper.writeValueAsString(host);

        String creationResponse = mockMvc.perform(post("/api/hosts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode createdNode = objectMapper.readTree(creationResponse);
        long id = createdNode.get("id").asLong();

        mockMvc.perform(get("/api/hosts/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Edge Router"))
                .andExpect(jsonPath("$.ipAddress").value("10.0.0.5"));

        mockMvc.perform(post("/api/hosts/" + id + "/check"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hostId").value(id));

        mockMvc.perform(delete("/api/hosts/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/hosts/" + id))
                .andExpect(status().isNotFound());
    }
}
