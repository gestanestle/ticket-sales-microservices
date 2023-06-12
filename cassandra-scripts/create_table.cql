CREATE TABLE IF NOT EXISTS event_capacity (
      seat text,
      event_code text,
      max_capacity int,
      attendee_count int,
      PRIMARY KEY (seat, event_code)
);