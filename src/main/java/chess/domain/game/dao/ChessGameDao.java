package chess.domain.game.dao;

import chess.domain.game.ChessGame;
import chess.domain.game.dto.ChessGameDto;
import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ChessGameDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ChessGameDto> chessGameDtoRowMapper = (rs, rowNum) -> new ChessGameDto(
            rs.getString("id"),
            rs.getString("name"));

    public ChessGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long addGame(ChessGame chessGame) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final String sql = "INSERT INTO chess_game (name) VALUES (?)";
        jdbcTemplate.update(con -> {
            PreparedStatement pstmt = con.prepareStatement(sql, new String[]{"id"});
            pstmt.setString(1, chessGame.getName());
            return pstmt;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<ChessGameDto> findActiveGames() {
        String sql = "SELECT * FROM chess_game WHERE is_end = false ORDER BY created_at";
        return jdbcTemplate.query(sql, chessGameDtoRowMapper);
    }

    public void updateGameEnd(String gameId) {
        String sql = "UPDATE chess_game SET is_end = true WHERE id = ?";
        jdbcTemplate.update(sql, gameId);
    }

    public ChessGameDto findGameById(String gameId) {
        String sql = "SELECT id, name FROM chess_game WHERE id = ? AND is_end = false ORDER BY created_at";
        return jdbcTemplate.queryForObject(sql, chessGameDtoRowMapper, gameId);
    }
}
