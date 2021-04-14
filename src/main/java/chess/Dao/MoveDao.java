package chess.Dao;

import chess.Dto.MoveRequest;
import chess.domain.position.Position;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MoveDao {

    private final JdbcTemplate jdbcTemplate;

    public MoveDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addMove(final Position from, final Position to) {
        String query = "insert into move (start, end) values (?,?)";
        jdbcTemplate.update(query, from.toString(), to.toString());
    }

    public List<MoveRequest> getMoves() {
        String query = "select * from move";
        return jdbcTemplate.query(query, ROW_MAPPER);
    }

    private final RowMapper<MoveRequest> ROW_MAPPER = (resultSet, rowNumber) -> {
        String start = resultSet.getString("start");
        String end = resultSet.getString("end");
        return new MoveRequest(start, end);
    };

    public void deleteAll() {
        String query = "delete from move";
        jdbcTemplate.update(query);
    }
}
