-- File: db/changelog/scripts/001-create-url-mapping.sql
CREATE SEQUENCE url_mapping_seq START WITH 1000000000000 INCREMENT BY 1;

CREATE TABLE url_mapping (
    id BIGINT PRIMARY KEY DEFAULT nextval('url_mapping_seq'),
    original_url VARCHAR(2048) NOT NULL,
    short_code VARCHAR(16),
    created_at TIMESTAMP NOT NULL
);

CREATE UNIQUE INDEX idx_short_code ON url_mapping(short_code) WHERE short_code IS NOT NULL;