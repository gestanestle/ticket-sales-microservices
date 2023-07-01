CREATE DATABASE event_ticket_db;

CREATE USER userevent WITH ENCRYPTED PASSWORD 'postgres';
CREATE USER userticket WITH ENCRYPTED PASSWORD 'postgres';

\c event_ticket_db;

GRANT ALL PRIVILEGES ON DATABASE event_ticket_db TO userevent, userticket;

GRANT ALL PRIVILEGES ON SCHEMA public to userevent, userticket;


