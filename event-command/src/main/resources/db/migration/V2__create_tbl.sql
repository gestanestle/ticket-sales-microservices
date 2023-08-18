CREATE TABLE IF NOT EXISTS public.event (
        event_id BIGSERIAL PRIMARY KEY,
        event_name VARCHAR(255) UNIQUE ,
        description VARCHAR(255),
        venue VARCHAR(255),
        date_time TIMESTAMP,
        created_by BIGINT,
        created_at TIMESTAMP,
        is_canceled BOOLEAN
);

ALTER TABLE public.event REPLICA IDENTITY FULL;
