package chess.repository;

import chess.domain.dto.BoardDto;
import chess.domain.dto.TurnDto;
import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.Team;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ChessDao {
    private final JdbcTemplate jdbcTemplate;
    private final String UPDATE_BOARD_QUERY = "update board set piece = ? where position = ?";
    private final String UPDATE_TURN_QUERY = "update turn set turn_owner = ? where turn_owner = ?";

    public ChessDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BoardDto getSavedBoardInfo() {
        String sql = "select position, piece from board;";
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
        Map<String, String> boardInfo = new HashMap<>();
        for (Map<String, Object> result : resultList) {
            String position = (String) result.get("position");
            String piece = (String) result.get("piece");
            boardInfo.put(position, piece);
        }
        return BoardDto.of(boardInfo);
    }

    public TurnDto getSavedTurnOwner() {
        String sql = "select * from turn;";
        String turnOwner = jdbcTemplate.queryForObject(sql, String.class);
        return TurnDto.of(turnOwner);
    }

    public void resetBoard(Board board) {
        for (Map.Entry<Position, Piece> entry : board.getBoard().entrySet()) {
            Position position = entry.getKey();
            Piece piece = entry.getValue();
            String unicode = piece != null ? piece.getUnicode() : "";
            executeBoardUpdateQuery(unicode, position.convertToString());
        }
    }

    private void executeBoardUpdateQuery(String unicode, String position) {
        jdbcTemplate.update(UPDATE_BOARD_QUERY, unicode, position);
    }

    public void renewBoardAfterMove(String targetPosition, String destinationPosition, Piece targetPiece) {
        jdbcTemplate.update(UPDATE_BOARD_QUERY, targetPiece.getUnicode(), destinationPosition);
        jdbcTemplate.update(UPDATE_BOARD_QUERY, "", targetPosition);
    }

    public void renewTurnOwnerAfterMove(Team turnOwner) {
        jdbcTemplate.update(UPDATE_TURN_QUERY, turnOwner.getTeamString(), turnOwner.getOpposite().getTeamString());
    }

    public void resetTurnOwner(Team turnOwner) {
        jdbcTemplate.update(UPDATE_TURN_QUERY, "white", turnOwner.getTeamString());
    }
}
