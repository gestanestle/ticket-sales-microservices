package com.krimo.ticket.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface EventDAO {

    Optional<String> getEventName(Long id);
    Boolean isCanceled(Long id);
}

@Repository
@Transactional
@RequiredArgsConstructor
class EventDAOImpl implements EventDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    MapSqlParameterSource parameters = new MapSqlParameterSource();

    @Override
    public Optional<String> getEventName(Long id) {

        String sql = "SELECT event_name FROM public.event WHERE event_id = :id";

        parameters.addValue("id", id);

        List<String> result = jdbcTemplate.query(sql, parameters, (rs, rowNum) -> rs.getString("event_name"));

        return Optional.ofNullable(result.get(0));
    }

    @Override
    public Boolean isCanceled(Long id) {

        String sql = "SELECT is_canceled FROM public.event WHERE event_id = :id";

        parameters.addValue("id", id);

        return jdbcTemplate.queryForObject(sql, parameters, Boolean.class);

    }
}