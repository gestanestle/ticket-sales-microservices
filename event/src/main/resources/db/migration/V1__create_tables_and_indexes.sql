CREATE TABLE IF NOT EXISTS public.event (
    event_id BIGSERIAL PRIMARY KEY,
    event_name VARCHAR(255) UNIQUE ,
    description VARCHAR(255),
    venue VARCHAR(255),
    date_time VARCHAR(255),
    created_by VARCHAR(255),
    created_at TIMESTAMP,
    is_canceled BOOLEAN
);

CREATE TABLE IF NOT EXISTS public.ticket_details (
    event_id BIGINT REFERENCES event(event_id) NOT NULL,
    section VARCHAR(255) NOT NULL,
    price INTEGER,
    total_stock INTEGER,
    remaining_stock INTEGER,
    PRIMARY KEY (event_id, section)
);

ALTER TABLE public.event REPLICA IDENTITY FULL;




