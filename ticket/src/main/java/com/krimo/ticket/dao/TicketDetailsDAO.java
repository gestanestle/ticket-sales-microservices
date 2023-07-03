package com.krimo.ticket.dao;

import com.krimo.ticket.dto.TicketDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TicketDetailsDAO {

    boolean isSoldOut(Long eventId, String section);
    void incrementTotalSold(Long eventId, String section);
    void decrementTotalSold(Long eventId, String section);
}

@Repository
@Transactional
@RequiredArgsConstructor
class TicketDetailsDAOImpl implements TicketDetailsDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public boolean isSoldOut(Long eventId, String section) {
        TicketDetails ticketDetails = getTicketDetails(eventId, section).orElseThrow();
        return ticketDetails.totalSold() >= ticketDetails.totalStock();
    }

    @Override
    public void incrementTotalSold(Long eventId, String section) {
        String sql =
                "UPDATE public.ticket_details " +
                "SET total_sold = COALESCE(total_sold, 0) + 1 " +
                "WHERE event_id = :eventId AND section = :section";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("eventId", eventId)
                .addValue("section", section);

        jdbcTemplate.update(sql, parameters);
    }

    @Override
    public void decrementTotalSold(Long eventId, String section) {
        String sql =
                "UPDATE public.ticket_details " +
                "SET total_sold = total_sold - 1 " +
                "WHERE event_id = :eventId AND section = :section";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("eventId", eventId)
                .addValue("section", section);

        jdbcTemplate.update(sql, parameters);
    }


    private Optional<TicketDetails> getTicketDetails(Long eventId, String section) {
        String sql =
                "SELECT total_stock, total_sold FROM public.ticket_details " +
                "WHERE event_id = :eventId AND section = :section";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("eventId", eventId)
                .addValue("section", section);

        List<TicketDetails> result = jdbcTemplate.query(sql, parameters, (rs, rowNum) ->
                new TicketDetails(rs.getInt("total_stock"), rs.getInt("total_sold")));

        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }


}
