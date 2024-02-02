package com.krimo.event_query.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

import com.krimo.event_query.data.Event;

import com.krimo.event_query.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
public interface EventQueryService {

    List<Event> search(int pageNo, int pageSize, String q);
    Event getById(Long id);
    String getName(Long id);
}

@Service
class EventQueryServiceImpl implements EventQueryService {
    private final ElasticsearchClient esClient;
    private static final String IDX = "events";
    private static final String ES_ERR = "The server encountered an error while fetching data from Elasticsearch.";

    @Autowired
    public EventQueryServiceImpl(ElasticsearchClient esClient) {
        this.esClient = esClient;
    }

    @Override
    public List<Event> search(int pageNo, int pageSize, String query) {

        SearchResponse<Event> response;
        try {
            response = esClient.search(s -> s
                            .index(IDX)
                            .from(pageNo)
                            .size(pageSize)
                            .query(q -> q
                                    .multiMatch(t -> t
                                            .query(query)
                                            .fields("event_name", "description"))
                            ),
                    Event.class
            );
        } catch (IOException e) {
            throw new ApiRequestException(HttpStatus.SERVICE_UNAVAILABLE, ES_ERR);
        }
        List<Hit<Event>> hits = response.hits().hits();
        return hits.stream().map(Hit::source).toList();
    }

    @Override
    public Event getById(Long id) {

        GetResponse<Event> response;
        try {
            response = esClient.get(g -> g
                            .index(IDX)
                            .id(String.valueOf(id)),
                    Event.class
            );
        } catch (IOException e) {
            throw new ApiRequestException(HttpStatus.SERVICE_UNAVAILABLE, ES_ERR);
        }

        if (!response.found()) throw new ApiRequestException(HttpStatus.NOT_FOUND, "Record not found.");

        return response.source();

    }

    @Override
    public String getName(Long id) {
        return getById(id).name();
    }
}
