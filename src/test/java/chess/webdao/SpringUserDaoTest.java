package chess.webdao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
@Transactional
public class SpringUserDaoTest {
    @Autowired
    SpringChessGameDao springChessGameDao;

    @Test
    void createUserInfo() {
        springChessGameDao.createUserInfo("duplicate", "one");
        assertThatCode(() -> springChessGameDao.createUserInfo("duplicate", "two"))
                .isInstanceOf(DuplicateKeyException.class);
    }
}
