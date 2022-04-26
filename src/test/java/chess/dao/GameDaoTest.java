package chess.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import chess.domain.piece.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class GameDaoTest {

    private GameDao gameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDao = new GameDao(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE game IF EXISTS");

        jdbcTemplate.execute("create table game("
            + "id varchar(100) not null unique, "
            + "turn varchar(10) not null, "
            + "force_end_flag tinyint(1) not null default false"
            + ")");
    }

    @Test
    void isExistId_게임생성_성공시_true를_반환() {
        gameDao.createById("1234");
        assertThat(gameDao.exists("1234")).isTrue();
    }

    @Test
    void isExistId_게임생성_실패시_false를_반환() {
        gameDao.createById("1234");
        assertThat(gameDao.exists("123")).isFalse();
    }

    @Test
    void findForceEndFlagById_게임이_끝나지_않으면_foce_end_flag는_false() {
        gameDao.createById("1234");
        assertThat(gameDao.findForceEndFlagById("1234")).isFalse();
    }

    @Test
    void findForceEndFlagById_존재하지_않는_게임아이디_조회시_예외가_발생한다() {
        gameDao.createById("1234");
        assertThatThrownBy(() -> gameDao.findForceEndFlagById("124"))
            .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void findTurnById_초기_불가능한_turn은_black이다() {
        gameDao.createById("1234");
        assertThat(gameDao.findTurnById("1234")).isEqualTo(Color.BLACK);
    }

    @Test
    void updateTurnById_turn_업데이트_성공() {
        gameDao.createById("1234");

        gameDao.updateTurnById(Color.WHITE, "1234");

        assertThat(gameDao.findTurnById("1234")).isEqualTo(Color.WHITE);
    }

    @Test
    void updateForceEndFlagById_force_end_flag_업데이트_성공() {
        gameDao.createById("1234");

        gameDao.updateForceEndFlagById(true, "1234");

        assertThat(gameDao.findForceEndFlagById("1234")).isEqualTo(true);
    }

    @Test
    void deleteById_게임_삭제_성공() {
        gameDao.createById("1234");
        assertThat(gameDao.exists("1234")).isTrue();

        gameDao.deleteById("1234");

        assertThat(gameDao.exists("1234")).isFalse();
    }


}
