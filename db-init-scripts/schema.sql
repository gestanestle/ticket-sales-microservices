CREATE DATABASE event_ticket_db;


\c event_ticket_db

CREATE USER userevent WITH ENCRYPTED PASSWORD 'postgres';
CREATE USER userticket WITH ENCRYPTED PASSWORD 'postgres';

GRANT ALL PRIVILEGES ON DATABASE event_ticket_db TO userevent;
GRANT ALL PRIVILEGES ON DATABASE event_ticket_db TO userticket;

REVOKE ALL PRIVILEGES ON SCHEMA public FROM userevent, userticket;
GRANT USAGE ON SCHEMA public TO userevent, userticket;
GRANT ALL PRIVILEGES ON SCHEMA public TO userevent, userticket;


    -- outbox table for all microservices
CREATE TABLE IF NOT EXISTS public.outbox (
 id UUID NOT NULL,
 topic varchar(255) NOT NULL,
payload varchar(4096) NOT NULL,
timestamp TIMESTAMP,
CONSTRAINT pkey PRIMARY KEY (id)
);

REVOKE ALL PRIVILEGES ON public.outbox FROM userevent, userticket;
GRANT ALL PRIVILEGES  ON public.outbox TO userevent, userticket;