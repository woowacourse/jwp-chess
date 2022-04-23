package chess.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceColor;
import chess.domain.piece.PieceType;
import chess.domain.position.Position;
import chess.domain.position.XAxis;
import chess.domain.position.YAxis;
import chess.dto.request.CreatePieceDto;
import chess.dto.request.DeletePieceDto;
import chess.dto.request.UpdatePiecePositionDto;
import chess.dto.response.BoardDto;

@Repository
public class BoardDaoImpl implements BoardDao {
    private static final String TABLE_NAME = "board";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BoardDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BoardDto getBoard(String gameId) {
        Map<Position, Piece> boardValue = new HashMap<>();

        for (Position position : getPositionsByGameId(gameId)) {
            String query = String.format(
                "SELECT piece_type, piece_color FROM %s WHERE game_id = ? AND x_axis = ? AND y_axis = ?", TABLE_NAME);
            Piece piece = jdbcTemplate.queryForObject(query, (resultSet, rowNum) -> {
                PieceType pieceType = PieceType.valueOf(resultSet.getString("piece_type"));
                PieceColor pieceColor = PieceColor.valueOf(resultSet.getString("piece_color"));
                return new Piece(pieceType, pieceColor);
            }, gameId, position.getXAxis().getValueAsString(), position.getYAxis().getValueAsString());

            boardValue.put(position, piece);
        }

        System.out.println(boardValue);
        return BoardDto.from(boardValue);
    }

    private List<Position> getPositionsByGameId(String gameId) {
        String query = String.format("SELECT x_axis, y_axis FROM %s WHERE game_id = ?",
            TABLE_NAME);
        List<Position> positions = jdbcTemplate.query(query, (resultSet, rowNum) -> {
            XAxis xAxis = XAxis.getByValue(resultSet.getString("x_axis"));
            YAxis yAxis = YAxis.getByValue(resultSet.getString("y_axis"));
            Position position = Position.of(xAxis, yAxis);
            return position;
        }, gameId);
        return positions;
    }

    @Override
    public void createPiece(CreatePieceDto createPieceDto) {
        String query = String.format(
            "INSERT INTO %s(game_id, x_axis, y_axis, piece_type, piece_color) VALUES(?, ?, ?, ?, ?)",
            TABLE_NAME);
        jdbcTemplate.update(query, createPieceDto.getGameId(), createPieceDto.getXAxisValueAsString(),
            createPieceDto.getYAxisValueAsString(), createPieceDto.getPieceTypeName(),
            createPieceDto.getPieceColorName());
    }

    @Override
    public void deletePiece(DeletePieceDto deletePieceDto) {
        String query = String.format(
            "DELETE FROM %s WHERE game_id = ? AND x_axis = ? AND y_axis = ?", TABLE_NAME);
        jdbcTemplate.update(query, deletePieceDto.getGameId(), deletePieceDto.getXAxisValueAsString(),
            deletePieceDto.getYAxisValueAsString());
    }

    @Override
    public void updatePiecePosition(UpdatePiecePositionDto updatePiecePositionDto) {
        String query = String.format(
            "UPDATE %s SET x_axis = ?, y_axis = ? WHERE x_axis = ? AND y_axis = ? AND game_id = ?", TABLE_NAME);
        jdbcTemplate.update(query, updatePiecePositionDto.getToXAxisValueAsString(),
            updatePiecePositionDto.getToYAxisValueAsString(), updatePiecePositionDto.getFromXAxisValueAsString(),
            updatePiecePositionDto.getFromYAxisValueAsString(), updatePiecePositionDto.getGameId());
    }
}
