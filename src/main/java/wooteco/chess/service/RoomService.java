package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardEntity;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.board.BoardRepository;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.room.Room;
import wooteco.chess.domain.room.RoomRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomService {

    private RoomRepository roomRepository;
    private BoardRepository boardRepository;

    public RoomService(final RoomRepository roomRepository, final BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
        this.roomRepository = roomRepository;
    }

    public List<Room> findAllRoom() {
        return roomRepository.findAll();
    }

    public Map<String, String> initializeBoard(Long roomId) {
        Board board = BoardFactory.initializeBoard();
        Map<Position, Piece> rawBoard = board.getBoard();

        Map<String, String> boardDTO = new HashMap<>();
        for (Position position : rawBoard.keySet()) {
            BoardEntity boardEntity = new BoardEntity(roomId, position.toString(), rawBoard.get(position).getName());
            boardRepository.save(boardEntity);
            boardDTO.put(position.toString(), rawBoard.get(position).getName());
        }
        return boardDTO;
    }

    public Room createRoom(final String title) {
        Room room = new Room(title);
        return roomRepository.save(room);
    }

    public Map<String, String> findPiecesById(Long id) {
        List<BoardEntity> board = boardRepository.findByRoomId(id);
        Map<String, String> boardDto = new HashMap<>();
        for (BoardEntity boardEntity : board) {
            boardDto.put(boardEntity.getPosition(), boardEntity.getPiece());
        }
        return boardDto;
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

    public Map<String, String> resetRoom(final Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Id와 일치하는 Room 정보가 없습니다."));
        room.setTurn(Team.WHITE);
        roomRepository.save(room);

        boardRepository.deleteByRoomId(roomId);
        return initializeBoard(roomId);
    }

    public void deleteRoom(final Long roomId) {
        roomRepository.deleteById(roomId);
        boardRepository.deleteByRoomId(roomId);
    }
}
