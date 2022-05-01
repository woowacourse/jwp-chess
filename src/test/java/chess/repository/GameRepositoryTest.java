package chess.repository;

import static org.assertj.core.api.Assertions.*;

import chess.dao.FakeGameDao;
import chess.domain.piece.Color;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameRepositoryTest {
    private GameRepository gameRepository;
    private int gameId;

    @BeforeEach
    void setUp() {
        gameRepository = new GameRepository(new FakeGameDao());
        gameId = gameRepository.saveGameGetKey("WHITE");
    }

    @AfterEach
    void clean() {
        gameRepository.deleteGame(gameId);
    }

    @Test
    void getColorFromStorageTest() {
        Color color = gameRepository.getColorFromStorage(gameId);
        assertThat(color.isWhite()).isTrue();
    }

    @Test
    void updateTest() {
        gameRepository.update(gameId, "BLACK");
        Color color = gameRepository.getColorFromStorage(gameId);
        assertThat(color.isWhite()).isFalse();
    }
}
