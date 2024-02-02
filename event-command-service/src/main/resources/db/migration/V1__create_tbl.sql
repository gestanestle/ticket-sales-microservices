CREATE TABLE IF NOT EXISTS public.event (
        event_id BIGSERIAL PRIMARY KEY,
        name VARCHAR(255) UNIQUE ,
        banner VARCHAR(255),
        description VARCHAR(255),
        venue VARCHAR(255),
        start_datetime TIMESTAMP,
        end_datetime TIMESTAMP,
        organizer VARCHAR(255),
        tags VARCHAR(255) ARRAY,
        status VARCHAR(255),
        created_at TIMESTAMP
    );

ALTER TABLE public.event REPLICA IDENTITY FULL;