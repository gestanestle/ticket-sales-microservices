CREATE DATABASE event_ticket_db;


\c event_ticket_db

CREATE USER userevent WITH ENCRYPTED PASSWORD 'postgres';
CREATE USER userticket WITH ENCRYPTED PASSWORD 'postgres';

GRANT ALL PRIVILEGES ON DATABASE event_ticket_db TO userevent;
GRANT ALL PRIVILEGES ON DATABASE event_ticket_db TO userticket;

REVOKE ALL PRIVILEGES ON SCHEMA public FROM userevent, userticket;
GRANT USAGE ON SCHEMA public TO userevent, userticket;
GRANT ALL PRIVILEGES ON SCHEMA public TO userevent, userticket;
