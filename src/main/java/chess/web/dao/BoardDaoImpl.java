package chess.web.dao;

import chess.board.Board;
import chess.board.Team;
import chess.board.Turn;
import chess.board.piece.Pieces;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDaoImpl implements BoardDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Board> boardRowMapper = (resultSet, rowNum) -> {
        Team team = Team.from(resultSet.getString("turn"));
        return Board.create(
                Pieces.from(new ArrayList<>()),
                new Turn(team)
        );
    };

    public BoardDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Long boardId, Turn turn) {
        final String query = "INSERT INTO board (turn) VALUES (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(query, new String[]{"id"});
            preparedStatement.setString(1, "white");
            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public Optional<Turn> findTurnById(Long id) {
        final String query = "SELECT (turn) from board where id = ?";

        try {
            return Optional.of(new Turn(Team.from(jdbcTemplate.queryForObject(query, String.class, id))));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public void updateTurnById(Long id, String newTurn) {
        final String query = "UPDATE board set turn = ? where id = ?";
        jdbcTemplate.update(query, newTurn, id);
    }

    @Override
    public void deleteById(Long id) {
        final String query = "DELETE FROM board WHERE id = ?";
        jdbcTemplate.update(query, id);
    }
}
