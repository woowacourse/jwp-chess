package chess.dao;

import chess.dto.PieceAndPositionDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChessDao {

    public static final int DEFAULT_GAME_ID = 0;

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<PieceAndPositionDto> pieceAndPositionDtoRowMapper = (resultSet, rowNum) -> new PieceAndPositionDto(
            resultSet.getString("piece_name"),
            resultSet.getString("piece_color"),
            resultSet.getString("position")
    );

    public ChessDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void updateTurn(final String color, final int gameId) {
        final var sql = "UPDATE game SET current_turn=? WHERE game_id=?";
        jdbcTemplate.update(sql, color, gameId);
    }

    public void deleteAllPiece(final int gameId) {
        final var sql = "DELETE FROM piece WHERE game_id = ?";
        jdbcTemplate.update(sql, gameId);
    }

    public void savePiece(final int gameId, final PieceAndPositionDto pieceAndPositionDto) {
        final var sql = "INSERT INTO piece (game_id, piece_name, piece_color, position) VALUES(?,?,?,?)";
        jdbcTemplate.update(sql, gameId, pieceAndPositionDto.getPieceName(), pieceAndPositionDto.getPieceColor(), pieceAndPositionDto.getPosition());
    }

    public List<PieceAndPositionDto> findBoard(final int gameId) {
        final var sql = "SELECT * FROM piece WHERE game_id=?";
        return jdbcTemplate.query(sql, pieceAndPositionDtoRowMapper, gameId);
    }

    public String findCurrentColor(final int gameId) {
        final var sql = "SELECT current_turn FROM game WHERE game_id=?";
        return jdbcTemplate.queryForObject(sql, String.class, gameId);
    }

    public void deletePiece(final String to) {
        final var sql = "DELETE FROM piece WHERE position = ?";
        jdbcTemplate.update(sql, to);
    }

    public void updatePiece(final String from, final String to, final int gameId) {
        final var sql = "UPDATE piece SET position = ? WHERE position = ? AND game_id = ?";
        jdbcTemplate.update(sql, to, from, gameId);
    }

    public void deleteGame(final int gameId) {
        String sql = "DELETE FROM game WHERE game_id = ?";
        jdbcTemplate.update(sql, gameId);
    }

    public void initGame(final int gameId) {
        String sql = "INSERT INTO game (game_id) VALUES(?)";
        jdbcTemplate.update(sql, gameId);
    }
}
