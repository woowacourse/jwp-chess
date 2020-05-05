package wooteco.chess.domain.game.repository;

import static org.assertj.core.api.Assertions.*;
import static wooteco.chess.domain.game.Ready.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;

import wooteco.chess.domain.game.Game;
import wooteco.chess.domain.position.Position;

@Import(wooteco.chess.domain.game.repository.config.GameIdProvider.class)
@DataJdbcTest
class GameRepositoryTest {
	@Autowired
	GameRepository gameRepository;

	@DisplayName("findAll Ids test")
	@Test
	void findAllIdsTest() {
		GameEntity newGame = GameEntity.of(READY_INSTANCE);
		GameEntity newGame2 = GameEntity.of(READY_INSTANCE);
		GameEntity newGame3 = GameEntity.of(READY_INSTANCE);

		List<GameEntity> newEntities = new ArrayList<>(Arrays.asList(newGame, newGame2, newGame3));
		Iterable<GameEntity> gameEntities = gameRepository.saveAll(newEntities);

		List<String> savedId = new ArrayList<>();
		gameEntities.forEach(entity -> savedId.add(entity.getId()));
		assertThat(savedId).containsExactlyInAnyOrderElementsOf(gameRepository.findAllIds());
	}

	@DisplayName("find Game Entity By Id and test matching id")
	@Test
	void findGameEntityTest() {
		GameEntity newSavedEntity = gameRepository.save(GameEntity.of(READY_INSTANCE));
		GameEntity persistedEntity = gameRepository.findById(newSavedEntity.getId())
			.orElseThrow(NoSuchElementException::new);

		assertThat(persistedEntity.getId()).isEqualTo(newSavedEntity.getId());
	}

	@DisplayName("insert game test")
	@Test
	void saveGameTest() {
		GameEntity newGame = GameEntity.of(READY_INSTANCE);
		GameEntity newGame2 = GameEntity.of(READY_INSTANCE);
		GameEntity newGame3 = GameEntity.of(READY_INSTANCE);

		List<GameEntity> newEntities = new ArrayList<>(Arrays.asList(newGame, newGame2, newGame3));
		gameRepository.saveAll(newEntities);

		assertThat(gameRepository.findAll()).hasSize(newEntities.size());
	}

	@DisplayName("update test")
	@Test
	void updateGameTest() {
		GameEntity newSavedEntity = gameRepository.save(GameEntity.of(READY_INSTANCE));
		Game game = newSavedEntity.toDomain();

		Game start = game.start();
		start.move(Position.of("b2"), Position.of("b4"));
		newSavedEntity.update(start);
		gameRepository.save(newSavedEntity);

		GameEntity persistedEntity = gameRepository.findById(newSavedEntity.getId())
			.orElseThrow(NoSuchElementException::new);

		assertThat(newSavedEntity).isEqualTo(persistedEntity);
	}
}