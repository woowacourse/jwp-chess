package chess.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chess.exception.UserInputException;
import chess.repository.RoomRepository;
import chess.domain.Room;
import chess.web.dto.RoomResponseDto;

@Service
@Transactional(readOnly = true)
public class RoomService {

	private static final String NOT_EXIST_ROOM = "유효하지 않은 체스방 주소입니다.";

	private final GameService gameService;
	private final RoomRepository roomRepository;

	public RoomService(GameService gameService, RoomRepository roomRepository) {
		this.gameService = gameService;
		this.roomRepository = roomRepository;
	}

	@Transactional
	public Room create(Room room) {
		String name = room.getName();
		validateDuplicateName(name);
		int roomId = roomRepository.save(room);
		return new Room(roomId, room);
	}

	public Room getRoom(int roomId) {
		return roomRepository.findById(roomId)
			.orElseThrow(() -> new UserInputException(NOT_EXIST_ROOM));
	}

	private void validateDuplicateName(String name) {
		roomRepository.findByName(name)
			.ifPresent(room -> {
				throw new UserInputException("해당 이름의 방이 이미 존재합니다.");
			});
	}

	@Transactional
	public void delete(int roomId, String password) {
		if (!getRoom(roomId).isRightPassword(password)) {
			throw new UserInputException("유효하지 않은 비밀번호입니다.");
		}
		if (!gameService.isEnd(roomId)) {
			throw new UserInputException("게임이 끝나지 않아 삭제할 수 없습니다.");
		}
		roomRepository.deleteById(roomId);
	}

	public List<RoomResponseDto> findAll() {
		return roomRepository.findAll().stream()
			.map(room ->
				new RoomResponseDto(
					room.getId(),
					room.getName(),
					gameService.isEnd((int)room.getId())
				)).collect(Collectors.toList());
	}
}
