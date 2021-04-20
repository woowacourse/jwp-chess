package chess.repository;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.Position;
import chess.domain.dto.BoardDto;
import chess.domain.dto.RoomListDto;
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
    private final String UPDATE_BOARD_QUERY = "update board set piece = ? where position = ? and room_name = ?";
    private final String UPDATE_GAME_QUERY = "update game set turn_owner = ? where room_name = ?";

    public ChessDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BoardDto getSavedBoard(String roomName) {
        String sql = "select position, piece from board where room_name = ?;";
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, roomName);
        Map<String, String> boardInfo = new HashMap<>();
        for (Map<String, Object> result : resultList) {
            String position = (String) result.get("position");
            String piece = (String) result.get("piece");
            boardInfo.put(position, piece);
        }
        return BoardDto.of(boardInfo);
    }

    public TurnDto getSavedTurnOwner(String roomName) {
        String sql = "select turn_owner from game where room_name = ?;";
        String turnOwner = jdbcTemplate.queryForObject(sql, String.class, roomName);
        return TurnDto.of(turnOwner);
    }

    public void resetBoard(Board board, String roomName) {
        for (Map.Entry<Position, Piece> entry : board.getBoard().entrySet()) {
            Position position = entry.getKey();
            Piece piece = entry.getValue();
            String unicode = piece != null ? piece.getUnicode() : "";
            executeBoardUpdateQuery(unicode, position.convertToString(), roomName);
        }
    }

    public RoomListDto getRoomList() {
        String sql = "select * from game;";
        List<String> roomNames = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("room_name"));
        return new RoomListDto(roomNames);
    }

    private void executeBoardInsertQuery(String unicode, String position, String roomName) {
        String sql = "insert into board (piece, position, room_name) values (?, ?, ?);";
        jdbcTemplate.update(sql, unicode, position, roomName);
    }

    public void addGame(String roomName) {
        initializeGameTable(roomName);
        initializeBoardTable(roomName);
    }

    private void initializeGameTable(String roomName) {
        String sql = "insert into game(room_name, turn_owner) values (?, 'white');";
        jdbcTemplate.update(sql, roomName);
    }

    private void initializeBoardTable(String roomName) {
        for (Map.Entry<Position, Piece> entry : BoardFactory.create().getBoard().entrySet()) {
            Position position = entry.getKey();
            Piece piece = entry.getValue();
            String unicode = piece != null ? piece.getUnicode() : "";
            executeBoardInsertQuery(unicode, position.convertToString(), roomName);
        }
    }

    private void executeBoardUpdateQuery(String unicode, String position, String roomName) {
        jdbcTemplate.update(UPDATE_BOARD_QUERY, unicode, position, roomName);
    }

    public void renewBoardAfterMove(String targetPosition, String destinationPosition,
                                    Piece targetPiece, String roomName) {
        jdbcTemplate.update(UPDATE_BOARD_QUERY, targetPiece.getUnicode(), destinationPosition, roomName);
        jdbcTemplate.update(UPDATE_BOARD_QUERY, "", targetPosition, roomName);
    }

    public void renewTurnOwnerAfterMove(Team turnOwner, String roomName) {
        jdbcTemplate.update(UPDATE_GAME_QUERY,
                turnOwner.getTeamString(), roomName);
    }

    public void resetTurnOwner(String roomName) {
        jdbcTemplate.update(UPDATE_GAME_QUERY, "white", roomName);
    }
}
