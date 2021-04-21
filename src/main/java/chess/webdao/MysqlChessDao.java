package chess.webdao;

import chess.webdto.MoveRequestDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MysqlChessDao implements ChessDao {
    private final RowMapper<MoveRequestDto> movesMapper = (resultSet, rowNum) -> {
        MoveRequestDto moveRequestDto = new MoveRequestDto();
        moveRequestDto.setStart(resultSet.getString("start"));
        moveRequestDto.setDestination(resultSet.getString("destination"));
        return moveRequestDto;
    };

    private JdbcTemplate jdbcTemplate;

    public MysqlChessDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertMove(String start, String destination) {
        String sql = "INSERT INTO board (room_id, start, destination) VALUES (?, ?, ?)";
        return this.jdbcTemplate.update(sql, 1, start, destination);
    }

    @Override
    public List<MoveRequestDto> selectAllMovesByRoomId(int roomId) {
        final String sql = "SELECT start, destination FROM board WHERE room_id = (?)";
        return jdbcTemplate.query(sql, movesMapper, roomId);
    }

    @Override
    public void deleteMovesByRoomId(int roomId) {
        final String sql = "DELETE FROM board WHERE room_id = (?)";
        this.jdbcTemplate.update(sql, roomId);
    }

}
