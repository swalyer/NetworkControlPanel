package ru.example.networkmonitoring.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.example.networkmonitoring.model.Host;
import ru.example.networkmonitoring.model.HostCheckResult;
import ru.example.networkmonitoring.service.MonitoringService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/hosts")
public class HostController {

    private final MonitoringService monitoringService;

    public HostController(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @GetMapping
    public List<Host> getHosts() {
        return monitoringService.listHosts();
    }

    @PostMapping
    public ResponseEntity<Host> createHost(@Valid @RequestBody Host host) {
        Host saved = monitoringService.createHost(host);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping("/{id}")
    public Host getHost(@PathVariable Long id) {
        return monitoringService.getHost(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHost(@PathVariable Long id) {
        monitoringService.deleteHost(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/check")
    public HostCheckResult checkHost(@PathVariable Long id) {
        return monitoringService.checkHostAvailability(id);
    }
}
