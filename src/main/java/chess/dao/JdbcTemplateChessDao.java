package chess.dao;

import chess.dao.dto.ChessGame;
import chess.domain.piece.attribute.Color;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTemplateChessDao implements ChessDao {
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<ChessGame> chessGameRowMapper = (rs, rownum) -> {
        long gameId = rs.getLong("id");
        String nextTurn = rs.getString("next_turn");
        boolean running = rs.getBoolean("running");
        String pieces = rs.getString("pieces");
        return new ChessGame(gameId, Color.of(nextTurn), running, pieces);
    };

    public JdbcTemplateChessDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long save(ChessGame entity) {
        String query =
                "INSERT INTO CHESSGAME (pieces, running, next_turn) VALUES " +
                        "(?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection ->
                {
                    PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"});
                    ps.setString(1, entity.getPieces());
                    ps.setBoolean(2, entity.isRunning());
                    ps.setString(3, entity.getNextTurn().name());
                    return ps;
                }
                , keyHolder
        );

        return keyHolder.getKey().longValue();
    }

    @Override
    public Optional<ChessGame> findById(long id) {
        String query =
                "SELECT * " +
                        "FROM CHESSGAME " +
                        "WHERE id = ?";

        return Optional.ofNullable(jdbcTemplate.queryForObject(query, chessGameRowMapper, id));
    }


    @Override
    public void update(ChessGame entity) {
        String query =
                "UPDATE CHESSGAME " +
                        "SET pieces = ?, running = ? , next_turn = ?" +
                        "WHERE id = ?";

        jdbcTemplate.update(query, entity.getPieces(), entity.isRunning(),
                entity.getNextTurn().name(), entity.getId());
    }

    @Override
    public List<ChessGame> findAllOnRunning() {
        String query =
                "SELECT * " +
                        "FROM CHESSGAME " +
                        "WHERE running = ?";

        return jdbcTemplate.query(query, chessGameRowMapper, true);
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM CHESSGAME " +
                "WHERE id = ?";

        jdbcTemplate.update(query, id);
    }
}
