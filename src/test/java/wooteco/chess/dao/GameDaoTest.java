package wooteco.chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.chess.dto.GameDto;

class GameDaoTest {
	private GameDao gameDao = GameDao.getInstance();

	@AfterEach
	void tearDown() {
		gameDao.deleteAll();
	}

	@DisplayName("save 기능 테스트")
	@Test
	void save() {
		GameDto gameDto = new GameDto("black");
		gameDto = gameDao.save(gameDto);

		assertThat(gameDto.getId()).isNotNull();
	}

	@DisplayName("update 기능 테스트")
	@Test
	void update() {
		//given
		GameDto gameDto = new GameDto("white");
		gameDto = gameDao.save(gameDto);

		//when
		GameDto updateDto = new GameDto(gameDto.getId(), "black");
		gameDao.update(updateDto);

		//then
		GameDto actual = gameDao.findById(gameDto.getId()).orElseThrow(NoSuchElementException::new);
		assertThat(actual.getTurn()).isEqualTo(updateDto.getTurn());
	}
}
