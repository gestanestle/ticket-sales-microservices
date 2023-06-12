CREATE KEYSPACE IF NOT EXISTS event_capacity_ks
  WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '2'};

USE event_capacity_ks;

CREATE TABLE IF NOT EXISTS event_capacity (
      seat text,
      event_code text,
      max_capacity int,
      attendee_count int,
      PRIMARY KEY (seat, event_code)
);
