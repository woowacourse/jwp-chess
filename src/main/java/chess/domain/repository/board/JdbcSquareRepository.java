package chess.domain.repository.board;

import chess.domain.board.Board;
import chess.domain.board.piece.Square;
import chess.domain.board.position.Position;
import chess.util.PieceConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcSquareRepository implements SquareRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Square> squareRowMapper = (resultSet, rowNum) ->
            new Square(
                    resultSet.getLong("id"),
                    resultSet.getLong("game_id"),
                    Position.of(resultSet.getString("position")),
                    PieceConverter.parsePiece(resultSet.getString("symbol"))
            );

    public JdbcSquareRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Long save(final Square square) {
        final String sql = "INSERT INTO square(game_id, position, symbol) VALUES(?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, square.getGameId());
            ps.setString(2, square.getPosition().parseString());
            ps.setString(3, square.getPiece().getSymbol());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public long[] saveBoard(final Long gameId, final Board board) {
        String sql = "INSERT INTO square(game_id, position, symbol) VALUES (?, ?, ?)";
        List<Object[]> collect = board.getBoard().entrySet().stream()
                .map(entry -> new Object[]{gameId, entry.getKey().parseString(), entry.getValue().getSymbol()})
                .collect(Collectors.toList());
        int[] ints = jdbcTemplate.batchUpdate(sql, collect);
        return Arrays.stream(ints).asLongStream().toArray();
    }

    @Override
    public Square findById(final Long id) {
        final String sql = "SELECT * FROM square WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, squareRowMapper, id);
    }

    @Override
    public List<Square> findByGameId(final Long gameId) {
        final String sql = "SELECT * FROM square WHERE game_id = ?";
        return jdbcTemplate.query(sql, squareRowMapper, gameId);
    }

    @Override
    public Long updateByGameIdAndPosition(final Square square) {
        final String sql = "UPDATE square SET symbol = ? WHERE game_id=? AND position=?";
        return (long) jdbcTemplate.update(sql, square.getPiece().getSymbol(), square.getGameId(), square.getPosition().parseString());
    }
}
