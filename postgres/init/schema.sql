CREATE DATABASE userprofile_db;
CREATE DATABASE event_db;

\c userprofile_db
CREATE USER userprofilemanager WITH ENCRYPTED PASSWORD 'postgres'; 
GRANT ALL PRIVILEGES ON DATABASE userprofile_db TO userprofilemanager;
REVOKE ALL PRIVILEGES ON SCHEMA public FROM userprofilemanager;
GRANT USAGE ON SCHEMA public TO userprofilemanager;
GRANT ALL PRIVILEGES ON SCHEMA public TO userprofilemanager;
    
\c event_db

CREATE USER eventmanager WITH ENCRYPTED PASSWORD 'postgres';
CREATE USER ticketmanager WITH ENCRYPTED PASSWORD 'postgres';

GRANT ALL PRIVILEGES ON DATABASE event_db TO eventmanager;

REVOKE ALL PRIVILEGES ON SCHEMA public FROM eventmanager;
GRANT USAGE ON SCHEMA public TO eventmanager;
GRANT ALL PRIVILEGES ON SCHEMA public TO eventmanager;


--     -- outbox table for all microservices
-- CREATE TABLE IF NOT EXISTS public.outbox (
--  id UUID NOT NULL,
--  topic varchar(255) NOT NULL,
-- payload varchar(4096) NOT NULL,
-- timestamp TIMESTAMP,
-- CONSTRAINT pkey PRIMARY KEY (id)
-- );
--
-- REVOKE ALL PRIVILEGES ON public.outbox FROM eventmanager, ticketmanager;
-- GRANT ALL PRIVILEGES  ON public.outbox TO eventmanager, ticketmanager;