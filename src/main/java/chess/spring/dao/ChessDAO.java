package chess.spring.dao;

import chess.domain.history.History;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChessDAO {
    private static final RowMapper<History> ROW_MAPPER = (resultSet, rowNumber) -> {
        String source = resultSet.getString("source");
        String destination = resultSet.getString("destination");
        String teamType = resultSet.getString("team_type");
        return new History(source, destination, teamType);
    };

    private final JdbcTemplate jdbcTemplate;

    public ChessDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<History> findAllHistories() {
        String query = "SELECT * FROM HISTORY";
        return jdbcTemplate.query(query, ROW_MAPPER);
    }

    public void insert(String source, String destination, String teamType){
        String sql = "INSERT INTO HISTORY (SOURCE, DESTINATION, TEAM_TYPE) VALUES (?, ?, ?)";
        this.jdbcTemplate.update(sql,source, destination, teamType);
    }

    public void deleteAll() {
        String sql = "TRUNCATE TABLE HISTORY";
        this.jdbcTemplate.update(sql);
    }
}
