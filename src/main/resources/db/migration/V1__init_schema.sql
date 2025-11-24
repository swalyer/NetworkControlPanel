CREATE TABLE network_segment (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    network_cidr VARCHAR(255) NOT NULL,
    location VARCHAR(255)
);

CREATE TABLE host (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    ip_address VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    role VARCHAR(255) NOT NULL,
    criticality VARCHAR(50) NOT NULL,
    monitored BOOLEAN NOT NULL,
    last_status VARCHAR(50) NOT NULL,
    last_check_time TIMESTAMP WITH TIME ZONE,
    network_segment_id BIGINT NOT NULL REFERENCES network_segment(id)
);

CREATE INDEX idx_host_segment ON host(network_segment_id);
CREATE INDEX idx_host_monitored ON host(monitored);

CREATE TABLE host_check_result (
    id BIGSERIAL PRIMARY KEY,
    host_id BIGINT NOT NULL REFERENCES host(id),
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(50) NOT NULL,
    response_time_millis BIGINT,
    error_message TEXT
);

CREATE INDEX idx_check_result_host ON host_check_result(host_id);
CREATE INDEX idx_check_result_timestamp ON host_check_result(timestamp);
