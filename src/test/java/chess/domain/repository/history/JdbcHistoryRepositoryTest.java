package chess.domain.repository.history;

import chess.domain.board.piece.Owner;
import chess.domain.board.position.Position;
import chess.domain.history.History;
import chess.domain.manager.Game;
import chess.domain.manager.State;
import chess.domain.manager.TurnNumber;
import chess.domain.repository.game.GameRepository;
import chess.domain.repository.game.JdbcGameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class JdbcHistoryRepositoryTest {

    private final GameRepository gameRepository;
    private final HistoryRepository historyRepository;

    public JdbcHistoryRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.gameRepository = new JdbcGameRepository(jdbcTemplate);
        this.historyRepository = new JdbcHistoryRepository(jdbcTemplate);
    }

    @Test
    void save() {
        //given
        Game game = new Game();
        Long gameId = gameRepository.save(game);
        Position expectedSource = Position.of("a1");
        Position expectedTarget = Position.of("a2");
        State expectedState = State.of(Owner.BLACK, TurnNumber.of(10), false);
        History history = new History(gameId, expectedSource, expectedTarget, expectedState);

        //when
        Long historyId = historyRepository.save(history);

        //then
        assertThat(historyId).isNotNull();
    }

    @Test
    void findByGameId() {
        //given
        Game game = new Game();
        Long gameId = gameRepository.save(game);
        saveHistoriesTenItems(gameId);

        //when
        List<History> histories = historyRepository.findByGameId(gameId);

        //then
        assertThat(histories).hasSize(10);
    }

    private void saveHistoriesTenItems(Long gameId) {
        for (int i = 0; i < 10; i++) {
            Position expectedSource = Position.of("a1");
            Position expectedTarget = Position.of("a2");
            State expectedState = State.of(Owner.BLACK, TurnNumber.of(10), false);
            History history = new History(gameId, expectedSource, expectedTarget, expectedState);
            historyRepository.save(history);
        }
    }
}