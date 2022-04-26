package chess.service;

import chess.repository.RoomRepository;
import chess.web.dto.RoomDto;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RoomService {

	private static final int NAME_MIN_SIZE = 1;
	private static final int NAME_MAX_SIZE = 16;
	private static final String NOT_EXIST_ROOM = "유효하지 않은 체스방 주소입니다.";

	private final RoomRepository roomRepository;

	public RoomService(RoomRepository roomRepository) {
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
		validatePassword(roomDto);
	}

	private void validateNameSize(String name) {
		if (name.length() < NAME_MIN_SIZE || name.length() > NAME_MAX_SIZE) {
			throw new IllegalArgumentException("방 이름은 1자 이상, 16자 이하입니다.");
		}
	}

	private void validateDuplicateName(String name) {
		if (roomRepository.find(name).isPresent()) {
			throw new IllegalArgumentException("해당 이름의 방이 이미 존재합니다.");
		}
	}

	private void validatePassword(RoomDto roomDto) {
		String password = roomDto.getPassword();
		if (password.isEmpty() || password.isBlank()) {
			throw new IllegalArgumentException("비밀번호를 입력하세요");
		}
	}

	public void validateId(int roomId) {
		Optional<RoomDto> roomDto = roomRepository.findById(roomId);
		if (roomDto.isEmpty()) {
			throw new IllegalArgumentException(NOT_EXIST_ROOM);
		}

	}

	@Transactional
	public void delete(int id, String password) {
		if (!getPassword(id).equals(password)) {
			throw new IllegalArgumentException("유효하지 않은 비밀번호입니다.");
		}
		roomRepository.deleteById(id);
	}

	private String getPassword(int id) {
		Optional<RoomDto> findRoom = roomRepository.findById(id);
		if (findRoom.isPresent()) {
			return findRoom.get()
				.getPassword();
		}
		throw new IllegalArgumentException(NOT_EXIST_ROOM);
	}

	public List<RoomDto> findAll() {
		return roomRepository.findAll();
	}
}
