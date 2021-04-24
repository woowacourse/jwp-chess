package chess.mysql.dao;

import chess.mysql.dao.dto.ChessGameDto;
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

    private final RowMapper<ChessGameDto> chessGameRowMapper = (rs, rownum) -> {
        long gameId = rs.getLong("id");
        String nextTurn = rs.getString("next_turn");
        boolean running = rs.getBoolean("running");
        String pieces = rs.getString("pieces");
        return new ChessGameDto(gameId, nextTurn, running, pieces);
    };

    public JdbcTemplateChessDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long save(ChessGameDto entity) {
        String query =
                "INSERT INTO chess.chessgame (pieces, running, next_turn) VALUES " +
                        "(?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection ->
                {
                    PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"});
                    ps.setString(1, entity.getPieces());
                    ps.setBoolean(2, entity.isRunning());
                    ps.setString(3, entity.getNextTurn());
                    return ps;
                }
                , keyHolder
        );

        return keyHolder.getKey().longValue();
    }

    @Override
    public Optional<ChessGameDto> findById(long id) {
        String query =
                "SELECT * " +
                        "FROM chess.chessgame " +
                        "WHERE id = ?";

        return jdbcTemplate.query(query, chessGameRowMapper, id).stream().findAny();
    }


    @Override
    public void update(ChessGameDto entity) {
        String query =
                "UPDATE chess.chessgame " +
                        "SET pieces = ?, running = ? , next_turn = ?" +
                        "WHERE id = ?";

        jdbcTemplate.update(query, entity.getPieces(), entity.isRunning(),
                entity.getNextTurn(), entity.getId());
    }

    @Override
    public List<ChessGameDto> findAllOnRunning() {
        String query =
                "SELECT * " +
                        "FROM chess.chessgame " +
                        "WHERE running = ?";

        return jdbcTemplate.query(query, chessGameRowMapper, true);
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM chess.chessgame " +
                "WHERE id = ?";

        jdbcTemplate.update(query, id);
    }
}
