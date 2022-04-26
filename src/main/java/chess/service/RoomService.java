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
	private static final String ERROR_NAME_SIZE = "방 이름은 1자 이상, 16자 이하입니다.";

	private final RoomRepository roomRepository;

	public RoomService(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	@Transactional
	public RoomDto create(RoomDto roomDto) {
		String name = roomDto.getName();
		validateNameSize(name);
		validateDuplicateName(name);
		int roomId = roomRepository.save(roomDto);
		return new RoomDto(roomId, name, roomDto.getPassword());
	}

	private void validateNameSize(String name) {
		if (name.length() < NAME_MIN_SIZE || name.length() > NAME_MAX_SIZE) {
			throw new IllegalArgumentException(ERROR_NAME_SIZE);
		}
	}

	private void validateDuplicateName(String name) {
		if (roomRepository.find(name).isPresent()) {
			throw new IllegalArgumentException("해당 이름의 방이 이미 존재합니다.");
		}
	}

	public void validateId(int roomId) {
		Optional<RoomDto> roomDto = roomRepository.findById(roomId);
		if (roomDto.isEmpty()) {
			throw new IllegalArgumentException("유효하지 않은 체스방 주소입니다.");
		}

	}

	@Transactional
	public void removeById(int id) {
		roomRepository.deleteById(id);
	}

	public List<RoomDto> findAll() {
		return roomRepository.findAll();
	}
}
