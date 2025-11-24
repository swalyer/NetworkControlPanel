package ru.example.networkmonitoring.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.example.networkmonitoring.dto.HostResponse;
import ru.example.networkmonitoring.dto.NetworkSegmentRequest;
import ru.example.networkmonitoring.dto.NetworkSegmentResponse;
import ru.example.networkmonitoring.service.HostService;
import ru.example.networkmonitoring.service.NetworkSegmentService;

@RestController
@RequestMapping("/api/segments")
public class NetworkSegmentController {

    private final NetworkSegmentService networkSegmentService;
    private final HostService hostService;

    public NetworkSegmentController(NetworkSegmentService networkSegmentService, HostService hostService) {
        this.networkSegmentService = networkSegmentService;
        this.hostService = hostService;
    }

    @GetMapping
    public List<NetworkSegmentResponse> listSegments() {
        return networkSegmentService.listSegments();
    }

    @GetMapping("/{id}")
    public NetworkSegmentResponse getSegment(@PathVariable Long id) {
        return networkSegmentService.getSegment(id);
    }

    @PostMapping
    public ResponseEntity<NetworkSegmentResponse> createSegment(@Valid @RequestBody NetworkSegmentRequest request) {
        NetworkSegmentResponse response = networkSegmentService.createSegment(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSegment(@PathVariable Long id) {
        networkSegmentService.deleteSegment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/hosts")
    public List<HostResponse> listHosts(@PathVariable Long id) {
        return hostService.listHostsBySegment(id);
    }
}
