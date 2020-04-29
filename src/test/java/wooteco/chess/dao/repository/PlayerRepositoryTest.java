package wooteco.chess.dao.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import wooteco.chess.db.entity.PlayerEntity;
import wooteco.chess.db.repository.PlayerRepository;

@SpringBootTest
class PlayerRepositoryTest {

	@Autowired
	private PlayerRepository playerRepository;

	@Test
	@DisplayName("정상적으로 플레이어가 생성됩니다.")
	void create() {
		PlayerEntity playerEntity = new PlayerEntity("pobi", "1234!", "white");
		PlayerEntity actual = playerRepository.save(playerEntity);
		assertThat(actual).isNotNull();
		assertThat(actual.getName()).isEqualTo("pobi");
	}

	@Test
	void findByID() {
		PlayerEntity playerEntity = new PlayerEntity("pobi", "1234!", "black");
		PlayerEntity actual = playerRepository.save(playerEntity);

		Optional<PlayerEntity> findPlayer = playerRepository.findById(actual.getId());
		assertThat(findPlayer.get().getTeam()).isEqualTo("BLACK");
	}
}