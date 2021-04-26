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

    public void addMoveById(final Position from, final Position to, int id) {
        String query = "insert into move (start, end, room_id) values (?,?,?)";
        jdbcTemplate.update(query, from.toString(), to.toString(), id);
    }

    public List<MoveRequest> getMovesById(int id) {
        String query = "select * from move where (room_id) = (?)";
        return jdbcTemplate.query(query, mapper, id);
    }

    public void deleteAllById(int id) {
        String query = "delete from move where (room_id) = (?)";
        jdbcTemplate.update(query, id);
    }
}
