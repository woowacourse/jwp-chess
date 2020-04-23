package wooteco.chess.dao;

import org.junit.jupiter.api.Test;

import wooteco.chess.domain.GameManager;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.piece.Color;
import wooteco.chess.dto.GameManagerDto;

public class GameDaoTest {
	@Test
	public void addTest() {
		GameDao gameDao = new GameDao();
		GameManager gameManager = new GameManager(BoardFactory.create(), Color.WHITE);
		GameManagerDto gameManagerDto = new GameManagerDto(gameManager);

		gameDao.addGame(gameManagerDto);
	}
}