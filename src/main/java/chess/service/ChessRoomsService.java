package chess.service;

import chess.repository.exceptions.DaoNoneSelectedException;
import chess.entity.RoomEntity;
import chess.repository.RoomRepository;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChessRoomsService {
	private final RoomRepository roomRepository;

	public ChessRoomsService(final RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	public List<RoomEntity> findAllRooms() {
		return Lists.newArrayList(roomRepository.findAll());
	}

	public RoomEntity findRoomByRoomName(final String roomName) {
		return roomRepository.findByRoomName(roomName).orElseThrow(DaoNoneSelectedException::new);
	}

	public void addRoomByRoomName(final String roomName) {
		roomRepository.saveByRoomName(roomName);
	}

	public void deleteRoomByRoomName(final String roomName) {
		roomRepository.deleteByRoomName(roomName);
	}
}
