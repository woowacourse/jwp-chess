package wooteco.chess.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GameRepositoryTest {

	private final String firstGameName = "first game";

	@Autowired
	private GameRepository gameRepository;

	@DisplayName("save 새 게임 시작시 게임정보 저장 테스트")
	@Test
	void save_normal_test() {
		Game game = gameRepository.save(new Game(firstGameName, true));

		assertThat(game.getId()).isEqualTo(1);
		assertThat(game.getName()).isEqualTo(firstGameName);
		assertThat(game.getCanContinue()).isEqualTo(true);
	}

	@DisplayName("게임 목록 중 진행중인 게임 조회 기능 구현")
	@Test
	void findAvailableGames_test() {
		Game firstGame = gameRepository.save(new Game(firstGameName, true));
		Game secondGame = gameRepository.save(new Game("secondGameName", true));
		Game thirdGame = gameRepository.save(new Game("thirdGameName", false));

		List<Game> games = gameRepository.findAvailableGames();

		assertThat(games).contains(firstGame, secondGame);
		assertThat(games).doesNotContain(thirdGame);
	}

	@DisplayName("이미 저장되어있는 게임에 canContinue를 false로 변경 확인")
	@Test
	void save_update_can_continue_column() {
		gameRepository.save(new Game(firstGameName, true));

		Game foundGame = gameRepository.findById(1L).get();

		Game actualGame = gameRepository.save(new Game(foundGame.getId(), foundGame.getName(), false));

		assertThat(actualGame.getCanContinue()).isEqualTo(false);
	}

	@DisplayName("게임 선택 시 해당하는 히스토리 조회")
	@Test
	void getHistories_history_from_specific_game() {
		Game firstGame = gameRepository.save(new Game(firstGameName, true));
		Game secondGame = gameRepository.save(new Game("secondGameName", true));
		History firstHistory = new History("a2", "a4");
		History secondHistory = new History("b2", "b4");
		History thirdHistory = new History("a7", "a5");
		firstGame.addHistory(firstHistory);
		secondGame.addHistory(secondHistory);
		firstGame.addHistory(thirdHistory);

		Game persistFirstGame = gameRepository.save(firstGame);
		Game persistSecondGame = gameRepository.save(secondGame);

		Set<History> firstHistories = persistFirstGame.getHistories();
		Set<History> secondHistories = persistSecondGame.getHistories();

		assertThat(firstHistories).contains(firstHistory, thirdHistory);
		assertThat(secondHistories).contains(secondHistory);
	}
}