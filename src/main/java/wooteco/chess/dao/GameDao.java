package wooteco.chess.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import wooteco.chess.domain.GameManager;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.piece.Color;
import wooteco.chess.dto.GameManagerDto;
import wooteco.chess.entity.ChessGame;
import wooteco.chess.exceptions.RoomNotFoundException;
import wooteco.chess.repository.ChessGameRepository;

@Component
public class GameDao {
	private final ChessGameRepository chessGameRepository;

	public GameDao(ChessGameRepository chessGameRepository) {
		this.chessGameRepository = chessGameRepository;
	}

	public int addGame(GameManagerDto gameManagerDto, int roomNo) {
		chessGameRepository.save(new ChessGame(gameManagerDto.getBoard(), gameManagerDto.getTurn(), roomNo));
		return roomNo;
	}

	public GameManager findGame(int roomNo) {
		return chessGameRepository.findChessGameByRoomNo(roomNo)
			.map(chessGame -> new GameManager(BoardFactory.of(chessGame.getBoard()), Color.of(chessGame.getTurn())))
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
