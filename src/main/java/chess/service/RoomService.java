package chess.service;

import chess.domain.room.Room;
import chess.dto.request.RoomCreationRequestDto;
import chess.dto.request.RoomDeletionRequestDto;
import chess.dto.response.CurrentTurnDto;
import chess.dto.response.RoomPageDto;
import chess.repository.ChessGameRepository;
import chess.repository.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final ChessGameRepository chessGameRepository;

    public RoomService(final RoomRepository roomRepository, final ChessGameRepository chessGameRepository) {
        this.roomRepository = roomRepository;
        this.chessGameRepository = chessGameRepository;
    }

    public RoomPageDto findAll(final int page, final int size) {
        return roomRepository.getAll(page, size);
    }

    public int createRoom(final RoomCreationRequestDto dto) {
        final Room room = new Room(dto.getRoomName(), dto.getPassword());
        return roomRepository.add(room);
    }

    public void startGame(final int roomId) {
        final Room room = roomRepository.get(roomId);
        room.startGame();
        roomRepository.update(roomId, room);
        chessGameRepository.add(roomId, room.getChessGame());
    }

    public void deleteRoom(final RoomDeletionRequestDto dto) {
        final Room room = roomRepository.get(dto.getRoomId());
        if (room.canRemove(dto.getPassword())) {
            roomRepository.remove(dto.getRoomId());
        }
    }

    public CurrentTurnDto findCurrentTurn(final int roomId) {
        final Room room = roomRepository.get(roomId);
        return CurrentTurnDto.of(room.getName(), room.getCurrentTurn());
    }
}
