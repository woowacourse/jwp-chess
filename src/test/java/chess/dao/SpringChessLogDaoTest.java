package chess.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource("classpath:application-test.properties")
class SpringChessLogDaoTest {
    private static final String testId = "123";

    @Autowired
    private SpringChessLogDao chessLogDao;

    @BeforeEach
    void setUp() {
        chessLogDao.addLog(testId, "a2", "a3");
    }

    @AfterEach
    void tearDown() {
        chessLogDao.deleteLog(testId);
    }

    @Test
    @DisplayName("로그 확인 기능")
    void applyCommand() {
        assertThat(chessLogDao.applyCommand(testId)).containsExactly("a2,a3");
    }

    @Test
    @DisplayName("로그 추가 기능")
    void addLog() {
        chessLogDao.addLog(testId, "a7", "a6");
        List<String> commands = chessLogDao.applyCommand(testId);

        assertThat(commands).hasSize(2);
        assertThat(commands).contains("a7,a6");
    }

    @Test
    @DisplayName("로그 삭제 기능")
    void deleteLog() {
        chessLogDao.deleteLog(testId);

        assertThat(chessLogDao.applyCommand(testId)).hasSize(0);
    }
}