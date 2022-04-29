package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import chess.domain.Position;
import chess.domain.game.Game;
import chess.repository.dao.GameDao;
import chess.repository.dao.PlayerDao;

@JdbcTest
class ChessRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ChessRepository chessRepository;

    @BeforeEach
    void setUp() {
        final GameDao gameDao = new GameDao(jdbcTemplate);
        final PlayerDao playerDao = new PlayerDao(jdbcTemplate);
        this.chessRepository = new ChessRepository(gameDao, playerDao);
    }

    @DisplayName("데이터를 저장할 수 있어야 한다.")
    @Test
    void save() {
        final Game game = Game.initializeGame("title", "password");

        final Long gameId = chessRepository.save(game).getId();
        assertThat(gameId).isNotEqualTo(0L);
        chessRepository.remove(gameId);
    }

    @DisplayName("데이터를 조회할 수 있어야 한다.")
    @Test
    void findById() {
        final Game expectedGame = Game.initializeGame("title", "password");

        final Long gameId = chessRepository.save(expectedGame).getId();
        final Game game = chessRepository.findById(gameId);

        assertAll(() -> {
            assertThat(game.getColorOfCurrentTurn()).isEqualTo(expectedGame.getColorOfCurrentTurn());
            assertThat(game.isFinished()).isEqualTo(expectedGame.isFinished());
        });
        chessRepository.remove(gameId);
    }

    @DisplayName("데이터를 수정할 수 있어야 한다.")
    @Test
    void update() {
        final Game game = Game.initializeGame("title", "password");

        final Game expectedGame = chessRepository.save(game);

        expectedGame.movePiece(Position.from("a2"), Position.from("a4"));
        expectedGame.end();

        final Game updatedGame = chessRepository.update(expectedGame);
        assertAll(() -> {
            assertThat(updatedGame.getColorOfCurrentTurn()).isEqualTo(expectedGame.getColorOfCurrentTurn());
            assertThat(updatedGame.isFinished()).isEqualTo(expectedGame.isFinished());
        });
        chessRepository.remove(updatedGame.getId());
    }

    @DisplayName("데이터를 삭제할 수 있어야 한다.")
    @Test
    void remove() {
        final Game game = chessRepository.save(Game.initializeGame("title", "password"));
        chessRepository.remove(game.getId());

        assertThatThrownBy(() -> chessRepository.findById(game.getId()))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}