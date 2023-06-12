CREATE TABLE ticket (
        id SERIAL PRIMARY KEY,
        event_code VARCHAR(255) NOT NULL,
        ticket_code VARCHAR(255) NOT NULL UNIQUE,
        section VARCHAR(255) NOT NULL,
        purchaser_email VARCHAR(255),
        created_at TIMESTAMP
);

CREATE INDEX idx_ticket_code ON ticket (ticket_code);
