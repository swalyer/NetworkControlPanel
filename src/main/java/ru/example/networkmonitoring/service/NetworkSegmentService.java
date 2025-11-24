package ru.example.networkmonitoring.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.networkmonitoring.dto.NetworkSegmentRequest;
import ru.example.networkmonitoring.dto.NetworkSegmentResponse;
import ru.example.networkmonitoring.exception.BadRequestException;
import ru.example.networkmonitoring.exception.NotFoundException;
import ru.example.networkmonitoring.model.NetworkSegment;
import ru.example.networkmonitoring.repository.HostRepository;
import ru.example.networkmonitoring.repository.NetworkSegmentRepository;

@Service
@Transactional
public class NetworkSegmentService {

    private final NetworkSegmentRepository networkSegmentRepository;
    private final HostRepository hostRepository;

    public NetworkSegmentService(NetworkSegmentRepository networkSegmentRepository, HostRepository hostRepository) {
        this.networkSegmentRepository = networkSegmentRepository;
        this.hostRepository = hostRepository;
    }

    public List<NetworkSegmentResponse> listSegments() {
        return networkSegmentRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public NetworkSegmentResponse getSegment(Long id) {
        NetworkSegment segment = findSegment(id);
        return toResponse(segment);
    }

    public NetworkSegmentResponse createSegment(NetworkSegmentRequest request) {
        if (networkSegmentRepository.findByName(request.getName()).isPresent()) {
            throw new BadRequestException("Network segment with name " + request.getName() + " already exists");
        }
        NetworkSegment segment = new NetworkSegment();
        segment.setName(request.getName());
        segment.setDescription(request.getDescription());
        segment.setNetworkCidr(request.getNetworkCidr());
        segment.setLocation(request.getLocation());
        NetworkSegment saved = networkSegmentRepository.save(segment);
        return toResponse(saved);
    }

    public void deleteSegment(Long id) {
        NetworkSegment segment = findSegment(id);
        if (!hostRepository.findByNetworkSegmentId(id).isEmpty()) {
            throw new BadRequestException("Cannot delete network segment with existing hosts");
        }
        networkSegmentRepository.delete(segment);
    }

    private NetworkSegment findSegment(Long id) {
        return networkSegmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Network segment with id " + id + " not found"));
    }

    private NetworkSegmentResponse toResponse(NetworkSegment segment) {
        NetworkSegmentResponse response = new NetworkSegmentResponse();
        response.setId(segment.getId());
        response.setName(segment.getName());
        response.setDescription(segment.getDescription());
        response.setNetworkCidr(segment.getNetworkCidr());
        response.setLocation(segment.getLocation());
        response.setHostsCount(segment.getHosts().size());
        return response;
    }
}
