package com.krimo.ticket.repository;

import com.krimo.ticket.data.Ticket;
import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TicketRepository{

    void saveTicket(Ticket ticket);
    List<String> getAllEmails(String eventCode);
}

@Transactional(
     rollbackFor = SQLException.class
)
@Repository
@RequiredArgsConstructor
class TicketRepositoryImpl implements TicketRepository{

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void saveTicket(Ticket ticket) {

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("event_code", ticket.getEventCode())
                .addValue("ticket_code", ticket.getTicketCode())
                .addValue("section", ticket.getSection().name())
                .addValue("purchaser_email", ticket.getPurchaserEmail())
                .addValue("created_at", ticket.getCreatedAt());

        String query = "INSERT INTO TICKET (event_code, ticket_code, section, purchaser_email, created_at) " +
                "VALUES (:event_code, :ticket_code, :section, :purchaser_email, :created_at)";

        jdbcTemplate.update(query, namedParameters);
    }

    @Override
    public List<String> getAllEmails(String eventCode) {
        String sql = "SELECT purchaser_email FROM ticket WHERE event_code = :eventCode";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("eventCode", eventCode);

        return jdbcTemplate.queryForList(sql, paramMap, String.class).stream().toList();


    }
}