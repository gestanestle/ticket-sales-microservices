ALTER TABLE public.event OWNER TO userevent;

REVOKE ALL PRIVILEGES ON public.event, public.ticket_details FROM userevent;

GRANT ALL PRIVILEGES ON public.event TO userevent;

GRANT SELECT, TRUNCATE, DELETE, REFERENCES ON public.ticket_details TO userevent;
GRANT INSERT, UPDATE (event_id, section, price, total_stock) ON public.ticket_details TO userevent;

