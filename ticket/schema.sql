CREATE DATABASE ticket_db;

\c ticket_db
CREATE USER ticketmanager WITH ENCRYPTED PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE ticket_db TO ticketmanager;

CREATE TABLE IF NOT EXISTS public.event (
    event_id BIGINT PRIMARY KEY,
    is_active BOOLEAN,
)

CREATE TABLE IF NOT EXISTS public.ticket (
    ticket_id BIGSERIAL PRIMARY KEY,
    event_id BIGINT REFERENCES event(event_id),
    ticket_type VARCHAR(255),
    price FLOAT,
    qty_stock INTEGER,
    qty_sold INTEGER
)

CREATE TABLE IF NOT EXISTS public.purchase (
    purchase_id BIGSERIAL PRIMARY KEY,
    ticket_code BIGINT,
    ticket_type VARCHAR(255),
    ticket_price FLOAT,
    quantity INTEGER,
    purchase_status VARCHAR(255),
    purchased_by BIGINT,
    purchased_at TIMESTAMP,
    FOREIGN KEY (ticket_type, ticket_price) REFERENCES ticket(ticket_type, price)
);