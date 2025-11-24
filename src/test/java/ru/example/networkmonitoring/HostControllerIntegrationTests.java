package ru.example.networkmonitoring;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.example.networkmonitoring.dto.HostRequest;
import ru.example.networkmonitoring.dto.NetworkSegmentRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HostControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void hostLifecycleWithSegment() throws Exception {
        NetworkSegmentRequest segmentRequest = new NetworkSegmentRequest();
        segmentRequest.setName("Test Segment");
        segmentRequest.setDescription("Segment for tests");
        segmentRequest.setNetworkCidr("172.16.0.0/24");
        segmentRequest.setLocation("Lab");
        String segmentBody = objectMapper.writeValueAsString(segmentRequest);
        String segmentResponse = mockMvc.perform(post("/api/segments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(segmentBody))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode segmentNode = objectMapper.readTree(segmentResponse);
        long segmentId = segmentNode.get("id").asLong();

        HostRequest hostRequest = new HostRequest();
        hostRequest.setName("Edge Router");
        hostRequest.setIpAddress("10.10.0.5");
        hostRequest.setRole("gateway");
        hostRequest.setDescription("Primary edge device");
        hostRequest.setCriticality("HIGH");
        hostRequest.setMonitored(true);
        hostRequest.setNetworkSegmentId(segmentId);
        String hostBody = objectMapper.writeValueAsString(hostRequest);

        String creationResponse = mockMvc.perform(post("/api/hosts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hostBody))
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
                .andExpect(jsonPath("$.ipAddress").value("10.10.0.5"))
                .andExpect(jsonPath("$.networkSegmentId").value(segmentId));

        mockMvc.perform(put("/api/hosts/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hostBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monitored").value(true));

        mockMvc.perform(post("/api/hosts/" + id + "/check"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hostId").value(id));

        String historyResponse = mockMvc.perform(get("/api/hosts/" + id + "/history"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode historyNode = objectMapper.readTree(historyResponse);
        assertThat(historyNode.isArray()).isTrue();
        assertThat(historyNode.size()).isGreaterThanOrEqualTo(1);

        mockMvc.perform(delete("/api/hosts/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/segments/" + segmentId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/hosts/" + id))
                .andExpect(status().isNotFound());
    }
}
