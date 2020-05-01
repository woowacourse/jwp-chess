package wooteco.chess.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.chess.db.entity.RoomEntity;
import wooteco.chess.db.repository.RoomRepository;
import wooteco.chess.dto.res.RoomDto;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<RoomDto> findAll() {
        List<RoomEntity> rooms = roomRepository.findEveryRooms();
        validateNoRoom(rooms);
        return rooms.stream()
            .map(RoomDto::new)
            .collect(Collectors.toList());
    }

    private void validateNoRoom(List<RoomEntity> rooms) {
        if (rooms.size() == 0) {
            throw new IllegalArgumentException("현재 진행중인 게임이 존재하지 않습니다.");
        }
    }
}
