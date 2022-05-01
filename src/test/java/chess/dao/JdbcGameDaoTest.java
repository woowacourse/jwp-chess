package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.util.RandomCreationUtils;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

//@JdbcTest
@SpringBootTest
class JdbcGameDaoTest {

    @Autowired
    private JdbcGameDao gameDao;

    @Test
    @Rollback(value = false)
    void test1() {
        boolean existGameBeforeSave = gameDao.isExistGame();
        assertThat(existGameBeforeSave).isFalse();
        gameDao.save(RandomCreationUtils.createUuid(), "Black Team", LocalDateTime.now());
    }
}