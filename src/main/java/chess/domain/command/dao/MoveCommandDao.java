package chess.domain.command.dao;

import chess.domain.command.Command;
import chess.domain.command.Move;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MoveCommandDao {

    private final JdbcTemplate jdbcTemplate;

    public MoveCommandDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int addMoveCommand(Move moveCommand) {
        String sql = "INSERT INTO move_command (game_id, source_position, target_position, turn)"
                + "SELECT ?, ?, ?, ?"
                + "FROM DUAL "
                + "WHERE (SELECT turn FROM move_command WHERE game_id = ? ORDER BY created_at DESC LIMIT 1) = ?"
                + "   OR (SELECT turn FROM move_command WHERE game_id = ? ORDER BY created_at DESC LIMIT 1) IS NULL;";

        return jdbcTemplate.update(sql,
                moveCommand.getGameId(),
                moveCommand.getSource(),
                moveCommand.getTarget(),
                moveCommand.getSide().name(),
                moveCommand.getGameId(),
                moveCommand.getSide().changeTurn().name(),
                moveCommand.getGameId()
        );
    }

    public List<Command> findCommandsByGameId(String gameId) {
        String sql = "SELECT cmd.source_position, cmd.target_position FROM chess_game game "
                + "JOIN move_command cmd on game.id = cmd.game_id "
                + "WHERE is_end=false AND game.id=(?) ORDER BY cmd.created_at";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            String source = rs.getString("cmd.source_position");
            String target = rs.getString("cmd.target_position");
            return new Move(source, target);
        }, gameId);
    }
}
