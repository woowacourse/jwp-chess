package chess.repository.dao;


import chess.domain.game.ChessGame;
import chess.repository.ChessGameRepository;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ChessGameDao implements ChessGameRepository {

    private final JdbcTemplate jdbcTemplate;

    public ChessGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ChessGame> chessGameRowMapper = (resultSet, rowNum) ->
        new ChessGame(
            resultSet.getLong("chess_game_id"),
            resultSet.getString("title"),
            resultSet.getString("board_status"),
            resultSet.getString("current_turn_team_color"));

    @Override
    public Long save(ChessGame chessGame) {
        String query = "INSERT INTO chess_game "
            + "(title, board_status, current_turn_team_color) "
            + "VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"chess_game_id"});
            ps.setString(1, chessGame.getTitle());
            ps.setString(2, chessGame.getBoardStatus());
            ps.setString(3, chessGame.getCurrentTurnTeamColor().getValue());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public ChessGame findById(Long gameId) {
        String query = "SELECT * FROM chess_game WHERE chess_game_id = ?";
        return jdbcTemplate.queryForObject(query, chessGameRowMapper, gameId);
    }

    @Override
    public List<ChessGame> findAll() {
        String query = "SELECT * FROM chess_game";
        return jdbcTemplate.query(query, chessGameRowMapper);
    }

    @Override
    public void update(ChessGame chessGame) {
        String query = "UPDATE chess_game "
            + "SET "
            + "board_status = ?, "
            + "current_turn_team_color = ? "
            + "WHERE chess_game_id = ?";
        jdbcTemplate.update(query,
            chessGame.getBoardStatus(),
            chessGame.getCurrentTurnTeamColorValue(),
            chessGame.getId());
    }

    @Override
    public void deleteById(Long gameId) {
        String query = "DELETE FROM chess_game WHERE chess_game_id = ?";
        jdbcTemplate.update(query, gameId);
    }
}
