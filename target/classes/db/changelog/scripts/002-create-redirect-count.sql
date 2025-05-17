-- File: db/changelog/scripts/002-create-redirect-count.sql

CREATE TABLE redirect_count (
    url_mapping_id BIGINT PRIMARY KEY,
    count INT DEFAULT 0,
    CONSTRAINT fk_redirect_count_url FOREIGN KEY (url_mapping_id)
        REFERENCES url_mapping(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_redirect_count_url_mapping_id ON redirect_count(url_mapping_id);