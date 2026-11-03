CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    created_at TIMESTAMP
);

INSERT INTO users (id, username, email, full_name, created_at)
VALUES
    (1, 'alice', 'alice@example.com', 'Alice Nguyen', CURRENT_TIMESTAMP),
    (2, 'bob', 'bob@example.com', 'Bob Tran', CURRENT_TIMESTAMP),
    (3, 'charlie', 'charlie@example.com', 'Charlie Le', CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;