package ru.example.networkmonitoring.repository;

import org.springframework.stereotype.Repository;
import ru.example.networkmonitoring.model.Host;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryHostRepository {

    private final ConcurrentMap<Long, Host> hosts = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public List<Host> findAll() {
        return new ArrayList<>(hosts.values());
    }

    public Optional<Host> findById(Long id) {
        return Optional.ofNullable(hosts.get(id));
    }

    public Host save(Host host) {
        if (host.getId() == null) {
            host.setId(idGenerator.incrementAndGet());
        }
        hosts.put(host.getId(), host);
        return host;
    }

    public void deleteById(Long id) {
        hosts.remove(id);
    }
}
