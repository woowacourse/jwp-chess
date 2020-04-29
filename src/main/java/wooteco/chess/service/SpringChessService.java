package wooteco.chess.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.chess.dto.RoomDto;
import wooteco.chess.repository.Room;
import wooteco.chess.repository.RoomRepository;

@Service
public class SpringChessService {
	private final RoomRepository roomRepository;

	public SpringChessService(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	public List<RoomDto> findAllRooms() {
		List<Room> rooms = roomRepository.findAll();
		return rooms.stream()
				.map(Room::getRoomName)
				.map(RoomDto::new)
				.collect(Collectors.toList());
	}
}
