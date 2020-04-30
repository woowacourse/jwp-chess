package chess.service;

import chess.dto.RoomDto;
import chess.repository.exceptions.DaoNoneSelectedException;
import chess.entity.RoomEntity;
import chess.repository.RoomRepository;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChessRoomsService {
	private final RoomRepository roomRepository;

	public ChessRoomsService(final RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	public List<RoomDto> findAllRooms() {
		List<RoomEntity> roomEntities = Lists.newArrayList(roomRepository.findAll());
		return roomEntities.stream()
				.map(roomEntity -> new RoomDto(roomEntity.getRoomName()))
				.collect(Collectors.toList());
	}

	public int findRoomIdByRoomName(final String roomName) {
		return roomRepository.findByRoomName(roomName)
				.orElseThrow(DaoNoneSelectedException::new)
				.getId();
	}

	public void addRoomByRoomName(final String roomName) {
		roomRepository.saveByRoomName(roomName);
	}

	public void deleteRoomByRoomName(final String roomName) {
		roomRepository.deleteByRoomName(roomName);
	}
}
