package wooteco.chess.domain.service;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import wooteco.chess.domain.BoardConverter;
import wooteco.chess.domain.ChessGame;
import wooteco.chess.domain.FinishFlag;
import wooteco.chess.domain.Side;
import wooteco.chess.domain.dao.RoomDao;
import wooteco.chess.domain.dto.ChessGameDto;
import wooteco.chess.domain.dto.RoomDto;
import wooteco.chess.domain.position.Position;

public class ChessGameService {
	private final RoomDao roomDao;

	public ChessGameService(RoomDao roomDao) {
		this.roomDao = roomDao;
	}

	public ChessGameDto create(String roomName) throws SQLException {
		validateDuplicated(roomName);
		ChessGame chessGame = ChessGame.start();
		roomDao.addRoom(roomName, chessGame);
		return ChessGameDto.of(roomName, chessGame);
	}

	private void validateDuplicated(String roomName) throws SQLException {
		if (isPresentRoom(roomName)) {
			throw new IllegalArgumentException("입력한 방이 이미 존재합니다.");
		}
	}

	private boolean isPresentRoom(String roomName) throws SQLException {
		return roomDao.findByRoomName(roomName, "board").isPresent();
	}

	public ChessGameDto move(String roomName, String source, String target) throws SQLException {
		ChessGameDto chessGameDto = load(roomName);
		ChessGame chessGame = new ChessGame(BoardConverter.convertToBoard(chessGameDto.getBoard()),
				Side.valueOf(chessGameDto.getTurn()));

		chessGame.move(new Position(source), new Position(target));
		roomDao.updateRoom(roomName, chessGame);
		return ChessGameDto.of(roomName, chessGame);
	}

	public List<RoomDto> findAllRooms() throws SQLException {
		List<String> rooms = roomDao.findAll();
		return rooms.stream()
				.map(RoomDto::of)
				.collect(Collectors.toList());
	}

	public ChessGameDto load(String roomName) throws SQLException {
		validatePresent(roomName);
		String finishFlag = roomDao.findByRoomName(roomName, "finish_flag")
				.orElseThrow(NoSuchElementException::new);
		validateFinish(finishFlag);

		String board = roomDao.findByRoomName(roomName, "board")
				.orElseThrow(NoSuchElementException::new);
		String turn = roomDao.findByRoomName(roomName, "turn")
				.orElseThrow(NoSuchElementException::new);
		ChessGame chessGame = new ChessGame(BoardConverter.convertToBoard(board), Side.valueOf(turn));
		return ChessGameDto.of(roomName, chessGame);
	}

	private void validatePresent(String roomName) throws SQLException {
		if (!isPresentRoom(roomName)) {
			throw new IllegalArgumentException("입력한 방이 없습니다.");
		}
	}

	private void validateFinish(String finishFlag) {
		if (FinishFlag.FINISH.isFinish(finishFlag)) {
			throw new IllegalArgumentException("종료된 게임입니다.");
		}
	}

	public ChessGameDto restart(String roomName) throws SQLException {
		ChessGame chessGame = ChessGame.start();
		roomDao.updateRoom(roomName, chessGame);
		return ChessGameDto.of(roomName, chessGame);
	}
}
