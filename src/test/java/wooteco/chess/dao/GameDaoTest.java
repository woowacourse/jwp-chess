package wooteco.chess.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import wooteco.chess.domain.GameManager;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.piece.Color;
import wooteco.chess.domain.position.Position;
import wooteco.chess.dto.GameManagerDto;
import wooteco.chess.exceptions.RoomNotFoundException;
import wooteco.chess.repository.ChessGameRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJdbcTest
public class GameDaoTest {
	@Autowired
	private ChessGameRepository chessGameRepository;

	@BeforeEach
	public void resetDB() {
		chessGameRepository.deleteAll();
	}

	@Test
	public void addTest() {
		GameDao gameDao = new GameDao(chessGameRepository);
		GameManager gameManager = new GameManager(BoardFactory.create(), Color.WHITE);
		GameManagerDto gameManagerDto = new GameManagerDto(gameManager);

		gameDao.addGame(gameManagerDto, 123456);

		assertThat(gameDao.findGame(123456)).isNotNull();
	}

	@Test
	public void updateGameTest() {
		GameDao gameDao = new GameDao(chessGameRepository);
		GameManager gameManager = new GameManager(BoardFactory.create(), Color.WHITE);
		GameManagerDto gameManagerDto = new GameManagerDto(gameManager);

		gameDao.addGame(gameManagerDto, 123456);
		gameManager.move(Position.of("a2"), Position.of("a4"));
		gameDao.updateGame(new GameManagerDto(gameManager), 123456);

		assertThat(gameDao.findGame(123456).getBoard().isNotEmptyPosition(Position.of("a4"))).isTrue();
	}

	@Test
	public void deleteGameTest() {
		GameDao gameDao = new GameDao(chessGameRepository);
		GameManager gameManager = new GameManager(BoardFactory.create(), Color.WHITE);
		GameManagerDto gameManagerDto = new GameManagerDto(gameManager);

		gameDao.addGame(gameManagerDto, 123456);

		gameDao.deleteGame(123456);

		assertThatThrownBy(() -> gameDao.findGame(123456))
				.isInstanceOf(RoomNotFoundException.class);

	}
}