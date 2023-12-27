CREATE TABLE IF NOT EXISTS public.event (
    event_id BIGINT PRIMARY KEY,
    is_active BOOLEAN
);

CREATE TABLE IF NOT EXISTS public.ticket (
    ticket_id BIGSERIAL PRIMARY KEY,
    event_id BIGINT REFERENCES event(event_id),
    type VARCHAR(255),
    price FLOAT,
    qty_stock INTEGER,
    qty_sold INTEGER
);

CREATE TABLE IF NOT EXISTS public.purchase (
    purchase_id BIGSERIAL PRIMARY KEY,
    ticket_code VARCHAR(10),
    ticket_id BIGINT REFERENCES ticket(ticket_id),
    status VARCHAR(255),
    customer_id BIGINT,
    created_at TIMESTAMP
);