package chess.domain.repository.game;

import chess.domain.board.Board;
import chess.domain.board.piece.Owner;
import chess.domain.board.piece.Piece;
import chess.domain.board.piece.Score;
import chess.domain.board.position.Position;
import chess.domain.manager.Game;
import chess.domain.manager.GameStatus;
import chess.domain.manager.State;
import chess.domain.manager.TurnNumber;
import chess.util.PieceConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

@Repository
public class JdbcGameRepository implements GameRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Game> gameRowMapper = (resultSet, rowNum) -> {
        Long id = resultSet.getLong("id");
        State state = State.of(Owner.valueOf(resultSet.getString("turn_owner")),
                TurnNumber.of(resultSet.getInt("turn_number")),
                resultSet.getBoolean("playing"));
        GameStatus gameStatus = GameStatus.from(new Score(resultSet.getDouble("white_score")),
                new Score(resultSet.getDouble("black_score")));
        Map<Position, Piece> pieceMap = new HashMap<>();
        do {
            pieceMap.put(Position.of(resultSet.getString("position")),
                    PieceConverter.parsePiece(resultSet.getString("symbol")));
        } while (resultSet.next());
        Board board = new Board(pieceMap);
        return new Game(id, board, state, gameStatus);
    };

    public JdbcGameRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(final Game game) {
        String sql = "INSERT INTO game(turn_owner, turn_number, playing, white_score, black_score) VALUES(?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, game.turnOwnerName());
            ps.setInt(2, game.turnNumberValue());
            ps.setBoolean(3, game.isPlaying());
            ps.setDouble(4, game.gameStatus().whiteScore());
            ps.setDouble(5, game.gameStatus().blackScore());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public Game findById(final Long id) {
        String sql = "SELECT * FROM game AS g JOIN square AS s ON g.id = s.game_id WHERE g.id = ?";
        return jdbcTemplate.queryForObject(sql, gameRowMapper, id);
    }

    @Override
    public void update(final Game game) {
        String sql = "UPDATE game SET turn_owner = ?, turn_number = ?, playing = ?, white_score = ?, black_score = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                game.turnOwnerName(),
                game.turnNumberValue(),
                game.isPlaying(),
                game.gameStatus().whiteScore(),
                game.gameStatus().blackScore(),
                game.getId());
    }
}
