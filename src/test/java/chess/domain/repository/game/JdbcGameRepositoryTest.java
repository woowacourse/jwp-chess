package chess.domain.repository.game;

import chess.domain.board.BoardInitializer;
import chess.domain.board.piece.Owner;
import chess.domain.board.piece.Score;
import chess.domain.manager.Game;
import chess.domain.manager.GameStatus;
import chess.domain.manager.State;
import chess.domain.manager.TurnNumber;
import chess.domain.repository.board.JdbcSquareRepository;
import chess.domain.repository.board.SquareRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class JdbcGameRepositoryTest {

    private final GameRepository gameRepository;
    private final SquareRepository squareRepository;

    public JdbcGameRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.gameRepository = new JdbcGameRepository(jdbcTemplate);
        this.squareRepository = new JdbcSquareRepository(jdbcTemplate);
    }

    @Test
    void save() {
        //given
        Game game = new Game();

        //when
        Long gameId = gameRepository.save(game);

        //then
        assertThat(gameId).isNotNull();
    }

    @Test
    void findById() {
        //given
        Game game = new Game();
        Long gameId = gameRepository.save(game);
        squareRepository.saveBoard(gameId, game.getBoard());

        //when
        Game findGame = gameRepository.findById(gameId);

        //then
        assertThat(gameId).isEqualTo(findGame.getId());
    }

    @Test
    @Rollback(value = false)
    void update() {
        //given
        Game expectedGame = new Game();
        Long gameId = gameRepository.save(expectedGame);
        squareRepository.saveBoard(gameId, expectedGame.getBoard());
        State updateState = State.of(Owner.BLACK, TurnNumber.of(10), false);
        GameStatus updateGameStatus = GameStatus.from(Score.BISHOP_SCORE, Score.KNIGHT_SCORE);
        Game updateGame = new Game(gameId, BoardInitializer.initiateBoard(), updateState, updateGameStatus);

        //when
        gameRepository.update(updateGame);
        Game resultGame = gameRepository.findById(gameId);

        assertThat(updateGame.turnOwnerName()).isEqualTo(resultGame.turnOwnerName());
        assertThat(updateGame.turnNumberValue()).isEqualTo(resultGame.turnNumberValue());
    }
}