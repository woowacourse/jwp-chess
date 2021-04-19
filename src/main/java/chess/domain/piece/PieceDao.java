package chess.domain.piece;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import chess.domain.board.BoardDto;
import chess.domain.position.MovePosition;
import chess.domain.position.MovePositionVo;

@Repository
public class PieceDao {

    private final JdbcTemplate jdbcTemplate;

    public PieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(Long chessId, BoardDto boardDto) {
        String sql = "INSERT INTO piece (chess_id, position, color, name) VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(
                sql,
                boardDto.getPieceDtos(),
                boardDto.getPieceDtos()
                        .size(),
                (pstmt, argument) -> {
                    pstmt.setLong(1, chessId);
                    pstmt.setString(2, argument.getPosition());
                    pstmt.setString(3, argument.getColor());
                    pstmt.setString(4, argument.getName());
                });
    }

    public void move(Long chessId, MovePosition movePosition) {
        MovePositionVo movePositionVO = MovePositionVo.from(movePosition);
        updateTargetPosition(chessId, movePositionVO);
        updateSourcePosition(chessId, movePositionVO);
    }

    private void updateTargetPosition(Long chessId, MovePositionVo movePositionVO) {
        String sql =
                "UPDATE piece AS target, (SELECT color, name FROM piece WHERE position = ? AND chess_id = ?) AS source " +
                        "SET target.color = source.color, target.name = source.name " +
                        "WHERE target.position = ? AND target.chess_id = ?";

        jdbcTemplate.update(sql, movePositionVO.getSource(), chessId, movePositionVO.getTarget(),
                chessId);
    }

    private void updateSourcePosition(Long chessId, MovePositionVo movePositionVO) {
        String sql =
                "UPDATE piece SET color = 'BLANK', name = 'BLANK' WHERE position = ? AND chess_id = ?";
        jdbcTemplate.update(sql, movePositionVO.getSource(), chessId);
    }
}
