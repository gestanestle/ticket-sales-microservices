CREATE KEYSPACE IF NOT EXISTS ec_ks
  WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};

USE ec_ks;

CREATE TABLE IF NOT EXISTS event_capacity (
    event_code text,
    seat text,
    max_capacity int,
    attendee_count counter,
    PRIMARY KEY (event_code, seat)
);