package chess.web.dao;

import chess.board.Board;
import chess.board.Team;
import chess.board.Turn;
import chess.board.piece.Pieces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BoardDaoImpl implements BoardDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Board> boardRowMapper = (resultSet, rowNum) -> {
        Team team = Team.from(resultSet.getString("turn"));
        return Board.create(
                Pieces.from(new ArrayList<>()),
                new Turn(team)
        );
    };

    @Override
    public Optional<Turn> findTurnById(Long id) {
        final String query = "SELECT (turn) from board where id = ?";
        String turn = jdbcTemplate.queryForObject(query, String.class, id);

        return Optional.of(new Turn(Team.from(turn)));
    }

    @Override
    public void updateTurnById(Long id, String newTurn) {
        final String query = "UPDATE board set turn = ? where id = ?";
        jdbcTemplate.update(query, newTurn, id);
    }

    @Override
    public Long save() {
        final String query = "INSERT INTO board (turn) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(query, new String[]{"id"});
            preparedStatement.setString(1, "white");
            return preparedStatement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public Optional<Board> findById(Long id) {
        final String query = "SELECT * " +
                "FROM board as b " +
                "JOIN piece as p ON b.id = p.board_id " +
                "WHERE b.id = ?";
        List<Board> boards = jdbcTemplate.query(query, boardRowMapper, id);
        Board board = getBoard(boards);
        return Optional.of(board);
    }

    private Board getBoard(List<Board> boards) {
        if (boards.isEmpty()) {
            return Board.create(Pieces.from(new ArrayList<>()), Turn.init());
        }
        return DataAccessUtils.singleResult(boards);
    }

    @Override
    public void deleteById(Long id) {
        final String query = "DELETE FROM board WHERE id = ?";
        jdbcTemplate.update(query, id);
    }
}
