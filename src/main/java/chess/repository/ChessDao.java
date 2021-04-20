package chess.repository;

import chess.domain.board.Board;
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

    public void addGame(String roomName) {
        String sql = "insert into game(room_name, turn_owner) values (?, 'white');";
        jdbcTemplate.update(sql, roomName);
        String sql3 = "insert into board (position, piece, room_name) values ('a1', '&#9814;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('b1', '&#9816;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('c1', '&#9815;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('d1', '&#9812;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('e1', '&#9813;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('f1', '&#9815;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('g1', '&#9816;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('h1', '&#9814;', ?);";
        jdbcTemplate.update(sql3, roomName);

        sql3 = "insert into board (position, piece, room_name) values ('a8', '&#9820;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('b8', '&#9822;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('c8', '&#9821;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('d8', '&#9818;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('e8', '&#9819;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('f8', '&#9821;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('g8', '&#9822;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('h8', '&#9820;', ?);";
        jdbcTemplate.update(sql3, roomName);

        sql3 = "insert into board (position, piece, room_name) values ('a7', '&#9823;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('b7', '&#9823;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('c7', '&#9823;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('d7', '&#9823;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('e7', '&#9823;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('f7', '&#9823;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('g7', '&#9823;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('h7', '&#9823;', ?);";
        jdbcTemplate.update(sql3, roomName);

        sql3 = "insert into board (position, piece, room_name) values ('a2', '&#9817;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('b2', '&#9817;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('c2', '&#9817;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('d2', '&#9817;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('e2', '&#9817;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('f2', '&#9817;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('g2', '&#9817;', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('h2', '&#9817;', ?);";
        jdbcTemplate.update(sql3, roomName);

        sql3 = "insert into board (position, piece, room_name) values ('a3', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('b3', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('c3', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('d3', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('e3', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('f3', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('g3', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('h3', '', ?);";
        jdbcTemplate.update(sql3, roomName);

        sql3 = "insert into board (position, piece, room_name) values ('a4', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('b4', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('c4', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('d4', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('e4', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('f4', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('g4', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('h4', '', ?);";
        jdbcTemplate.update(sql3, roomName);

        sql3 = "insert into board (position, piece, room_name) values ('a5', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('b5', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('c5', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('d5', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('e5', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('f5', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('g5', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('h5', '', ?);";
        jdbcTemplate.update(sql3, roomName);

        sql3 = "insert into board (position, piece, room_name) values ('a6', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('b6', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('c6', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('d6', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('e6', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('f6', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('g6', '', ?);";
        jdbcTemplate.update(sql3, roomName);
        sql3 = "insert into board (position, piece, room_name) values ('h6', '', ?);";
        jdbcTemplate.update(sql3, roomName);
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
