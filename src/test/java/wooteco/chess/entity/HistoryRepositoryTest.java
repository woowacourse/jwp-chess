package wooteco.chess.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class HistoryRepositoryTest {

	final String firstGameName = "first game";
	final String firstUuid = "1q2w3e4r5t6y7u8i9o0p1q2w32e4t5y5u";

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private GameRepository gameRepository;

	@DisplayName("특정 게임에 해당되는 히스토리 생성 테스트")
	@Test
	void save_history_from_specific_game() {
		Game game = gameRepository.save(new Game(firstGameName, firstUuid, true));
		History history = new History("a2", "a4", game);
		historyRepository.save(history);

		History persistHistory = historyRepository.findById(history.getId())
				.orElseThrow(RuntimeException::new);

		assertThat(persistHistory).isNotNull();
		assertThat(persistHistory.getId()).isEqualTo(1);
		assertThat(persistHistory.getStart()).isEqualTo(history.getStart());
		assertThat(persistHistory.getEnd()).isEqualTo(history.getEnd());
	}

}