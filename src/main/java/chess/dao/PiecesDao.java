package chess.dao;

import chess.dto.PiecesDto;
import chess.dto.RoomIdDto;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PiecesDao {

    private JdbcTemplate jdbcTemplate;

    public PiecesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PiecesDto> findPiecesByRoomId(int id) {
        String sql = "select * from pieces where room_id=?";

        return jdbcTemplate.query(
            sql,
            (resultSet, rowNum) -> {
                PiecesDto piecesDto = new PiecesDto(
                    resultSet.getInt("room_id"),
                    resultSet.getString("piece_name"),
                    resultSet.getString("position")
                );
                return piecesDto;
            }, id);
    }

    public List<RoomIdDto> findAllRoomId() {
        String sql = "select id from room";

        return jdbcTemplate.query(
            sql,
            (resultSet, rowNum) -> {
                RoomIdDto roomIdDto = new RoomIdDto(resultSet.getInt("id"));
                return roomIdDto;
            });
    }

    public void insertRoom(int roomId) {
        String sql = "insert into room (id, turn) values (?, 'WHITE')";
        jdbcTemplate.update(sql, roomId);
    }
}
