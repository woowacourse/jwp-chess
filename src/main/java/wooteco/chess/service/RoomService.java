package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.dao.BoardDAO;
import wooteco.chess.dao.RoomDAO;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.room.Room;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomService {

    private RoomDAO roomDAO;
    private BoardDAO boardDAO;

    public RoomService() throws SQLException {
        roomDAO = new RoomDAO();
        boardDAO = new BoardDAO();
    }

    // Get("/")
    public List<Room> findAllRoom() throws SQLException {
        return roomDAO.findAllRoom();
    }

    // Post("/room")
    public Map<String, String> initializeBoard(Long roomId) throws SQLException {
        Board board = BoardFactory.initializeBoard();
        Map<Position, Piece> rawBoard = board.getBoard();

        Map<String, String> boardDTO = new HashMap<>();
        for (Position position : rawBoard.keySet()) {
            boardDAO.insertPiece(roomId, board, position);
            boardDTO.put(position.toString(), rawBoard.get(position).getName());
        }
        return boardDTO;
    }

    //Post("/room")
    public Room createRoom(final Long id, final String title) throws SQLException {
        Room room = new Room(id, title);
        roomDAO.insertRoom(id, title, room.getTurn());
        return room;
    }

    // Get("/room/{room_id}")
    public Map<String, String> findPiecesById(Long id) throws SQLException {
        return boardDAO.findAllById(id);
    }

    public void updateTurn(final Long roomId) throws SQLException {
        if (roomDAO.findTurn(roomId) == Team.WHITE) {
            roomDAO.updateTurn(roomId, Team.BLACK);
            return;
        }
        roomDAO.updateTurn(roomId, Team.WHITE);
    }

    public Team getCurrentTurn(final Long roomId) throws SQLException {
        return roomDAO.findTurn(roomId);
    }

    public Long findCurrentMaxId() throws SQLException {
        return roomDAO.findCurrentMaxId();
    }

    public void deleteRoom(final Long roomId) throws SQLException {
        roomDAO.deleteRoomById(roomId);
        boardDAO.deleteBoardById(roomId);
    }
}
