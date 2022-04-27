package chess;

import chess.model.dao.TurnDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class TurnDaoTest {
    TurnDao turnDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void initPieceDaoTest() {
        jdbcTemplate.execute("DROP TABLE TURNS IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE TURNS\n" +
                "(\n" +
                "    turn_id  int        not null AUTO_INCREMENT,\n" +
                "    turn     varchar(5) not null,\n" +
                "    primary key (turn_id)\n" +
                ");");
        turnDao = new TurnDao(jdbcTemplate);


    }

    @AfterEach
    void cleanDB() {
        turnDao.deleteAll();
    }

    @Test
    @DisplayName("턴이 초기에 저장되었는지 확인한다")
    void init() {
        turnDao.init();
        Optional<String> turn = turnDao.findOne();

        assertThat(turn.get()).isEqualToIgnoringCase("white");
    }

    @Test
    @DisplayName("턴이 존재하지 않는 경우 무엇을 반환하는지 확인")
    void getTurn() {
        Optional<String> turn = turnDao.findOne();

        assertThat(turn).isEmpty();
    }

    @Test
    @DisplayName("턴이 update 되는지 확인한다")
    void update() {
        turnDao.init();
        turnDao.update("BLACK");
        Optional<String> turn = turnDao.findOne();

        assertThat(turn.get()).isEqualToIgnoringCase("black");
    }

    @Test
    @DisplayName("저장된 턴을 모두 삭제한다.")
    void deleteAll() {
        turnDao.init();
        turnDao.deleteAll();

        assertThat(turnDao.findOne()).isEmpty();
    }
}
