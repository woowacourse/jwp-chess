package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import chess.domain.Position;
import chess.domain.game.Game;

@SpringBootTest
@Transactional
class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    @DisplayName("데이터를 저장할 수 있어야 한다.")
    @Test
    void save() {
        final Game game = Game.initializeGame();

        final Long gameId = gameRepository.save(game).getId();
        assertThat(gameId).isNotEqualTo(0L);
        gameRepository.remove(gameId);
    }

    @DisplayName("데이터를 조회할 수 있어야 한다.")
    @Test
    void findById() {
        final Game expectedGame = Game.initializeGame();

        final Long gameId = gameRepository.save(expectedGame).getId();
        final Game game = gameRepository.findById(gameId);

        assertAll(() -> {
            assertThat(game.getColorOfCurrentTurn()).isEqualTo(expectedGame.getColorOfCurrentTurn());
            assertThat(game.isFinished()).isEqualTo(expectedGame.isFinished());
        });
        gameRepository.remove(gameId);
    }

    @DisplayName("데이터를 수정할 수 있어야 한다.")
    @Test
    void update() {
        final Game game = Game.initializeGame();

        final Game expectedGame = gameRepository.save(game);

        expectedGame.movePiece(Position.from("a2"), Position.from("a4"));
        expectedGame.end();

        final Game updatedGame = gameRepository.update(expectedGame);
        assertAll(() -> {
            assertThat(updatedGame.getColorOfCurrentTurn()).isEqualTo(expectedGame.getColorOfCurrentTurn());
            assertThat(updatedGame.isFinished()).isEqualTo(expectedGame.isFinished());
        });
        gameRepository.remove(updatedGame.getId());
    }
}