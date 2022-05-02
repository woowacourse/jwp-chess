package chess.dao;

import chess.domain.board.Board;
import chess.domain.game.room.RoomId;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceColor;
import chess.domain.piece.PieceType;
import chess.domain.position.Position;
import chess.domain.position.XAxis;
import chess.domain.position.YAxis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcBoardDao implements BoardDao {
    private static final String TABLE_NAME = "board";

    private final JdbcTemplate jdbcTemplate;
    private final RoomDao roomDao;

    private final RowMapper<Position> positionMapper = (resultSet, rowNum) -> {
        XAxis xAxis = XAxis.getByValue(resultSet.getString("x_axis"));
        YAxis yAxis = YAxis.getByValue(resultSet.getString("y_axis"));
        return Position.of(xAxis, yAxis);
    };

    private final RowMapper<Piece> pieceRowMapper = (resultSet, rowNum) -> {
        PieceType pieceType = PieceType.valueOf(resultSet.getString("piece_type"));
        PieceColor pieceColor = PieceColor.valueOf(resultSet.getString("piece_color"));
        return new Piece(pieceType, pieceColor);
    };

    @Autowired
    public JdbcBoardDao(JdbcTemplate jdbcTemplate, RoomDao roomDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.roomDao = roomDao;
    }

    @Override
    public Board getBoard(RoomId roomId) {
        roomDao.validateRoomExisting(roomId);

        Map<Position, Piece> boardValue = new HashMap<>();

        String query = String.format(
                "SELECT piece_type, piece_color FROM %s WHERE room_id = ? AND x_axis = ? AND y_axis = ?",
                TABLE_NAME);

        for (Position position : getPositionsByRoomId(roomId)) {
            Piece piece = jdbcTemplate.queryForObject(query, pieceRowMapper, roomId.getValue(),
                    position.getXAxis().getValueAsString(), position.getYAxis().getValueAsString());
            boardValue.put(position, piece);
        }

        return Board.from(boardValue);
    }

    private List<Position> getPositionsByRoomId(RoomId roomId) {
        String query = String.format("SELECT x_axis, y_axis FROM %s WHERE room_id = ?", TABLE_NAME);
        return jdbcTemplate.query(query, positionMapper, roomId.getValue());
    }

    @Override
    public void createPiece(RoomId roomId, Position position, Piece piece) {
        String query = String.format(
                "INSERT INTO %s(room_id, x_axis, y_axis, piece_type, piece_color) VALUES(?, ?, ?, ?, ?)",
                TABLE_NAME);
        jdbcTemplate.update(query, roomId.getValue(), position.getXAxis().getValueAsString(),
                position.getYAxis().getValueAsString(), piece.getPieceType().name(), piece.getPieceColor().name());
    }

    @Override
    public void deletePiece(RoomId roomId, Position position) {
        String query = String.format(
                "DELETE FROM %s WHERE room_id = ? AND x_axis = ? AND y_axis = ?", TABLE_NAME);
        jdbcTemplate.update(query, roomId.getValue(), position.getXAxis().getValueAsString(),
                position.getYAxis().getValueAsString());
    }

    @Override
    public void updatePiecePosition(RoomId roomId, Position from, Position to) {
        String query = String.format(
                "UPDATE %s SET x_axis = ?, y_axis = ? WHERE x_axis = ? AND y_axis = ? AND room_id = ?",
                TABLE_NAME);
        jdbcTemplate.update(query, to.getXAxis().getValueAsString(), to.getYAxis().getValueAsString(),
                from.getXAxis().getValueAsString(), from.getYAxis().getValueAsString(), roomId.getValue());
    }
}
