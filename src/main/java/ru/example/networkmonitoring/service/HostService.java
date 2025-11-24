package ru.example.networkmonitoring.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.networkmonitoring.dto.HostRequest;
import ru.example.networkmonitoring.dto.HostResponse;
import ru.example.networkmonitoring.exception.BadRequestException;
import ru.example.networkmonitoring.exception.NotFoundException;
import ru.example.networkmonitoring.model.Host;
import ru.example.networkmonitoring.model.HostStatus;
import ru.example.networkmonitoring.model.NetworkSegment;
import ru.example.networkmonitoring.repository.HostRepository;
import ru.example.networkmonitoring.repository.NetworkSegmentRepository;

@Service
@Transactional
public class HostService {

    private final HostRepository hostRepository;
    private final NetworkSegmentRepository networkSegmentRepository;

    public HostService(HostRepository hostRepository, NetworkSegmentRepository networkSegmentRepository) {
        this.hostRepository = hostRepository;
        this.networkSegmentRepository = networkSegmentRepository;
    }

    public List<HostResponse> listHosts() {
        return hostRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<HostResponse> listHostsBySegment(Long segmentId) {
        return hostRepository.findByNetworkSegmentId(segmentId).stream()
                .map(this::toResponse)
                .toList();
    }

    public HostResponse getHost(Long id) {
        Host host = findHost(id);
        return toResponse(host);
    }

    public Host findHostEntity(Long id) {
        return findHost(id);
    }

    public HostResponse createHost(HostRequest request) {
        if (hostRepository.findByIpAddress(request.getIpAddress()).isPresent()) {
            throw new BadRequestException("Host with IP address " + request.getIpAddress() + " already exists");
        }
        NetworkSegment segment = findSegment(request.getNetworkSegmentId());
        Host host = new Host();
        host.setName(request.getName());
        host.setIpAddress(request.getIpAddress());
        host.setDescription(request.getDescription());
        host.setRole(request.getRole());
        host.setCriticality(request.getCriticality());
        host.setMonitored(Boolean.TRUE.equals(request.getMonitored()));
        host.setNetworkSegment(segment);
        host.setLastStatus(HostStatus.UNKNOWN);
        Host saved = hostRepository.save(host);
        return toResponse(saved);
    }

    public HostResponse updateHost(Long id, HostRequest request) {
        Host host = findHost(id);
        NetworkSegment segment = findSegment(request.getNetworkSegmentId());
        if (!host.getIpAddress().equals(request.getIpAddress())) {
            hostRepository.findByIpAddress(request.getIpAddress()).ifPresent(existing -> {
                throw new BadRequestException("Host with IP address " + request.getIpAddress() + " already exists");
            });
        }
        host.setName(request.getName());
        host.setIpAddress(request.getIpAddress());
        host.setDescription(request.getDescription());
        host.setRole(request.getRole());
        host.setCriticality(request.getCriticality());
        host.setMonitored(Boolean.TRUE.equals(request.getMonitored()));
        host.setNetworkSegment(segment);
        Host saved = hostRepository.save(host);
        return toResponse(saved);
    }

    public void deleteHost(Long id) {
        Host host = findHost(id);
        hostRepository.delete(host);
    }

    private Host findHost(Long id) {
        return hostRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Host with id " + id + " not found"));
    }

    private NetworkSegment findSegment(Long id) {
        return networkSegmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Network segment with id " + id + " not found"));
    }

    private HostResponse toResponse(Host host) {
        HostResponse response = new HostResponse();
        response.setId(host.getId());
        response.setName(host.getName());
        response.setIpAddress(host.getIpAddress());
        response.setDescription(host.getDescription());
        response.setRole(host.getRole());
        response.setCriticality(host.getCriticality());
        response.setMonitored(host.isMonitored());
        response.setLastStatus(host.getLastStatus().name());
        response.setLastCheckTime(host.getLastCheckTime());
        response.setNetworkSegmentId(host.getNetworkSegment().getId());
        response.setNetworkSegmentName(host.getNetworkSegment().getName());
        return response;
    }
}
