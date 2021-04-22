package chess.repository.dao;


import chess.domain.game.ChessGame;
import chess.repository.ChessGameRepository;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
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
            resultSet.getString("current_turn_team_color"),
            resultSet.getString("white_player_password"),
            resultSet.getString("black_player_password"));

    @Override
    public Long save(ChessGame chessGame) {
        String query = "INSERT INTO chess_game "
            + "(title, board_status, current_turn_team_color, white_player_password, black_player_password) "
            + "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"chess_game_id"});
            ps.setString(1, chessGame.getTitle());
            ps.setString(2, chessGame.getBoardStatus());
            ps.setString(3, chessGame.getCurrentTurnTeamColor().getValue());
            ps.setString(4, chessGame.getEncryptedWhitePlayerPassword());
            ps.setString(5, chessGame.getEncryptedBlackPlayerPassword());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public Optional<ChessGame> findById(Long gameId) {
        String query = "SELECT * FROM chess_game WHERE chess_game_id = ?";
        ChessGame result = DataAccessUtils.singleResult(
            jdbcTemplate.query(query, chessGameRowMapper, gameId)
        );
        return Optional.ofNullable(result);
    }

    @Override
    public List<ChessGame> findAll() {
        String query = "SELECT * FROM chess_game";
        return jdbcTemplate.query(query, chessGameRowMapper);
    }

    @Override
    public List<ChessGame> findAllBlackPlayerPasswordIsNull() {
        String query = "SELECT * FROM chess_game WHERE black_player_password is NULL";
        return jdbcTemplate.query(query, chessGameRowMapper);
    }

    @Override
    public void update(ChessGame chessGame) {
        String query = "UPDATE chess_game "
            + "SET "
            + "board_status = ?, "
            + "current_turn_team_color = ?, "
            + "black_player_password = ? "
            + "WHERE chess_game_id = ?";
        jdbcTemplate.update(query,
            chessGame.getBoardStatus(),
            chessGame.getCurrentTurnTeamColorValue(),
            chessGame.getEncryptedBlackPlayerPassword(),
            chessGame.getId());
    }

    @Override
    public void deleteById(Long gameId) {
        String query = "DELETE FROM chess_game WHERE chess_game_id = ?";
        jdbcTemplate.update(query, gameId);
    }

    @Override
    public void deleteAll() {
        String query = "DELETE FROM chess_game";
        jdbcTemplate.update(query);
    }
}
