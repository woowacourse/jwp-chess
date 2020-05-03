package wooteco.chess.service;

import static java.util.stream.Collectors.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.stereotype.Service;

import wooteco.chess.database.GameRoomRepository;
import wooteco.chess.domain.chessBoard.ChessBoard;
import wooteco.chess.domain.chessBoard.ChessBoardInitializer;
import wooteco.chess.domain.chessGame.ChessCommand;
import wooteco.chess.domain.chessGame.ChessGame;
import wooteco.chess.entity.GameHistory;
import wooteco.chess.entity.GameRoom;
import wooteco.chess.service.dto.ChessGameDto;

@Service
public class ChessService {

	private static final String MOVE_COMMAND = "move";

	private final GameRoomRepository gameRoomRepository;

	public ChessService(final GameRoomRepository gameRoomRepository) {
		this.gameRoomRepository = gameRoomRepository;
	}

	public ChessGameDto loadChessGameByName(String name) {
		GameRoom gameRoom = gameRoomRepository.findByName(name);
		return ChessGameDto.of(gameRoom.getId(), initChessGameOf(gameRoom));
	}

	private ChessGame initChessGameOf(final GameRoom gameRoom) {
		final ChessBoard chessBoard = new ChessBoard(ChessBoardInitializer.create());
		final ChessGame chessGame = ChessGame.from(chessBoard);

		gameRoom.getGameHistories().stream()
			.map(GameHistory::generateMoveCommand)
			.forEach(chessGame::move);

		return chessGame;
	}

	public ChessGameDto playChessGame(final Long gameId, final String sourcePosition, final String targetPosition) {
		Objects.requireNonNull(sourcePosition, "소스 위치가 null입니다.");
		Objects.requireNonNull(targetPosition, "타겟 위치가 null입니다.");
		return moveChessPiece(gameId, sourcePosition, targetPosition);
	}

	private ChessGameDto moveChessPiece(final Long gameId, final String sourcePosition, final String targetPosition) {
		GameRoom gameRoom = gameRoomRepository.findById(gameId)
			.orElseThrow(() -> new NoSuchElementException("게임이 존재하지 않습니다."));

		final ChessGame chessGame = initChessGameOf(gameRoom);
		final ChessCommand chessCommand = ChessCommand.of(Arrays.asList(MOVE_COMMAND, sourcePosition, targetPosition));

		chessGame.move(chessCommand);
		gameRoom.addGameHistory(new GameHistory(sourcePosition, targetPosition, gameId));

		gameRoomRepository.save(gameRoom);

		return ChessGameDto.of(gameRoom.getId(), chessGame);
	}

	public ChessGameDto createChessGame(final String name) {
		GameRoom savedGameRoom = gameRoomRepository.save(new GameRoom(name));
		return ChessGameDto.of(savedGameRoom.getId(), initChessGameOf(savedGameRoom));
	}

	public ChessGameDto endChessGame(final Long gameId) {
		GameRoom gameRoom = gameRoomRepository.findById(gameId)
			.orElseThrow(() -> new NoSuchElementException("게임이 존재하지 않습니다."));
		final ChessGame chessGame = initChessGameOf(gameRoom);

		chessGame.end();
		gameRoomRepository.save(new GameRoom(gameRoom, true));
		return ChessGameDto.of(gameRoom.getId(), chessGame);
	}

	public boolean isEndGame(final Long gameId) {
		GameRoom gameRoom = gameRoomRepository.findById(gameId)
			.orElseThrow(() -> new NoSuchElementException("게임이 존재하지 않습니다."));
		return gameRoom.getState();
	}

	public List<String> showAllGames() {
		List<GameRoom> gameRooms = gameRoomRepository.findAll();

		return gameRooms.stream()
			.map(GameRoom::getName)
			.collect(toList());
	}

}
