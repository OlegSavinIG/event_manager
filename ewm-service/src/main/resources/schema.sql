-- Schema for UserEntity
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

-- Schema for CategoryEntity
CREATE TABLE IF NOT EXISTS categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Schema for EventEntity
CREATE TABLE IF NOT EXISTS events (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    annotation TEXT,
    description TEXT,
    created_on TIMESTAMP NOT NULL,
    event_date TIMESTAMP NOT NULL,
    published_on TIMESTAMP,
    paid BOOLEAN NOT NULL,
    views INT NOT NULL,
    confirmed_requests INT NOT NULL,
    participant_limit INT NOT NULL,
    request_moderation BOOLEAN,
    state VARCHAR(50),
    category_id INT,
    user_id INT,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Schema for UserEventRequestEntity
CREATE TABLE IF NOT EXISTS requests (
    id SERIAL PRIMARY KEY,
    created TIMESTAMP NOT NULL,
    event_id INT NOT NULL,
    user_id INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (event_id) REFERENCES events(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Schema for CompilationEntity
CREATE TABLE IF NOT EXISTS compilations (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    pinned BOOLEAN NOT NULL
);

-- Join table for CompilationEntity and EventEntity many-to-many relationship
CREATE TABLE IF NOT EXISTS compilation_events (
    compilation_id INT NOT NULL,
    event_id INT NOT NULL,
    PRIMARY KEY (compilation_id, event_id),
    FOREIGN KEY (compilation_id) REFERENCES compilations(id),
    FOREIGN KEY (event_id) REFERENCES events(id)
);
