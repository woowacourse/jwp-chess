package chess.service;

import chess.domain.room.Room;
import chess.dto.request.RoomCreationRequestDto;
import chess.dto.request.RoomDeletionRequestDto;
import chess.dto.response.CurrentTurnDto;
import chess.dto.response.RoomResponseDto;
import chess.repository.RoomRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(final RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<RoomResponseDto> findAll() {
        return roomRepository.getAll();
    }

    public int createRoom(final RoomCreationRequestDto dto) {
        final Room room = new Room(dto.getRoomName(), dto.getPassword());
        return roomRepository.add(room);
    }

    public void startGame(final int roomId) {
        final Room room = roomRepository.get(roomId);
        room.startGame();
        roomRepository.update(roomId, room);
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
