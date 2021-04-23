package chess.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import chess.domain.position.MovePosition;
import chess.domain.position.MovePositionVo;
import chess.repository.MoveRepository;

@Repository
public class MoveDao implements MoveRepository {
    private final JdbcTemplate jdbcTemplate;

    public MoveDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void move(long chessId, MovePosition movePosition) {
        MovePositionVo movePositionVO = MovePositionVo.from(movePosition);
        updateTargetPosition(chessId, movePositionVO);
        updateSourcePosition(chessId, movePositionVO);
    }

    private void updateTargetPosition(long chessId, MovePositionVo movePositionVO) {
        String sql =
                "UPDATE piece AS target, (SELECT color, name FROM piece WHERE position = ? AND chess_id = ?) AS source " +
                        "SET target.color = source.color, target.name = source.name " +
                        "WHERE target.position = ? AND target.chess_id = ?";

        jdbcTemplate.update(sql, movePositionVO.getSource(), chessId, movePositionVO.getTarget(),
                chessId);
    }

    private void updateSourcePosition(long chessId, MovePositionVo movePositionVO) {
        String sql =
                "UPDATE piece SET color = 'BLANK', name = 'BLANK' WHERE position = ? AND chess_id = ?";
        jdbcTemplate.update(sql, movePositionVO.getSource(), chessId);
    }


    @Override
    public void updateChess(long chessId, String status, String turn) {
        String sql = "UPDATE chess SET status = ?, turn = ? WHERE chess_id = ?";
        jdbcTemplate.update(sql, status, turn, chessId);
    }
}
