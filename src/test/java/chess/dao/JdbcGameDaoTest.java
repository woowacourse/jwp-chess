package chess.dao;

import chess.dao.entity.Game;
import chess.domain.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Sql("classpath:init.sql")
public class JdbcGameDaoTest {

    private JdbcGameDao gameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDao = new JdbcGameDao(jdbcTemplate);
    }

    @Test
    @DisplayName("게임 데이터 저장")
    void save() {
        // given
        Game game = new Game("라라라", "1234", "white", "playing");

        // when
        long id = gameDao.save(game);

        // then
        assertThat(id).isEqualTo(1);
    }

    @Test
    @DisplayName("게임 데이터 삭제")
    void remove() {
        // given
        Game game = new Game("라라라", "1234", "white", "playing");
        long id = gameDao.save(game);

        // when
        gameDao.removeById(id);

        // then
        assertThatThrownBy(() -> gameDao.findGameById(id))
                .isInstanceOfAny(EmptyResultDataAccessException.class);
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
        long id = gameDao.save(game);

        // when
        Game selectedGame = gameDao.findGameById(id);

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
        gameDao.save(game1);
        gameDao.save(game2);

        // when
        List<Game> games = gameDao.findAll();

        // then
        assertThat(games.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("게임 정보 수정")
    void update() {
        // given
        Game game = new Game("라라라", "1234", "white", "playing");
        long id = gameDao.save(game);

        // when
        gameDao.updateGame(id, "black", "end");

        // then
        assertAll(
                () -> assertThat(gameDao.findGameById(id).getTurn()).isEqualTo("black"),
                () -> assertThat(gameDao.findGameById(id).getStatus()).isEqualTo("end")
        );
    }

    @Test
    @DisplayName("게임 상태 업데이트")
    void updateStatus() {
        // given
        Game game = new Game("라라라", "1234", "white", "playing");
        long id = gameDao.save(game);

        // when
        GameStatus gameStatus = GameStatus.FINISHED;
        gameDao.updateStatus(id, gameStatus);

        // then
        assertThat(gameDao.findGameById(id).getStatus()).isEqualTo(gameStatus.getName());
    }
}
