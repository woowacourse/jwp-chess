package chess.repository;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.Team;
import chess.dto.BoardDto;
import chess.dto.RoomsDto;
import chess.dto.TurnDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ChessDao {
    private static final String UPDATE_BOARD_QUERY = "update board set piece = ? where position = ? and room_id = ?";
    private static final String UPDATE_ROOM_QUERY = "update room set turn = ? where room_id = ?";
    private final JdbcTemplate jdbcTemplate;

    public ChessDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BoardDto getSavedBoardInfo(int roomId) {
        String sql = "select position, piece from board where room_id = ?;";
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, roomId);
        Map<String, String> boardInfo = new HashMap<>();
        for (Map<String, Object> result : resultList) {
            String position = (String) result.get("position");
            String piece = (String) result.get("piece");
            boardInfo.put(position, piece);
        }
        Board board = Board.from(boardInfo);
        return BoardDto.of(board);
    }

    public BoardDto initializeByName(String roomName) {
        int roomId = initializeRoom(roomName);
        return initializeBoard(roomId);
    }

    private int initializeRoom(String roomName) {
        String sql = "insert into room (room_name, turn) values (?, 'white');";
        jdbcTemplate.update(sql, roomName);
        return findRoomIdByRoomName(roomName);
    }

    private int findRoomIdByRoomName(String roomName) {
        String sql = "select room_id from room where room_name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, roomName);
    }

    public BoardDto initializeBoard(int roomId) {
        Board newBoard = BoardFactory.create();
        newBoard.getBoard().forEach((key, value) -> {
            String unicode = value != null ? value.getUnicode() : "";
            executeBoardInsertQuery(key.convertToString(), unicode, roomId);
        });
        return BoardDto.of(newBoard);
    }

    private void executeBoardInsertQuery(String position, String unicode, int roomId) {
        String sql = "insert into board (position, piece, room_id) values (?, ?, ?)";
        jdbcTemplate.update(sql, position, unicode, roomId);
    }

    public TurnDto getSavedTurnOwner(int roomId) {
        String sql = "select turn from room where room_id = ?;";
        String turnOwner = jdbcTemplate.queryForObject(sql, String.class, roomId);
        return TurnDto.of(turnOwner);
    }

    public void resetBoard(Board board, int roomId) {
        for (Map.Entry<Position, Piece> entry : board.getBoard().entrySet()) {
            Position position = entry.getKey();
            Piece piece = entry.getValue();
            String unicode = piece != null ? piece.getUnicode() : "";
            executeBoardUpdateQuery(unicode, position.convertToString(), roomId);
        }
    }

    private void executeBoardUpdateQuery(String unicode, String position, int roomId) {
        jdbcTemplate.update(UPDATE_BOARD_QUERY, unicode, position, roomId);
    }

    public void renewBoardAfterMove(String targetPosition, String destinationPosition, Piece targetPiece, int roomId) {
        jdbcTemplate.update(UPDATE_BOARD_QUERY, targetPiece.getUnicode(), destinationPosition, roomId);
        jdbcTemplate.update(UPDATE_BOARD_QUERY, "", targetPosition, roomId);
    }

    public void renewTurnOwnerAfterMove(Team turnOwner, int roomId) {
        jdbcTemplate.update(UPDATE_ROOM_QUERY, turnOwner.getTeamString(), roomId);
    }

    public void resetTurnOwner(int roomId) {
        jdbcTemplate.update(UPDATE_ROOM_QUERY, "white", roomId);
    }

    public RoomsDto getRoomList() {
        String sql = "select * from room;";
        List<String> roomNames = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("room_name"));
        List<Integer> roomIds = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("room_id"));
        return RoomsDto.of(roomNames, roomIds);
    }
}
