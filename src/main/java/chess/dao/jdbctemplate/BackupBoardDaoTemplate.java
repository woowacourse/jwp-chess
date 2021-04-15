package chess.dao.jdbctemplate;

import chess.dao.BackupBoardDao;
import chess.domain.board.Board;
import chess.dto.SquareDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BackupBoardDaoTemplate implements BackupBoardDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<SquareDto> actorRowMapper = (resultSet, rowNum) ->
        new SquareDto(
            resultSet.getString("position"),
            resultSet.getString("piece")
        );

    public BackupBoardDaoTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void deleteExistingBoard(String name) {
        String sql = "DELETE FROM backup_board WHERE room_name = ?";
        jdbcTemplate.update(sql, name);
    }

    @Override
    public void addPlayingBoard(String name, Board board) {
        String sql = "INSERT INTO backup_board VALUES (?, ?, ?)";
        List<Object[]> squares = board.positions()
            .stream()
            .map(position -> new Object[]{name, position.toString(), board.pieceAtPosition(position).toString()})
            .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(sql, squares);
    }

    @Override
    public List<SquareDto> findPlayingBoardByRoom(String name) {
        String sql = "SELECT backup_board.position, backup_board.piece FROM backup_board " +
            "WHERE backup_board.room_name = ?";

        return jdbcTemplate.query(sql, actorRowMapper, name);
    }
}
