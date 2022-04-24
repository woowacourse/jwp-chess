package chess.dao;

import chess.domain.board.Board;
import chess.domain.game.GameId;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceColor;
import chess.domain.piece.PieceType;
import chess.domain.position.Position;
import chess.domain.position.XAxis;
import chess.domain.position.YAxis;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDaoImpl implements BoardDao {
    private static final String TABLE_NAME = "board";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BoardDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Board getBoard(GameId gameId) {
        Map<Position, Piece> boardValue = new HashMap<>();

        for (Position position : getPositionsByGameId(gameId)) {
            String query = String.format(
                    "SELECT piece_type, piece_color FROM %s WHERE game_id = ? AND x_axis = ? AND y_axis = ?",
                    TABLE_NAME);
            Piece piece = jdbcTemplate.queryForObject(query, (resultSet, rowNum) -> {
                PieceType pieceType = PieceType.valueOf(resultSet.getString("piece_type"));
                PieceColor pieceColor = PieceColor.valueOf(resultSet.getString("piece_color"));
                return new Piece(pieceType, pieceColor);
            }, gameId.getGameId(), position.getXAxis().getValueAsString(), position.getYAxis().getValueAsString());

            boardValue.put(position, piece);
        }

        return Board.from(boardValue);
    }

    private List<Position> getPositionsByGameId(GameId gameId) {
        String query = String.format("SELECT x_axis, y_axis FROM %s WHERE game_id = ?",
                TABLE_NAME);
        List<Position> positions = jdbcTemplate.query(query, (resultSet, rowNum) -> {
            XAxis xAxis = XAxis.getByValue(resultSet.getString("x_axis"));
            YAxis yAxis = YAxis.getByValue(resultSet.getString("y_axis"));
            Position position = Position.of(xAxis, yAxis);
            return position;
        }, gameId.getGameId());
        return positions;
    }

    @Override
    public void createPiece(GameId gameId, Position position, Piece piece) {
        String query = String.format(
                "INSERT INTO %s(game_id, x_axis, y_axis, piece_type, piece_color) VALUES(?, ?, ?, ?, ?)",
                TABLE_NAME);
        jdbcTemplate.update(query, gameId.getGameId(), position.getXAxis().getValueAsString(),
                position.getYAxis().getValueAsString(), piece.getPieceType().name(), piece.getPieceColor().name());
    }

    @Override
    public void deletePiece(GameId gameid, Position position) {
        String query = String.format(
                "DELETE FROM %s WHERE game_id = ? AND x_axis = ? AND y_axis = ?", TABLE_NAME);
        jdbcTemplate.update(query, gameid.getGameId(), position.getXAxis().getValueAsString(),
                position.getYAxis().getValueAsString());
    }

    @Override
    public void updatePiecePosition(GameId gameId, Position from, Position to) {
        String query = String.format(
                "UPDATE %s SET x_axis = ?, y_axis = ? WHERE x_axis = ? AND y_axis = ? AND game_id = ?", TABLE_NAME);
        jdbcTemplate.update(query, to.getXAxis().getValueAsString(), to.getYAxis().getValueAsString(),
                from.getXAxis().getValueAsString(), from.getYAxis().getValueAsString(), gameId.getGameId());
    }
}
