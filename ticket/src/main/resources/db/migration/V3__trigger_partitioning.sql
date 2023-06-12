CREATE TRIGGER ticket_partition_per_event_trigger
BEFORE INSERT ON ticket
FOR EACH ROW EXECUTE PROCEDURE create_partition_table()
