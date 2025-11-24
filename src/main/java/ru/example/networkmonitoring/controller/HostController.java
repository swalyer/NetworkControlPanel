package ru.example.networkmonitoring.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.example.networkmonitoring.dto.HostCheckResultResponse;
import ru.example.networkmonitoring.dto.HostRequest;
import ru.example.networkmonitoring.dto.HostResponse;
import ru.example.networkmonitoring.service.HostService;
import ru.example.networkmonitoring.service.MonitoringService;

@RestController
@RequestMapping("/api/hosts")
public class HostController {

    private final HostService hostService;
    private final MonitoringService monitoringService;

    public HostController(HostService hostService, MonitoringService monitoringService) {
        this.hostService = hostService;
        this.monitoringService = monitoringService;
    }

    @GetMapping
    public List<HostResponse> listHosts() {
        return hostService.listHosts();
    }

    @GetMapping("/{id}")
    public HostResponse getHost(@PathVariable Long id) {
        return hostService.getHost(id);
    }

    @PostMapping
    public ResponseEntity<HostResponse> createHost(@Valid @RequestBody HostRequest request) {
        HostResponse response = hostService.createHost(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public HostResponse updateHost(@PathVariable Long id, @Valid @RequestBody HostRequest request) {
        return hostService.updateHost(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHost(@PathVariable Long id) {
        hostService.deleteHost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/segment/{segmentId}")
    public List<HostResponse> listHostsBySegment(@PathVariable Long segmentId) {
        return hostService.listHostsBySegment(segmentId);
    }

    @PostMapping("/{id}/check")
    public HostCheckResultResponse checkHost(@PathVariable Long id) {
        return monitoringService.checkHostAvailability(id);
    }

    @GetMapping("/{id}/history")
    public List<HostCheckResultResponse> hostHistory(@PathVariable Long id) {
        return monitoringService.getRecentResults(id);
    }
}
