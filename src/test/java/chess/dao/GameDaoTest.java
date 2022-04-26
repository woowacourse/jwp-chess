package chess.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import chess.domain.piece.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class GameDaoTest {

    private GameDao gameDao;
    private long id;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDao = new GameDao(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE game IF EXISTS");

        jdbcTemplate.execute("create table game("
            + "id int AUTO_INCREMENT PRIMARY KEY, "
            + "turn varchar(10) not null default 'black',"
            + "force_end_flag tinyint(1) not null default false,"
            + "title varchar(100) not null,"
            + "password varchar(100) not null"
            + ")");

        id = gameDao.createByTitleAndPassword("게임방제목", "password486");
    }

    @DisplayName("게임을 만들 때 title과 password를 입력받는다.")
    @Test
    void createById() {
        long gameId = gameDao.createByTitleAndPassword("게임방입니다~", "password486");
        assertThat(gameDao.exists(String.valueOf(gameId))).isTrue();
    }

    //    @DisplayName("존재하는 게임을 조회시 true를 반환한다.")
//    @Test
//    void isExistId_true() {
//        gameDao.createById("1234");
//        assertThat(gameDao.exists("1234")).isTrue();
//    }
//
//    @DisplayName("존재하지 않는 게임을 조회시 false를 반환한다.")
//    @Test
//    void isExistId_false() {
//        gameDao.createById("1234");
//        assertThat(gameDao.exists("123")).isFalse();
//    }
//
    @DisplayName("게임이 끝나지 않은 경우 force_end_flag는 false이다.")
    @Test
    void findForceEndFlagById_false() {
        assertThat(gameDao.findForceEndFlagById(id)).isFalse();
    }
//
//    @DisplayName("존재하지_않는_게임아이디_조회시_예외가_발생한다.")
//    @Test
//    void findForceEndFlagById_exception() {
//        gameDao.createById("1234");
//        assertThatThrownBy(() -> gameDao.findForceEndFlagById("124"))
//            .isInstanceOf(EmptyResultDataAccessException.class);
//    }
//
    @DisplayName("turn칼럼에는 불가능한 turn이 저장되며 초깃값은 black이다.")
    @Test
    void findTurnById() {
        assertThat(gameDao.findTurnById(id)).isEqualTo(Color.BLACK);
    }

    @DisplayName("turn 업데이트 성공")
    @Test
    void updateTurnById() {
        gameDao.updateTurnById(Color.WHITE, id);

        assertThat(gameDao.findTurnById(id)).isEqualTo(Color.WHITE);
    }

    @DisplayName("force_end_flag 업데이트 성공")
    @Test
    void updateForceEndFlagById() {
        gameDao.updateForceEndFlagById(true, id);

        assertThat(gameDao.findForceEndFlagById(id)).isEqualTo(true);
    }

    @DisplayName("게임 삭제시 더이상 게임은 존재하지 않는다.")
    @Test
    void deleteById_게임_삭제_성공() {
//        assertThat(gameDao.exists(id)).isTrue();

        gameDao.deleteById(id);

        assertThat(gameDao.exists("1234")).isFalse();
    }


}
