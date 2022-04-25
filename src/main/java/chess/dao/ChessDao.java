package chess.dao;

import chess.dto.BoardElementDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChessDao {
    public static final int GAME_ID = 0;

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<BoardElementDto> boardDtoRowMapper = (resultSet, rowNum) -> new BoardElementDto(
            resultSet.getString("piece_name"),
            resultSet.getString("piece_color"),
            resultSet.getString("position")
    );

    public ChessDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void updateTurn(int gameId, final String color) {
        final var sql = "UPDATE game SET current_turn=? WHERE game_id=?";
        jdbcTemplate.update(sql, color, gameId);
    }

    public void deleteAllPiece(int gameId) {
        final var sql = "DELETE FROM piece WHERE game_id = ?";
        jdbcTemplate.update(sql, gameId);
    }

    public void savePiece(int gameId, BoardElementDto boardElementDto) {
        final var sql = "insert into piece (game_id, piece_name, piece_color, position) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                gameId,
                boardElementDto.getPieceName(),
                boardElementDto.getPieceColor(),
                boardElementDto.getPosition());
    }

    public List<BoardElementDto> findBoard(int gameId) {
        final var sql = "SELECT * FROM piece WHERE game_id=?";
        return jdbcTemplate.query(sql, boardDtoRowMapper, gameId);
    }

    public String findCurrentColor(int gameId) {
        final var sql = "SELECT current_turn FROM game WHERE game_id=?";
        return jdbcTemplate.queryForObject(sql, String.class, gameId);
    }

    public void deletePiece(final String to) {
        final var sql = "DELETE FROM piece WHERE position = ?";
        jdbcTemplate.update(sql, to);
    }

    public void updatePiece(final int gameId, final String from, final String to) {
        final var sql = "UPDATE piece SET position = ? WHERE position = ? AND game_id = ?";
        jdbcTemplate.update(sql, to, from, gameId);
    }

    public void deleteGame(int gameId) {
        String sql = "delete from game where game_id = ?";
        jdbcTemplate.update(sql, gameId);
    }

    public void initGame(int gameId) {
        String sql = "INSERT INTO game (game_id) VALUES(?)";
        jdbcTemplate.update(sql, gameId);
    }
}
