package wooteco.chess.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
@Transactional
class GameRepositoryTest {

	@Autowired
	private GameRepository gameRepository;

	@DisplayName("save 새 게임 시작시 게임정보 저장 테스트")
	@Test
	void save_normal_test() {
		String gameName = "first game";
		String uuid = "1q2w3e4r5t6y7u8i9o0p1q2w32e4t5y5u";

		Game game = gameRepository.save(new Game(gameName, uuid, true));

		assertThat(game.getId()).isEqualTo(1);
		assertThat(game.getName()).isEqualTo(gameName);
		assertThat(game.getUuid()).isEqualTo(uuid);
		assertThat(game.getCanContinue()).isEqualTo(true);
	}
}