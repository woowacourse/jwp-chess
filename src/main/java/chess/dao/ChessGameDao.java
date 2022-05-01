package chess.dao;

import chess.domain.ChessGame;
import chess.domain.Score;
import chess.domain.piece.Color;
import chess.domain.vo.Room;
import chess.dto.ChessGameDto;
import chess.dto.GameStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ChessGameDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ChessGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int saveChessGame(ChessGame chessGame) {
        String sql = "INSERT INTO chess_game(name, password, status, current_color, black_score, white_score) VALUES(?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> getPreparedStatement(chessGame, sql, connection), keyHolder);
        return keyHolder.getKey().intValue();
    }

    public ChessGameDto findById(int id) {
        return DataAccessUtils.singleResult(jdbcTemplate.query(
            "SELECT id, name, status, current_color, black_score, white_score FROM chess_game WHERE id = ?",
            this::createChessGameDto, id));
    }

    public List<ChessGameDto> findAll() {
        return jdbcTemplate.query(
                "SELECT id, name, status, current_color, black_score, white_score FROM chess_game",
                this::createChessGameDto);
    }

    public void updateChessGame(ChessGameDto chessGameDto) {
        jdbcTemplate.update(
                "UPDATE chess_game SET status=?, current_color=?, black_score=?, white_score=? WHERE id = ?",
                chessGameDto.getStatus().name(),
                chessGameDto.getCurrentColor().name(),
                chessGameDto.getBlackScore().getValue().toPlainString(),
                chessGameDto.getWhiteScore().getValue().toPlainString(), chessGameDto.getId());
    }

    public void deleteByIdAndPassword(int chessGameId, String password) {
        jdbcTemplate.update(
            "DELETE FROM chess_game WHERE chess_game.id = ? and chess_game.password = ? and chess_game.status != 'RUNNING'",
            chessGameId,
            password
        );
    }

    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM chess_game");
    }

    public boolean existByName(String name) {
        int count = jdbcTemplate.queryForObject(
            "SELECT count(*) FROM chess_game WHERE name = ?", Integer.class, name);
        return count != 0;
    }

    private PreparedStatement getPreparedStatement(
        ChessGame chessGame, String sql, Connection connection
    ) throws SQLException {
        PreparedStatement request = connection.prepareStatement(sql, new String[]{"id"});
        request.setString(1, chessGame.getRoom().getName());
        request.setString(2, chessGame.getRoom().getPassword());
        request.setString(3, chessGame.getStatus().name());
        request.setString(4, chessGame.getCurrentColor().name());
        request.setString(5, chessGame.getBlackScore().getValue().toPlainString());
        request.setString(6, chessGame.getWhiteScore().getValue().toPlainString());
        return request;
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
}
