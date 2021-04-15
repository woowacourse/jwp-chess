package chess.dao;

import chess.domain.piece.Color;
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
        String sql = "insert into room (id, turn, playing_flag) values (?, 'WHITE', true)";
        jdbcTemplate.update(sql, roomId);
    }

    public Color findTurnByRoomId(int id) {
        String sql = "select turn from room where id=?";
        System.out.println("####id : " + id);
        return Color.valueOf(jdbcTemplate.queryForObject(sql, String.class, id));
    }

    public boolean findPlayingFlagByRoomId(int id) {
        String sql = "select playing_flag from room where id=?";

        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    public void updateRoom(int roomId, boolean isBlackTurn, boolean playing) {
        String sql = "update room set turn = ?, playing_flag = ? where id=?";

        Color color;
        if (isBlackTurn) {
            color = Color.BLACK;
            jdbcTemplate.update(sql, color.name(), playing, roomId);
            return;
        }
        color = Color.WHITE;
        jdbcTemplate.update(sql, color.name(), playing, roomId);
    }

    public void updatePiecesByRoomId(List<PiecesDto> piecesDtos) {
        deleteAllPiecesByRoomId(piecesDtos.get(0).getRoomId());
        for (PiecesDto piecesDto : piecesDtos) {
            insertPieceByRoomId(piecesDto);
        }
    }

    public void deleteAllPiecesByRoomId(int id) {
        String sql = "delete from pieces where room_id=?";
        jdbcTemplate.update(sql, id);
    }

    public void insertPieceByRoomId(PiecesDto piecesDto) {
        String sql = "insert into pieces (room_id, piece_name, position) values(?,?,?)";
        jdbcTemplate.update(sql, piecesDto.getRoomId(), piecesDto.getPieceName(), piecesDto.getPosition());
    }

    public void deleteRoomById(int id) {
        String sql = "delete from room where id=?";
        jdbcTemplate.update(sql, id);
    }
}
