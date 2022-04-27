package chess.dao;

import chess.entity.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

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

        // when
        Game selectedGame = jdbcGameDao.find(id, password);

        // then
        assertAll(
                () -> assertThat(selectedGame).isNotNull(),
                () -> assertThat(selectedGame.getId()).isEqualTo(id),
                () -> assertThat(selectedGame.getTitle()).isEqualTo(title),
                () -> assertThat(selectedGame.getTurn()).isEqualTo(turn),
                () -> assertThat(selectedGame.getStatus()).isEqualTo(status)
        );
    }

    @Test
    @DisplayName("모든 게임 데이터 저장")
    void findAll() {
        // given
        Game game1 = new Game("라라라", "1234", "white", "playing");
        Game game2 = new Game("룰룰루", "222", "white", "playing");
        jdbcGameDao.save(game1);
        jdbcGameDao.save(game2);

        // when
        List<Game> games = jdbcGameDao.findAll();

        // then
        assertThat(games.size()).isEqualTo(2);
    }
}
