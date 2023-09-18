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

    @KafkaListener(topics = "event_ticket_db.public.event")
    public void indexDocuments(String eventAgg) {
        logger.debug("Event Message: " + eventAgg);

        BrokerMessage msg = null;

        try {
            msg = objectMapper.readValue(eventAgg, BrokerMessage.class);
        } catch (JsonProcessingException e) {
            logger.debug("UNABLE TO READ BROKER MESSAGE: \n" + e);
        }

        if (msg == null) return;

        if (msg.isDeleted()) {
            try {
                esClient.delete(new DeleteRequest.Builder().index(IDX).id(msg.id()).build());
            } catch (IOException e) {
                logger.debug("UNABLE TO DELETE RECORD: \n" + e);
            }
            return;
        }

        Event event = mapToEvent(msg);

        IndexRequest<Event> request = IndexRequest.of(i -> i
                .index(IDX)
                .id(String.valueOf(event.id()))
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
                msg.id(), msg.name(), msg.description(), msg.venue(),
                String.valueOf(msg.dateTime()), msg.createdBy(),
                String.valueOf(msg.createdAt()), msg.isCanceled()
        );
    }

}
