CREATE DATABASE auth_db;
CREATE DATABASE account_db;
CREATE DATABASE event_db;
CREATE DATABASE ticket_db;

\c auth_db
CREATE USER keycloak WITH ENCRYPTED PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE auth_db TO keycloak;
REVOKE ALL PRIVILEGES ON SCHEMA public FROM keycloak;
GRANT USAGE ON SCHEMA public TO keycloak;
GRANT ALL PRIVILEGES ON SCHEMA public TO keycloak;

\c account_db
CREATE USER accountmanager WITH ENCRYPTED PASSWORD 'postgres'; 
GRANT ALL PRIVILEGES ON DATABASE account_db TO accountmanager;
REVOKE ALL PRIVILEGES ON SCHEMA public FROM accountmanager;
GRANT USAGE ON SCHEMA public TO accountmanager;
GRANT ALL PRIVILEGES ON SCHEMA public TO accountmanager;
    
\c event_db

CREATE USER eventmanager WITH ENCRYPTED PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE event_db TO eventmanager;
REVOKE ALL PRIVILEGES ON SCHEMA public FROM eventmanager;
GRANT USAGE ON SCHEMA public TO eventmanager;
GRANT ALL PRIVILEGES ON SCHEMA public TO eventmanager;

\c ticket_db

CREATE USER ticketmanager WITH ENCRYPTED PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE ticket_db TO ticketmanager;
REVOKE ALL PRIVILEGES ON SCHEMA public FROM ticketmanager;
GRANT USAGE ON SCHEMA public TO ticketmanager;
GRANT ALL PRIVILEGES ON SCHEMA public TO ticketmanager;
