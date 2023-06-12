-- Define the function to generate the partition table name dynamically
CREATE OR REPLACE FUNCTION get_partition_table_name(p_event_code VARCHAR)
RETURNS VARCHAR AS $$
BEGIN
RETURN CONCAT('ticket_', p_event_code);
END;
$$ LANGUAGE plpgsql;

-- Define the function to create a new partition table dynamically
CREATE OR REPLACE FUNCTION create_partition_table()
RETURNS TRIGGER AS $$
DECLARE
partition_table_name VARCHAR := get_partition_table_name(NEW.event_code);
BEGIN
  -- Check if the partition table already exists
IF NOT EXISTS (
    SELECT 1
    FROM information_schema.tables
    WHERE table_name = partition_table_name
    )
THEN
    -- If doesn't exist, create partition table
    EXECUTE FORMAT('CREATE TABLE %I (CHECK (event_code = %L)) INHERITS (ticket);', partition_table_name, NEW.event_code);
END IF;
    -- Insert values into the partition table
    EXECUTE FORMAT('INSERT INTO %I SELECT ($1).*', partition_table_name) USING NEW;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

