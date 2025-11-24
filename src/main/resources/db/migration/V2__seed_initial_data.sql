INSERT INTO network_segment (name, description, network_cidr, location)
VALUES ('Office LAN', 'Main office local network', '192.168.10.0/24', 'Main office, 3rd floor');

INSERT INTO network_segment (name, description, network_cidr, location)
VALUES ('Data Center', 'Primary data center', '10.0.0.0/24', 'Data center hall');

INSERT INTO host (name, ip_address, description, role, criticality, monitored, last_status, last_check_time, network_segment_id)
VALUES
    ('Gateway', '192.168.10.1', 'Office gateway', 'gateway', 'HIGH', true, 'UNKNOWN', NULL, (SELECT id FROM network_segment WHERE name = 'Office LAN')),
    ('File Server', '192.168.10.20', 'File server', 'fileserver', 'MEDIUM', true, 'UNKNOWN', NULL, (SELECT id FROM network_segment WHERE name = 'Office LAN')),
    ('Database Server', '10.0.0.10', 'Database server', 'db', 'HIGH', true, 'UNKNOWN', NULL, (SELECT id FROM network_segment WHERE name = 'Data Center'));
