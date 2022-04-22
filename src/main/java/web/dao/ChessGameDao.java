package web.dao;

import chess.Score;
import chess.piece.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import web.dto.ChessGameDto;
import web.dto.GameStatus;

@Repository
public class ChessGameDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ChessGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ChessGameDto> findAll() {
        return jdbcTemplate.query(
                "SELECT id, name, status, current_color, black_score, white_score FROM chess_game",
                this::createChessGameDto);
    }

    private ChessGameDto createChessGameDto(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        GameStatus status = GameStatus.valueOf(rs.getString("status"));
        Color currentColor = Color.valueOf(rs.getString("current_color"));
        Score blackScore = new Score(rs.getBigDecimal("black_score"));
        Score whiteScore = new Score(rs.getBigDecimal("white_score"));
        return new ChessGameDto(id, name, status, blackScore, whiteScore, currentColor);
    }

    public ChessGameDto findById(int id) {
        return jdbcTemplate.queryForObject(
                "SELECT id, name, status, current_color, black_score, white_score FROM chess_game WHERE id = ?",
                this::createChessGameDto, id);
    }

    public int saveChessGame(String name, GameStatus status, Color currentColor, Score blackScore, Score whiteScore) {
        String sql = "INSERT INTO chess_game(name, status, current_color, black_score, white_score) VALUES(?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                    ps.setString(1, name);
                    ps.setString(2, status.name());
                    ps.setString(3, currentColor.name());
                    ps.setString(4, blackScore.getValue().toPlainString());
                    ps.setString(5, whiteScore.getValue().toPlainString());
                    return ps;
                }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public void updateChessGame(ChessGameDto chessGameDto) {
        jdbcTemplate.update(
                "UPDATE chess_game SET status=?, current_color=?, black_score=?, white_score=? WHERE id = ?",
                chessGameDto.getStatus().name(), chessGameDto.getCurrentColor().name(),
                chessGameDto.getBlackScore().getValue().toPlainString(),
                chessGameDto.getWhiteScore().getValue().toPlainString(), chessGameDto.getId());
    }

    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM chess_game");
    }

    public boolean existByName(String name) {
        int count = jdbcTemplate.queryForObject("SELECT count(*) FROM chess_game WHERE name = ?",
                Integer.class, name);
        return count != 0;
    }
}
