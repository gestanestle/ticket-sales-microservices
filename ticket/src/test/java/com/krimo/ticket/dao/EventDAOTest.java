package com.krimo.ticket.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class EventDAOTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @InjectMocks
    @Autowired
    private EventDAOImpl eventDAO;

    @BeforeEach
    void setUp() {
        eventDAO = new EventDAOImpl(jdbcTemplate);
    }

    @Test
    void getEventName() {
        String expected = "EventName";

        doAnswer(invocationOnMock -> {

            ResultSet rs = mock(ResultSet.class);
            when(rs.getString("event_name")).thenReturn(expected);
            RowMapper<String> mapper = invocationOnMock.getArgument(2);
            return Collections.singletonList(mapper.mapRow(rs, 0));
        }).when(jdbcTemplate).query(any(String.class), any(MapSqlParameterSource.class), any(RowMapper.class)
        );

        Optional<String> actual = eventDAO.getEventName(1L);
        assertTrue(actual.isPresent());
        assertThat(actual.get()).isEqualTo(expected);
    }


    @Test
    void isCanceled() {

        when(jdbcTemplate.queryForObject(
                anyString(), any(MapSqlParameterSource.class), Mockito.<Class>any()
        )).thenAnswer(x -> true);
        assertThat(eventDAO.isCanceled(1L)).isTrue();
    }

}
