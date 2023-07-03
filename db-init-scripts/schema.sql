CREATE DATABASE event_ticket_db;

CREATE USER userevent WITH ENCRYPTED PASSWORD 'postgres';
CREATE USER userticket WITH ENCRYPTED PASSWORD 'postgres';

\c event_ticket_db;

GRANT ALL PRIVILEGES ON DATABASE event_ticket_db TO userevent, userticket;

GRANT ALL PRIVILEGES ON SCHEMA public to userevent, userticket;

-- FOR EVENT APPLICATION

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

CREATE TABLE IF NOT EXISTS public.ticket_details (
    event_id BIGINT REFERENCES event(event_id) NOT NULL,
    section VARCHAR(255) NOT NULL,
    price INTEGER,
    total_stock INTEGER,
    total_sold INTEGER,
    PRIMARY KEY (event_id, section)
    );

ALTER TABLE public.event REPLICA IDENTITY FULL;

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_event_name ON public.event(event_name);

ALTER TABLE public.event OWNER TO userevent;
REVOKE ALL PRIVILEGES ON public.event, public.ticket_details FROM userevent;
GRANT ALL PRIVILEGES ON public.event TO userevent;

-- SHARED TABLE

GRANT USAGE ON SCHEMA public TO userevent, userticket;

GRANT SELECT, TRUNCATE, DELETE, REFERENCES ON public.ticket_details TO userevent;
GRANT INSERT, UPDATE (event_id, section, price, total_stock) ON public.ticket_details TO userevent;
GRANT SELECT ON public.ticket_details TO userticket;
GRANT UPDATE (total_sold) ON public.ticket_details TO userticket;


-- FOR TICKET APPLICATION

CREATE TABLE IF NOT EXISTS public.ticket (
      ticket_id BIGSERIAL PRIMARY KEY,
      event_id BIGSERIAL,
      section VARCHAR(255),
    ticket_code VARCHAR(255),
    purchased_by BIGINT,
    purchased_at TIMESTAMP
    );

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_event_id ON public.ticket(event_id);

ALTER TABLE public.ticket OWNER TO userticket;
REVOKE ALL PRIVILEGES ON public.ticket FROM userticket;
GRANT ALL PRIVILEGES ON public.ticket TO userticket;


