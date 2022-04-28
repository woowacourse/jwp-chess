package chess.repository;

import chess.model.board.Board;
import chess.model.piece.Team;
import chess.model.status.Status;
import chess.model.status.StatusType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ChessBoardRepository implements BoardRepository<Board> {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Board> boardRowMapper = (resultSet, rowNum) -> new Board(
            resultSet.getInt("id"),
            StatusType.findStatus(resultSet.getString("status")),
            Team.findTeam(resultSet.getString("team"))
    );

    public ChessBoardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Board save(Board board) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("board").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("status", board.getStatus().name());
        parameters.put("team", board.getTeam().name());

        Number number = simpleJdbcInsert.executeAndReturnKey(parameters);
        return new Board(number.intValue(), board.getStatus(), board.getTeam());
    }

    @Override
    public int deleteByRoomId(int id) {
        return jdbcTemplate.update("DELETE b FROM board b " +
                "JOIN room r ON r.board_id=b.id WHERE r.id=?", id);
    }

    @Override
    public Board getById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM board WHERE id=?", boardRowMapper, id);
    }

    @Override
    public int updateStatus(int boardId, Status status) {
        return jdbcTemplate.update("UPDATE board SET status=? where id=?", status.name(), boardId);
    }

    @Override
    public int updateTeamById(int boardId, Team team) {
        return jdbcTemplate.update("UPDATE board SET team=? where id=?", team.name(), boardId);
    }

    @Override
    public Status getStatusById(int boardId) {
        String status = jdbcTemplate.queryForObject("SELECT status FROM board WHERE id=?", String.class, boardId);
        return StatusType.findStatus(status);
    }

    @Override
    public Team getTeamById(int boardId) {
        String team = jdbcTemplate.queryForObject("SELECT team FROM board WHERE id=?", String.class, boardId);
        return Team.findTeam(team);
    }
}
