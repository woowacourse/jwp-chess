package chess.service;

import chess.model.dto.CreateRoomDto;
import chess.model.dto.DeleteRoomDto;
import chess.model.dto.RoomsDto;
import chess.model.repository.Room;
import chess.model.repository.RoomDao;
import chess.model.repository.RoomRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private static final RoomDao ROOM_DAO = RoomDao.getInstance();

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public RoomsDto getUsedRooms() {
        Map<Integer, String> rooms = new HashMap<>();
        for (Room room : roomRepository.findAllByUsedYNEquals("Y")) {
            rooms.put(room.getId(), room.getName());
        }
        return new RoomsDto(rooms);
    }

    public void addRoom(CreateRoomDto createRoomDto) {
        Room room = new Room(createRoomDto.getRoomName(), createRoomDto.getRoomPassword());
        roomRepository.save(room);
        ROOM_DAO.create(createRoomDto.getRoomName(), createRoomDto.getRoomPassword());
    }

    public void deleteRoom(DeleteRoomDto deleteRoomDto) {
        Room room = roomRepository.findById(deleteRoomDto.getRoomId())
            .orElseThrow(IllegalArgumentException::new);
        room.setUsedYN("N");
        roomRepository.save(room);
        ROOM_DAO.updateUsedN(deleteRoomDto.getRoomId());
    }
}
