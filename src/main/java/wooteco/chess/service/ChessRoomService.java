package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.db.ChessPiece;
import wooteco.chess.db.Room;
import wooteco.chess.db.RoomRepository;
import wooteco.chess.domains.board.Board;
import wooteco.chess.domains.position.Position;
import wooteco.chess.dto.GameResponseDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChessRoomService {
    private final RoomRepository roomRepository;

    public ChessRoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> findRooms() {
        return roomRepository.findAll();
    }

    public GameResponseDto addRoom(String roomName) {
        Room room = new Room(roomName);
        Board board = new Board();

        Set<ChessPiece> chessPieces = Position.stream()
                .map(position -> new ChessPiece(position.name(), board.getPieceByPosition(position).name()))
                .collect(Collectors.toSet());

        room.addChessPieces(chessPieces);
        Room savedRoom = roomRepository.save(room);

        return new GameResponseDto(savedRoom.getId(), board);
    }

    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }
}
