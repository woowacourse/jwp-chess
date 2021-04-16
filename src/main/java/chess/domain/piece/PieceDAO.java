package chess.domain.piece;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import chess.domain.board.BoardDTO;
import chess.domain.position.MovePosition;
import chess.domain.position.MovePositionVO;

@Repository
public class PieceDAO {

    private final JdbcTemplate jdbcTemplate;

    public PieceDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(Long chessId, BoardDTO boardDTO) {
        String sql = "INSERT INTO piece (chess_id, position, color, name) VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(
                sql,
                boardDTO.getPieceDTOS(),
                boardDTO.getPieceDTOS()
                        .size(),
                (pstmt, argument) -> {
                    pstmt.setLong(1, chessId);
                    pstmt.setString(2, argument.getPosition());
                    pstmt.setString(3, argument.getColor());
                    pstmt.setString(4, argument.getName());
                });
    }

    public void move(Long chessId, MovePosition movePosition) {
        MovePositionVO movePositionVO = MovePositionVO.from(movePosition);
        updateTargetPosition(chessId, movePositionVO);
        updateSourcePosition(chessId, movePositionVO);
    }

    private void updateTargetPosition(Long chessId, MovePositionVO movePositionVO) {
        String sql =
                "UPDATE piece AS target, (SELECT color, name FROM piece WHERE position = (?) AND chess_id = (?)) AS source "
                        +
                        "SET target.color = source.color, target.name = source.name " +
                        "WHERE target.position = (?) AND target.chess_id = (?)";

        jdbcTemplate.update(sql, movePositionVO.getSource(), chessId, movePositionVO.getTarget(),
                chessId);
    }

    private void updateSourcePosition(Long chessId, MovePositionVO movePositionVO) {
        String sql =
                "UPDATE piece SET color = 'BLANK', name = 'BLANK' WHERE position = (?) AND chess_id = (?)";
        jdbcTemplate.update(sql, movePositionVO.getSource(), chessId);
    }
}
