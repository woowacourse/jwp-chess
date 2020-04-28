package wooteco.chess.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

import wooteco.chess.domain.GameManager;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.piece.Color;
import wooteco.chess.dto.GameManagerDto;
import wooteco.chess.repository.ChessGameRepository;

@DataJdbcTest
public class GameDaoTest {
	@Autowired
	private ChessGameRepository chessGameRepository;

	@Test
	public void addTest() {
		GameDao gameDao = new GameDao(chessGameRepository);
		GameManager gameManager = new GameManager(BoardFactory.create(), Color.WHITE);
		GameManagerDto gameManagerDto = new GameManagerDto(gameManager);

		gameDao.addGame(gameManagerDto);
	}
}