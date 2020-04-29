package wooteco.chess.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.chess.domain.BoardConverter;
import wooteco.chess.domain.ChessGame;
import wooteco.chess.domain.FinishFlag;
import wooteco.chess.domain.Side;
import wooteco.chess.dto.ChessGameDto;
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

	public ChessGameDto load(String roomName) {
		Room room = roomRepository.findByRoomName(roomName).orElseThrow(NoSuchElementException::new);
		validateFinish(room.getFinishFlag());

		String board = room.getBoard();
		String turn = room.getTurn();
		ChessGame chessGame = new ChessGame(BoardConverter.convertToBoard(board), Side.valueOf(turn));
		return ChessGameDto.of(roomName, chessGame);
	}

	private void validateFinish(String finishFlag) {
		if (FinishFlag.FINISH.isFinish(finishFlag)) {
			throw new IllegalArgumentException("종료된 게임입니다.");
		}
	}
}
