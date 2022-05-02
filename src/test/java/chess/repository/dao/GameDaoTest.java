package chess.repository.dao;

import static chess.domain.Color.BLACK;
import static chess.domain.Color.WHITE;
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

import chess.repository.dao.dto.game.GameDto;
import chess.repository.dao.dto.game.GameUpdateDto;

@JdbcTest
class GameDaoTest {

    private static final Long SAVED_GAME_ID = 1L;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private GameDao gameDao;

    @BeforeEach
    void setUp() {
        gameDao = new GameDao(jdbcTemplate);
    }

    @DisplayName("데이터를 저장할 수 있어야 한다.")
    @Test
    void save() {
        final Long id = gameDao.save(
                new GameDto(0L, "title", "password",
                        1L, 2L, false, WHITE.getName())
        );
        assertThat(gameDao.findById(id)).isNotNull();
    }

    @DisplayName("데이터를 조회할 수 있어야 한다.")
    @Test
    void findById() {
        final GameDto gameDto = gameDao.findById(SAVED_GAME_ID);
        assertAll(() -> {
            assertThat(gameDto.getTitle()).isEqualTo("title");
            assertThat(gameDto.getPassword()).isEqualTo("password");
            assertThat(gameDto.getPlayer_id1()).isEqualTo(1);
            assertThat(gameDto.getPlayer_id2()).isEqualTo(2);
            assertThat(gameDto.getFinished()).isFalse();
            assertThat(gameDto.getCurrentTurnColor()).isEqualTo("White");
        });
    }

    @DisplayName("존재하지 않는 데이터 조회 시, 예외를 발생시켜야 한다.")
    @Test
    void notExistException() {
        final Long notFoundGameId = 100L;
        assertThatThrownBy(() -> gameDao.findById(notFoundGameId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("데이터를 수정할 수 있어야 한다.")
    @Test
    void update() {
        final boolean finished = true;
        final String currentTurnColor = BLACK.getName();
        final GameUpdateDto gameUpdateDto = new GameUpdateDto(SAVED_GAME_ID, finished, currentTurnColor);
        gameDao.update(gameUpdateDto);

        final GameDto updatedGameDto = gameDao.findById(SAVED_GAME_ID);
        assertAll(() -> {
            assertThat(updatedGameDto.getFinished()).isEqualTo(finished);
            assertThat(updatedGameDto.getCurrentTurnColor()).isEqualTo(currentTurnColor);
        });
    }

    @DisplayName("데이터를 삭제할 수 있어야 한다.")
    @Test
    void remove() {
        gameDao.remove(SAVED_GAME_ID);
        assertThatThrownBy(() -> gameDao.findById(SAVED_GAME_ID))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}