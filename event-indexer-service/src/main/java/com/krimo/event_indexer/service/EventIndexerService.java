package com.krimo.event_indexer.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import com.krimo.event_indexer.payload.BrokerMessage;
import com.krimo.event_indexer.payload.Event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EventIndexerService {
    Logger logger = LoggerFactory.getLogger(EventIndexerService.class);
    private final ObjectMapper objectMapper;
    private final ElasticsearchClient esClient;
    private static final String IDX = "events";

    @Autowired
    public EventIndexerService(ElasticsearchClient esClient, ObjectMapper objectMapper) {
        this.esClient = esClient;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "event_db.public.event")
    public void indexDocuments(String eventAgg) throws JsonProcessingException {
        logger.info("Event Message: " + eventAgg);

        BrokerMessage msg = objectMapper.readValue(eventAgg, BrokerMessage.class);

        if (msg == null) return;

        if (msg.isDeleted()) {
            try {
                esClient.delete(new DeleteRequest.Builder().index(IDX).id(String.valueOf(msg.event_id())).build());
            } catch (IOException e) {
                logger.debug("UNABLE TO DELETE RECORD: \n" + e);
            }
            return;
        }

        Event event = mapToEvent(msg);

        IndexRequest<Event> request = IndexRequest.of(i -> i
                .index(IDX)
                .id(String.valueOf(event.event_id()))
                .document(event)
        );

        try {
            esClient.index(request);
        } catch (IOException e) {
            logger.debug("UNABLE TO INDEX RECORD: \n" + e);
        }

    }

    private Event mapToEvent(BrokerMessage msg) {
        return new Event(
                msg.event_id(), msg.name(), msg.banner(), msg.description(), msg.venue(),
                String.valueOf(msg.startDateTime()), String.valueOf(msg.endDateTime()),
                msg.organizer(), msg.tags(), msg.status()
        );
    }

}
