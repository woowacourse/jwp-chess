package wooteco.chess.dao.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import wooteco.chess.dto.PlayerDto;

@SpringBootTest
class PlayerRepositoryTest {

	@Autowired
	private PlayerRepository playerRepository;

	@Test
	@DisplayName("정상적으로 플레이어가 생성됩니다.")
	void create() {
		PlayerDto playerDto = new PlayerDto("pobi", "1234!", "white");
		PlayerDto actual = playerRepository.save(playerDto);
		assertThat(actual).isNotNull();
		assertThat(actual.getName()).isEqualTo("pobi");
	}

	@Test
	void findByID() {
		PlayerDto playerDto = new PlayerDto("pobi", "1234!", "black");
		PlayerDto actual = playerRepository.save(playerDto);

		Optional<PlayerDto> findPlayer = playerRepository.findById(actual.getId());
		assertThat(findPlayer.get().getTeam()).isEqualTo("black");
	}
}