CREATE TABLE IF NOT EXISTS public.ticket_details (
             event_id BIGINT NOT NULL,
             section VARCHAR(255) NOT NULL,
             price INTEGER,
             total_stock INTEGER,
             PRIMARY KEY (event_id, section)
);

CREATE TABLE IF NOT EXISTS public.ticket (
             ticket_id BIGSERIAL PRIMARY KEY,
             event_id BIGINT,
             section VARCHAR(255),
             ticket_code VARCHAR(255),
             purchased_by BIGINT,
             purchased_at TIMESTAMP,
             FOREIGN KEY (event_id, section) REFERENCES ticket_details(event_id, section)
);
