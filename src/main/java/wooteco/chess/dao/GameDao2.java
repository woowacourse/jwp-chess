package wooteco.chess.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import wooteco.chess.domain.GameManager;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.piece.Color;
import wooteco.chess.dto.GameManagerDto;
import wooteco.chess.entity.ChessGame;
import wooteco.chess.repository.ChessGameRepository;

@Component
public class GameDao2 {
	private final ChessGameRepository chessGameRepository;

	public GameDao2(ChessGameRepository chessGameRepository) {
		this.chessGameRepository = chessGameRepository;
	}

	public int addGame(GameManagerDto gameManagerDto) {
		int roomNo = ThreadLocalRandom.current()
			.ints(100000, 999999)
			.findFirst()
			.orElse(0);

		chessGameRepository.save(new ChessGame(gameManagerDto.getBoard(), gameManagerDto.getTurn(), roomNo));
		return roomNo;
	}

	public GameManager findGame(int roomNo) {
		ChessGame game = chessGameRepository.findChessGameByRoomNo(roomNo);

		return Optional.ofNullable(game)
			.map(chessGame -> new GameManager(BoardFactory.of(chessGame.getBoard()), Color.of(chessGame.getTurn())))
			.orElseThrow(() -> new IllegalArgumentException("없는데"));
	}

	public void updateGame(GameManagerDto gameManagerDto, int roomNo) {
		chessGameRepository.updateChessGame(gameManagerDto.getBoard(), gameManagerDto.getTurn(), roomNo);
	}

	public void deleteGame(int roomNo) {
		chessGameRepository.deleteByRoomNo(roomNo);
	}

	public List<String> findAllRoomNo() {
		List<String> allRoomNo = new ArrayList<>();

		chessGameRepository.findAll()
			.forEach(x -> allRoomNo.add(String.valueOf(x.getRoomNo())));

		return allRoomNo;
	}
}
