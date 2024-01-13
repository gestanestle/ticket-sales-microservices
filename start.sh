#!/bin/bash

ELASTIC_PASSWORD="password"

# Wait for Elasticsearch to start
until curl -s -X GET "http://elastic:${ELASTIC_PASSWORD}@localhost:9200/_cluster/health" > /dev/null; do
    echo "Elasticsearch is not yet ready, waiting..."
    sleep 1
done

echo "Elasticsearch is ready! Running curl..."

# Create an index "events" for Event Query and Event Indexer
curl -X PUT "http://elastic:${ELASTIC_PASSWORD}@localhost:9200/events" -H "Content-Type: application/json"

# Create a role with only read access privileges
curl -X POST "http://elastic:${ELASTIC_PASSWORD}@localhost:9200/_security/role/event_reader?pretty" -H 'Content-Type: application/json' -d'
{
    "cluster": ["all"],
    "indices": [
        {
          "names": [ "events" ],
          "privileges": ["read"]
        }
    ]
}
'

# Create a role with only write access privileges
curl -X POST "http://elastic:${ELASTIC_PASSWORD}@localhost:9200/_security/role/event_writer?pretty" -H 'Content-Type: application/json' -d'
{
    "cluster": ["all"],
    "indices": [
        {
          "names": [ "events" ],
          "privileges": ["write"]
        }
    ]
}
'

# Create a user for Event Query Application with 'event_reader' role
curl -X POST "http://elastic:${ELASTIC_PASSWORD}@localhost:9200/_security/user/event_query?pretty" -H 'Content-Type: application/json' -d'
{
  "password" : "elastic",
  "roles" : [ "event_reader" ],
  "full_name" : "Event Query App"
}
'

# Create a user for Event Indexer Application with 'event_writer' role
curl -X POST "http://elastic:${ELASTIC_PASSWORD}@localhost:9200/_security/user/event_indexer?pretty" -H 'Content-Type: application/json' -d'
{
  "password" : "elastic",
  "roles" : [ "event_writer" ],
  "full_name" : "Event Indexer App"
}
'

# TO CONFIGURE DEBEZIUM:

# Wait until Debezium is ready
until curl -s "http://localhost:8083/connectors" > /dev/null; do
    echo "Debezium is not yet ready, waiting..."
    sleep 5  # Adjust the sleep interval as needed
done

# Once Debezium is ready, execute your curl command
echo "Debezium is ready! Running curl..."

# Register connector for event table
curl -X POST localhost:8083/connectors -H "Content-Type: application/json" -d @./connectors/cdc-event.json
# Register connector for the outbox table
curl -X POST localhost:8083/connectors -H "Content-Type: application/json" -d @./connectors/outbox-event.json
