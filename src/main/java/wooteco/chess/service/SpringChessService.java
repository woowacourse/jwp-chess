package wooteco.chess.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.chess.domain.BoardConverter;
import wooteco.chess.domain.ChessGame;
import wooteco.chess.domain.FinishFlag;
import wooteco.chess.domain.Side;
import wooteco.chess.domain.position.Position;
import wooteco.chess.dto.ChessGameDto;
import wooteco.chess.dto.MoveDto;
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
		Side turn = room.getTurn();
		ChessGame chessGame = new ChessGame(BoardConverter.convertToBoard(board), turn);
		return ChessGameDto.of(roomName, chessGame);
	}

	private void validateFinish(String finishFlag) {
		if (FinishFlag.FINISH.isFinish(finishFlag)) {
			throw new IllegalArgumentException("종료된 게임입니다.");
		}
	}

	public ChessGameDto create(String roomName) {
		validateDuplicated(roomName);
		ChessGame chessGame = ChessGame.start();
		String board = BoardConverter.convertToString(chessGame.getBoard());
		roomRepository.save(new Room(roomName, board, Side.WHITE, FinishFlag.GOING.getSymbol()));
		return ChessGameDto.of(roomName, chessGame);
	}

	private void validateDuplicated(String roomName) {
		if (roomRepository.findByRoomName(roomName).isPresent()) {
			throw new IllegalArgumentException("입력한 방이 이미 존재합니다.");
		}
	}

	public Room move(MoveDto moveDto) {
		String roomName = moveDto.getRoomName();
		Room room = roomRepository.findByRoomName(roomName).orElseThrow(NoSuchElementException::new);
		ChessGame chessGame = new ChessGame(BoardConverter.convertToBoard(room.getBoard()),
				room.getTurn());

		Position source = new Position(moveDto.getSource());
		Position target = new Position(moveDto.getTarget());
		chessGame.move(source, target);

		room.update(BoardConverter.convertToString(chessGame.getBoard()), chessGame.getTurn(),
				FinishFlag.of(chessGame.isEnd()).getSymbol());
		return roomRepository.save(room);
	}

	public ChessGameDto restart(String name) {
		Room room = roomRepository.findByRoomName(name).orElseThrow(NoSuchElementException::new);
		ChessGame chessGame = ChessGame.start();
		room.update(BoardConverter.convertToString(chessGame.getBoard()), chessGame.getTurn(),
				FinishFlag.of(chessGame.isEnd()).getSymbol());
		roomRepository.save(room);
		return ChessGameDto.of(name, chessGame);
	}
}
