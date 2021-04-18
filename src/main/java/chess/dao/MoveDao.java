package chess.dao;

import chess.domain.position.Position;
import chess.dto.MoveRequest;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MoveDao {

    private final JdbcTemplate jdbcTemplate;
    private final MoveRequestMapper mapper;

    public MoveDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = new MoveRequestMapper();
    }

    public void addMove(final Position from, final Position to) {
        String query = "insert into move (start, end) values (?,?)";
        jdbcTemplate.update(query, from.toString(), to.toString());
    }

    public List<MoveRequest> getMoves() {
        String query = "select * from move";
        return jdbcTemplate.query(query, mapper);
    }

    public void deleteAll() {
        String query = "delete from move";
        jdbcTemplate.update(query);
    }
}
