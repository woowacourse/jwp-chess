package wooteco.chess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wooteco.chess.domain.Color;
import wooteco.chess.dto.RoomResponseDto;
import wooteco.chess.repository.RoomRepository;
import wooteco.chess.repository.entity.RoomEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpringRoomService {

    @Autowired
    private RoomRepository roomRepository;

    public void addRoom(String roomName) throws SQLException {
        roomRepository.save(new RoomEntity(roomName, Color.WHITE.name()));
    }

    public void removeRoom(int roomId) throws SQLException {
        roomRepository.deleteById(roomId);
    }

    public List<RoomResponseDto> findAllRoom() throws SQLException {
        List<RoomResponseDto> rooms = new ArrayList<>();
        for (final RoomEntity roomEntity : roomRepository.findAll()) {
            rooms.add(RoomResponseDto.of(roomEntity));
        }
        return rooms;
    }
}
