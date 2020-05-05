package wooteco.chess.service;

import static org.assertj.core.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import wooteco.chess.AbstractChessApplicationTest;
import wooteco.chess.exceptions.GameNotFoundException;

class SpringChessServiceTest extends AbstractChessApplicationTest {

    @Autowired
    private ChessService chessService;

    @Value("${chess.test.data.sample1}")
    String gameOneId;

    @Value("${chess.test.data.sample2}")
    String gameTwoId;

    @DisplayName("move가 게임별로 개별적으로 작동하는지")
    @Test
    void moveEachWorksWithinDifferentGames() throws SQLException {
        // when
        chessService.moveIfMovable(gameOneId, "b2", "b4");
        chessService.moveIfMovable(gameTwoId, "a2", "a4");

        // then
        assertThat(chessService.moveIfMovable(gameOneId, "a2", "a4")).isTrue();
        assertThat(chessService.moveIfMovable(gameTwoId, "b2", "b4")).isTrue();
    }

    @DisplayName("finish한 게임만 지워지는지")
    @Test
    void finishEachWorksWithinDifferentGames() throws SQLException {
        // when
        chessService.finishGameById(gameOneId);

        // then
        assertThatThrownBy(() -> chessService.findGameById(gameOneId))
            .isInstanceOf(GameNotFoundException.class);
        assertThat(chessService.findGameById(gameTwoId)).isNotNull();
    }

    @DisplayName("reset한 게임 초기화되는지")
    @Test
    void resetEachWorksWithinDifferentGames() throws SQLException {
        // given
        chessService.moveIfMovable(gameOneId, "b2", "b4");
        assertThat(chessService.moveIfMovable(gameOneId, "b2", "b4")).isFalse();

        // when
        chessService.resetGameById(gameOneId);

        // then
        assertThat(chessService.moveIfMovable(gameOneId, "b2", "b4")).isTrue();
        assertThat(chessService.moveIfMovable(gameTwoId, "b2", "b4")).isTrue();
    }

    @DisplayName("turn이 개별적으로 작동하는지")
    @Test
    void isWhiteTurnEachWorksWithinDifferentGames() throws SQLException {
        // when
        chessService.moveIfMovable(gameOneId, "b2", "b4");

        // then
        assertThat(chessService.isWhiteTurn(gameOneId)).isFalse();
        assertThat(chessService.isWhiteTurn(gameTwoId)).isTrue();
    }
}
