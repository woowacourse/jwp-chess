package chess.dao;

import chess.domain.piece.Color;
import chess.dto.PieceDto;
import chess.dto.PiecesDto;
import chess.dto.RoomIdDto;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PiecesDao {

    private final JdbcTemplate jdbcTemplate;

    public PiecesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PieceDto> findPiecesByRoomId(RoomIdDto roomIdDto) {
        String sql = "select * from pieces where room_id=?";

        return jdbcTemplate.query(
            sql,
            (resultSet, rowNum) -> new PieceDto(
                resultSet.getInt("room_id"),
                resultSet.getString("piece_name"),
                resultSet.getString("position")
            ), roomIdDto.getId());
    }

    public List<RoomIdDto> findAllRoomId() {
        String sql = "select id from room";

        return jdbcTemplate.query(sql,
            (resultSet, rowNum) -> new RoomIdDto(resultSet.getInt("id")));
    }

    public void insertRoom(RoomIdDto roomIdDto) {
        String sql = "insert into room (id, turn, playing_flag) values (?, 'WHITE', true)";
        jdbcTemplate.update(sql, roomIdDto.getId());
    }

    public String findTurnByRoomId(RoomIdDto roomIdDto) {
        String sql = "select turn from room where id=?";
        return jdbcTemplate.queryForObject(sql, String.class, roomIdDto.getId());
    }

    public boolean findPlayingFlagByRoomId(RoomIdDto roomIdDto) {
        String sql = "select playing_flag from room where id=?";
        return jdbcTemplate.queryForObject(sql, Boolean.class, roomIdDto.getId());
    }

    public void updateRoom(int roomId, boolean isBlackTurn, boolean playing) {
        String sql = "update room set turn = ?, playing_flag = ? where id=?";

        if (isBlackTurn) {
            jdbcTemplate.update(sql, Color.BLACK.name(), playing, roomId);
            return;
        }

        jdbcTemplate.update(sql, Color.WHITE.name(), playing, roomId);
    }

    public void updatePiecesByRoomId(PiecesDto piecesDto) {
        deleteAllPiecesByRoomId(new RoomIdDto(piecesDto.getRoomId()));
        for (PieceDto pieceDto : piecesDto.getPieceDtos()) {
            insertPieceByRoomId(pieceDto);
        }
    }

    public void deleteAllPiecesByRoomId(RoomIdDto roomIdDto) {
        String sql = "delete from pieces where room_id=?";
        jdbcTemplate.update(sql, roomIdDto.getId());
    }

    public void insertPieceByRoomId(PieceDto pieceDto) {
        String sql = "insert into pieces (room_id, piece_name, position) values(?,?,?)";
        jdbcTemplate
            .update(sql, pieceDto.getRoomId(), pieceDto.getPieceName(), pieceDto.getPosition());
    }

    public void deleteRoomById(RoomIdDto roomIdDto) {
        String sql = "delete from room where id=?";
        jdbcTemplate.update(sql, roomIdDto.getId());
    }
}
