package chess.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chess.exception.UserInputException;
import chess.repository.RoomRepository;
import chess.web.dto.RoomDto;

@Service
@Transactional(readOnly = true)
public class RoomService {

	private static final int NAME_MIN_SIZE = 1;
	private static final int NAME_MAX_SIZE = 16;
	private static final String NOT_EXIST_ROOM = "유효하지 않은 체스방 주소입니다.";

	private final GameService gameService;
	private final RoomRepository roomRepository;

	public RoomService(GameService gameService, RoomRepository roomRepository) {
		this.gameService = gameService;
		this.roomRepository = roomRepository;
	}

	@Transactional
	public RoomDto create(RoomDto roomDto) {
		String name = roomDto.getName();
		validateRoom(roomDto);
		int roomId = roomRepository.save(roomDto);
		return new RoomDto(roomId, name, roomDto.getPassword());
	}

	private void validateRoom(RoomDto roomDto) {
		validateNameSize(roomDto.getName());
		validateDuplicateName(roomDto.getName());
		validatePassword(roomDto.getPassword());
	}

	private void validateNameSize(String name) {
		if (name.length() < NAME_MIN_SIZE || name.length() > NAME_MAX_SIZE) {
			throw new UserInputException("방 이름은 1자 이상, 16자 이하입니다.");
		}
	}

	private void validateDuplicateName(String name) {
		if (roomRepository.findByName(name).isPresent()) {
			throw new UserInputException("해당 이름의 방이 이미 존재합니다.");
		}
	}

	private void validatePassword(String password) {
		if (password.isEmpty() || password.isBlank()) {
			throw new UserInputException("비밀번호를 입력하세요");
		}
	}

	public void validateId(int roomId) {
		Optional<RoomDto> roomDto = roomRepository.findById(roomId);
		if (roomDto.isEmpty()) {
			throw new UserInputException(NOT_EXIST_ROOM);
		}

	}

	@Transactional
	public void delete(int roomId, String password) {
		validatePassword(password);
		if (!getPassword(roomId).equals(password)) {
			throw new UserInputException("유효하지 않은 비밀번호입니다.");
		}
		if (!gameService.isEnd(roomId)) {
			throw new UserInputException("게임이 끝나지 않아 삭제할 수 없습니다.");
		}
		roomRepository.deleteById(roomId);
	}

	private String getPassword(int id) {
		Optional<RoomDto> findRoom = roomRepository.findById(id);
		if (findRoom.isPresent()) {
			return findRoom.get()
				.getPassword();
		}
		throw new UserInputException(NOT_EXIST_ROOM);
	}

	public List<RoomDto> findAll() {
		return roomRepository.findAll();
	}
}
