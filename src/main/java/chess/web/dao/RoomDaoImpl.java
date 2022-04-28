package chess.web.dao;

import chess.domain.entity.Room;
import chess.domain.board.Board;
import chess.domain.board.Team;
import chess.domain.board.Turn;
import chess.domain.board.piece.Pieces;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RoomDaoImpl implements RoomDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Board> boardRowMapper = (resultSet, rowNum) -> {
        Team team = Team.from(resultSet.getString("turn"));
        return Board.create(
                Pieces.from(new ArrayList<>()),
                new Turn(team)
        );
    };

    public RoomDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Turn> findTurnById(Long id) {
        final String query = "SELECT (turn) from room where id = ?";
        String turn = jdbcTemplate.queryForObject(query, String.class, id);

        return Optional.of(new Turn(Team.from(turn)));
    }

    @Override
    public void updateTurnById(Long id, String newTurn) {
        final String query = "UPDATE room set turn = ? where id = ?";
        jdbcTemplate.update(query, newTurn, id);
    }

    @Override
    public Long save(String title, String password) {
        final String query = "INSERT INTO room (turn, title, password) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(query, new String[]{"id"});
            preparedStatement.setString(1, "white");
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, password);
            return preparedStatement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public Optional<Board> findById(Long id) {
        final String query = "SELECT * " +
                "FROM room as r " +
                "JOIN piece as p ON r.id = p.room_id " +
                "WHERE r.id = ?";
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
        final String query = "DELETE FROM room WHERE id = ?";
        jdbcTemplate.update(query, id);
    }

    @Override
    public List<Room> findAll() {
        final String query = "SELECT id, title, password FROM room";
        return jdbcTemplate.query(
                query,
                (resultSet, rowNum) -> new Room(
                        resultSet.getLong("id"),
                        resultSet.getString("title"),
                        resultSet.getString("password")
                )
        );
    }
}
