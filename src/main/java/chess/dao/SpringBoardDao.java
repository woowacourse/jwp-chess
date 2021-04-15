package chess.dao;

import chess.domain.Side;
import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.position.Position;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class SpringBoardDao {

    private final JdbcTemplate jdbcTemplate;

    public SpringBoardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<Position, Piece> initBoardTable(String roomName) {
        try {
            return findBoard(roomName);
        } catch (DataAccessException e) {
            newBoard(roomName);
            return initBoardTable(roomName);
        }
    }

    public Map<Position, Piece> newBoard(String roomName) {
        String query = "INSERT INTO board (roomName, position, pieceName, turn) VALUES (?, ?, ?, ?)";
        Board board = Board.getGamingBoard();
        this.jdbcTemplate.update(query, roomName, boardPositionSet(board.getBoard()), boardPieceSet(board.getBoard()), "WHITE");

        return board.getBoard();
    }

    public void updateBoard(Board board, String turn, String roomName) {
        System.out.println("@@@@@@@@@@update" + roomName);

        String query = "UPDATE board SET position = ?, pieceName = ?, turn = ? WHERE roomName = ?";
        this.jdbcTemplate.update(query, boardPositionSet(board.getBoard()), boardPieceSet(board.getBoard()), turn, roomName);
    }

    public Map<Position, Piece> findBoard(String roomName) {
        String query = "select * from board where roomName=?";
        try {
            return this.jdbcTemplate.queryForObject(
                    query,
                    (resultSet, rowNum) -> daoToBoard(
                            resultSet.getString("position"),
                            resultSet.getString("pieceName")
                    ),
                    roomName);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Side findTurn(String roomName) {
        try {
            System.out.println("@@@@@@@@@@findTurn" + roomName);
            String query = "SELECT turn FROM board WHERE roomName = ?";
            String side = this.jdbcTemplate.queryForObject(query, String.class, roomName);
            System.out.println("@@@@@@@@@@findTurn" + side);

            return Side.getTurnByName(side);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private String boardPositionSet(Map<Position, Piece> board) {
        return board.keySet()
                .stream()
                .map(Position::positionName)
                .collect(Collectors.joining(","));
    }

    private String boardPieceSet(Map<Position, Piece> board) {
        List<String> pieceNames = new ArrayList<>();
        for (Piece piece : board.values()) {
            pieceNames.add(pieceToName(piece));
        }
        return String.join(",", pieceNames);
    }

    private String pieceToName(Piece piece) {
        String pieceName = piece.getInitial();
        if (piece.side() == Side.WHITE) {
            return "W" + pieceName.toUpperCase();
        }
        if (piece.side() == Side.BLACK) {
            return "B" + pieceName.toUpperCase();
        }
        return pieceName;
    }

    private Map<Position, Piece> daoToBoard(String positions, String pieces) {
        Map<Position, Piece> board = new LinkedHashMap<>();

        String[] position = positions.split(",");
        String[] piece = pieces.split(",");

        for (int i = 0; i < position.length; i++) {
            board.put(Position.from(position[i]), PieceFactory.createPieceByName(piece[i]));
        }
        return board;
    }

    public boolean checkDuplicateByRoomName(String roomName) {
        String query = "SELECT count(*) FROM board WHERE roomName = ?";
        int count = this.jdbcTemplate.queryForObject(query, Integer.class, roomName);
        return count != 0;
    }

    public List<String> findRooms() {
        String query = "SELECT roomName FROM board";
        return this.jdbcTemplate.query(query,
                (resultSet, rowNum) -> resultSet.getString("roomName"));
    }

    public void deleteRoom(String roomName) {
        String query = "DELETE FROM board WHERE roomName = ?";
        this.jdbcTemplate.update(query, roomName);
    }
}
