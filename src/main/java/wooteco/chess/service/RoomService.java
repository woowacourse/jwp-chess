package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.dao.BoardDAO;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.room.Room;
import wooteco.chess.domain.room.RoomRepository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomService {

    private RoomRepository roomRepository;
    private BoardDAO boardDAO;

    public RoomService(final RoomRepository roomRepository) {
        boardDAO = new BoardDAO();
        this.roomRepository = roomRepository;
    }

    public List<Room> findAllRoom() {
        return roomRepository.findAll();
    }

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

    public Room createRoom(final String title) {
        Room room = new Room(title);
        return roomRepository.save(room);
    }

    public Map<String, String> findPiecesById(Long id) throws SQLException {
        return boardDAO.findAllById(id);
    }

    public void updateTurn(final Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Id와 일치하는 Room 정보가 없습니다."));
        if (room.getTurn().equals(Team.WHITE.name())) {
            room.setTurn(Team.BLACK);
            roomRepository.save(room);
            return;
        }
        room.setTurn(Team.WHITE);
        roomRepository.save(room);
    }

    public String findTurnById(final Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Id와 일치하는 Room 정보가 없습니다."));
        return room.getTurn();
    }

    public String findTitleById(final Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Id와 일치하는 Room 정보가 없습니다."));
        return room.getTitle();
    }

    public Long findCurrentMaxId() {
        return roomRepository.findCurrentMaxId();
    }

    public Map<String, String> resetRoom(final Long roomId) throws SQLException {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Id와 일치하는 Room 정보가 없습니다."));
        room.setTurn(Team.WHITE);
        roomRepository.save(room);

        boardDAO.deleteBoardById(roomId);
        return initializeBoard(roomId);
    }

    public void deleteRoom(final Long roomId) throws SQLException {
        roomRepository.deleteById(roomId);
        boardDAO.deleteBoardById(roomId);
    }
}
