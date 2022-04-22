package chess.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
public class BoardRepositoryImplTest {

    private final BoardRepository boardRepository = new BoardRepositoryImpl();

    @DisplayName("체스판 저장")
    @Test
    public void save() {

    }
}
