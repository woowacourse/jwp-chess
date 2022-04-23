package chess.dao.springjdbc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class SpringBoardDaoTest {

    private SpringBoardDao springBoardDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        springBoardDao = new SpringBoardDao(jdbcTemplate);
    }

    @Test
    void initBoard() {
    }

    @Test
    void getBoardByGameId() {
    }

    @Test
    void remove() {
    }

    @Test
    void update() {
    }
}