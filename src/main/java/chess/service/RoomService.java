package chess.service;

import chess.dto.CreateRoomDto;
import chess.dto.DeleteRoomDto;
import chess.dto.RoomsDto;
import chess.model.repository.RoomEntity;
import chess.model.repository.RoomRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public RoomsDto getUsedRooms() {
        Map<Integer, String> rooms = new HashMap<>();
        for (RoomEntity roomEntity : roomRepository.findAllByUsedYNEquals("Y")) {
            rooms.put(roomEntity.getId(), roomEntity.getName());
        }
        return new RoomsDto(rooms);
    }

    public RoomEntity addRoom(CreateRoomDto createRoomDto) {
        RoomEntity roomEntity = new RoomEntity(createRoomDto.getRoomName(),
            createRoomDto.getRoomPassword());
        return roomRepository.save(roomEntity);
    }

    public void deleteRoom(DeleteRoomDto deleteRoomDto) {
        RoomEntity roomEntity = roomRepository.findById(deleteRoomDto.getRoomId())
            .orElseThrow(IllegalArgumentException::new);
        roomEntity.setUsedYN("N");
        roomRepository.save(roomEntity);
    }
}
