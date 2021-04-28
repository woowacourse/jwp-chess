package chess.webdao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
@Transactional
public class UserDaoTest {
    @Autowired
    ChessGameDao chessGameDao;

    @Test
    void createUserInfo() {
        chessGameDao.createUserInfo("duplicate", "one");
        assertThatCode(() -> chessGameDao.createUserInfo("duplicate", "two"))
                .isInstanceOf(DuplicateKeyException.class);
    }
}
