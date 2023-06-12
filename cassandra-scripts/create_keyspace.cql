CREATE KEYSPACE IF NOT EXISTS event_capacity_ks
  WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '2'};

USE event_capacity_ks;