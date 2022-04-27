package chess.dao;

import chess.entity.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Sql("classpath:init.sql")
public class JdbcGameDaoTest {

    private JdbcGameDao jdbcGameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcGameDao = new JdbcGameDao(jdbcTemplate);
    }

    @Test
    @DisplayName("게임 데이터 저장")
    void save() {
        // given
        Game game = new Game("라라라", "1234", "white", "playing");

        // when
        long id = jdbcGameDao.save(game);

        // then
        assertThat(id).isEqualTo(1);
    }

    @Test
    @DisplayName("게임 데이터 삭제")
    void remove() {
        // given
        Game game = new Game("라라라", "1234", "white", "playing");
        long id = jdbcGameDao.save(game);

        // when
        Game deleteGame = new Game(id, "1234");
        jdbcGameDao.remove(deleteGame);

        // then
        assertThatThrownBy(() -> jdbcGameDao.find(id, "1234"))
                .isInstanceOfAny(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("게임 데이터 저장")
    void find() {
        // given
        String title = "라라라";
        String password = "1234";
        String turn = "white";
        String status = "playing";
        Game game = new Game(title, password, turn, status);
        long id = jdbcGameDao.save(game);

        // then
        assertAll(
                () -> assertThat(jdbcGameDao.find(id, password)).isNotNull(),
                () -> assertThat(jdbcGameDao.find(id, password).getId()).isEqualTo(id),
                () -> assertThat(jdbcGameDao.find(id, password).getTitle()).isEqualTo(title),
                () -> assertThat(jdbcGameDao.find(id, password).getTurn()).isEqualTo(turn),
                () -> assertThat(jdbcGameDao.find(id, password).getStatus()).isEqualTo(status)
        );
    }
}
