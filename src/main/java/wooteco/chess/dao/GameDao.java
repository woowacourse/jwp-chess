package wooteco.chess.dao;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import wooteco.chess.domain.GameManager;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.piece.Color;
import wooteco.chess.dto.GameManagerDto;
import wooteco.chess.entity.ChessGame;
import wooteco.chess.exceptions.RoomNotFoundException;
import wooteco.chess.repository.ChessGameRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Component
public class GameDao {
	private static final int MIN_ROOM_NUMBER = 100_000;
	private static final int MAX_ROOM_NUMBER = 999_999;
	private final ChessGameRepository chessGameRepository;

	public GameDao(ChessGameRepository chessGameRepository) {
		this.chessGameRepository = chessGameRepository;
	}

	public int addGame(GameManagerDto gameManagerDto) {
		int roomNo = ThreadLocalRandom.current()
			.ints(MIN_ROOM_NUMBER, MAX_ROOM_NUMBER)
			.findFirst()
			.orElse(0);

		chessGameRepository.save(new ChessGame(gameManagerDto.getBoard(), gameManagerDto.getTurn(), roomNo));
		return roomNo;
	}

	public GameManager findGame(int roomNo) {
		Optional<ChessGame> game = chessGameRepository.findChessGameByRoomNo(roomNo);

		return game.map(
			chessGame -> new GameManager(BoardFactory.of(chessGame.getBoard()), Color.of(chessGame.getTurn())))
			.orElseThrow(RoomNotFoundException::new);
	}

	public void updateGame(GameManagerDto gameManagerDto, int roomNo) {
		ChessGame chessGame = chessGameRepository.findChessGameByRoomNo(roomNo)
				.orElseThrow(RoomNotFoundException::new);
		chessGame.setBoard(gameManagerDto.getBoard());
		chessGame.setTurn(gameManagerDto.getTurn());
		chessGameRepository.save(chessGame);
	}

	public void deleteGame(int roomNo) {
		ChessGame chessGame = chessGameRepository.findChessGameByRoomNo(roomNo)
				.orElseThrow(RoomNotFoundException::new);
		chessGameRepository.delete(chessGame);
	}

	public List<String> findAllRoomNo() {
		return Lists.newArrayList(chessGameRepository.findAll())
			.stream()
			.map(ChessGame::getRoomNo)
			.map(String::valueOf)
			.collect(Collectors.toList());
	}
}
