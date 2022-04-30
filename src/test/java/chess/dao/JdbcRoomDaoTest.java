package chess.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class JdbcRoomDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcRoomDao jdbcRoomDao;

    @BeforeEach
    public void setUp() {
        jdbcRoomDao = new JdbcRoomDao(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE game IF EXISTS");
        jdbcTemplate.execute("DROP TABLE piece IF EXISTS");
        jdbcTemplate.execute("DROP TABLE room IF EXISTS");
        jdbcTemplate.execute("create table room("
                + "id int auto_increment, name varchar(20) not null,"
                + "password varchar(20) not null)");
    }

    @Test
    @DisplayName("정상적으로 방이 만들어 지는지 테스트한다.")
    public void createRoomTest() {
        jdbcRoomDao.createRoom("집에 가고 싶다.", "12345678");
        assertThat(jdbcRoomDao.findAllRoom()).hasSize(1);
    }

    @Test
    @DisplayName("정상적으로 방이 삭제되는지 테스트한다.")
    public void deleteRoomTest() {
        jdbcRoomDao.createRoom("집에 가고 싶다.", "12345678");
        jdbcRoomDao.deleteRoom(1, "12345678");
        assertThat(jdbcRoomDao.findAllRoom()).hasSize(0);
    }
}
