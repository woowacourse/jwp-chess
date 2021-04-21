package chess.repository;

import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.dto.BoardDto;
import chess.domain.dto.RoomsDto;
import chess.domain.dto.TurnDto;
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
    private final String UPDATE_BOARD_QUERY = "update board set piece = ? where room_number = ? and position = ?";
    private final String UPDATE_GAME_QUERY = "update game set turn = ? where room_number = ?";

    public ChessDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BoardDto getSavedBoardInfo(int roomNumber) {
        String sql = "select position, piece from board where room_number = ?;";
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, roomNumber);
        Map<String, String> boardInfo = new HashMap<>();
        for (Map<String, Object> result : resultList) {
            String position = (String) result.get("position");
            String piece = (String) result.get("piece");
            boardInfo.put(position, piece);
        }
        return BoardDto.of(boardInfo);
    }

    public TurnDto getSavedTurnOwner(int roomNumber) {
        String sql = "select turn from game where room_number = ?;";
        String turnOwner = jdbcTemplate.queryForObject(sql, String.class, roomNumber);
        return TurnDto.of(turnOwner);
    }

    public void resetBoard(Board board, int roomNumber) {
        for (Map.Entry<Position, Piece> entry : board.getBoard().entrySet()) {
            Position position = entry.getKey();
            Piece piece = entry.getValue();
            String unicode = piece != null ? piece.getUnicode() : "";
            executeBoardUpdateQuery(unicode, position.convertToString(), roomNumber);
        }
    }

    private void executeBoardUpdateQuery(String unicode, String position, int roomNumber) {
        jdbcTemplate.update(UPDATE_BOARD_QUERY, unicode, position, roomNumber);
    }

    public void renewBoardAfterMove(String targetPosition, String destinationPosition, Piece targetPiece, int roomNumber) {
        jdbcTemplate.update(UPDATE_BOARD_QUERY, targetPiece.getUnicode(), destinationPosition, roomNumber);
        jdbcTemplate.update(UPDATE_BOARD_QUERY, "", targetPosition, roomNumber);
    }

    public void renewTurnOwnerAfterMove(Team turnOwner, int roomNumber) {
        jdbcTemplate.update(UPDATE_GAME_QUERY, turnOwner.getTeamString(), roomNumber);
    }

    public void resetTurnOwner(Team turnOwner) {
        jdbcTemplate.update(UPDATE_GAME_QUERY, "white", turnOwner.getTeamString());
    }

    public RoomsDto getRoomList() {
        String sql = "select * from game;";
        List<String> roomNames = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("room_name"));
        List<Integer> roomNumbers = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("room_number"));
        return RoomsDto.of(roomNames, roomNumbers);
    }
}
