package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RoomDaoImplTest {

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        roomDao = new RoomDaoImpl(jdbcTemplate);
    }

    @DisplayName("데이터를 삽입한다.")
    @Test
    void insert() {
        String name = "리버룸";
        String password = "1234";

        assertThat(roomDao.insert(name, password)).isEqualTo(1);
    }
}
