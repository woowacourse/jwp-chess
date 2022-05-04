package chess.repository.dao;

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

import chess.repository.dao.dto.player.PlayerDto;

@JdbcTest
class PlayerDaoTest {

    private static final Long SAVED_PLAYER_ID = 1L;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private PlayerDao playerDao;

    @BeforeEach
    void setUp() {
        playerDao = new PlayerDao(jdbcTemplate);
    }

    @DisplayName("데이터를 저장할 수 있어야 한다.")
    @Test
    void save() {
        final Long id = playerDao.save(
                new PlayerDto(0L, WHITE.getName(), "a1:Rook,a2:Pawn"));
        assertThat(playerDao.findById(id)).isNotNull();
    }

    @DisplayName("데이터를 조회할 수 있어야 한다.")
    @Test
    void findById() {
        final PlayerDto playerDto = playerDao.findById(SAVED_PLAYER_ID);
        assertAll(() -> {
            assertThat(playerDto.getColorName()).isEqualTo("White");
            assertThat(playerDto.getPieces()).isEqualTo("a1:King");
        });
    }

    @DisplayName("존재하지 않는 데이터 조회 시, 예외를 발생시켜야 한다.")
    @Test
    void notExistException() {
        final Long notFoundGameId = 100L;
        assertThatThrownBy(() -> playerDao.findById(notFoundGameId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("데이터를 수정할 수 있어야 한다.")
    @Test
    void update() {
        final String expectedPieces = "a3:Rook,a4:Pawn";
        final PlayerDto playerDto = new PlayerDto(SAVED_PLAYER_ID, WHITE.getName(), expectedPieces);
        playerDao.update(playerDto);

        final PlayerDto updatedPlayerDto = playerDao.findById(SAVED_PLAYER_ID);
        assertThat(updatedPlayerDto.getPieces()).isEqualTo(expectedPieces);
    }

    @DisplayName("데이터를 삭제할 수 있어야 한다.")
    @Test
    void remove() {
        playerDao.remove(SAVED_PLAYER_ID);
        assertThatThrownBy(() -> playerDao.findById(SAVED_PLAYER_ID))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}