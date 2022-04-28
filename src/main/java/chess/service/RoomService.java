package chess.service;

import chess.domain.Room;
import chess.repository.RoomRepository;
import chess.web.dto.RoomDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public RoomDto create(Room room) {
        int id = roomRepository.save(room.getName(), room.getPassword());
        return roomRepository.findById(id).get();
    }

    public List<Map<String, String>> findRooms() {
        List<RoomDto> data = roomRepository.findAll();
        return data.stream()
                .map(room -> Map.of("id", String.valueOf(room.getId()), "name", room.getName()))
                .collect(Collectors.toList());
    }

    public void validateId(int roomId) {
        Optional<RoomDto> roomDto = roomRepository.findById(roomId);
        if (roomDto.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 체스방 주소입니다.");
        }

    }
}
