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
        jdbcTemplate.execute("DROP TABLE IF EXISTS room");
        jdbcTemplate.execute("CREATE TABLE room("
                + "id INT NOT NULL AUTO_INCREMENT,"
                + "name VARCHAR(20) NOT NULL, "
                + "password VARCHAR(20) NOT NULL,"
                + "PRIMARY KEY (id));"
        );

        jdbcTemplate.update("insert into room (name, password) values (?, ?)", "일번방", "1234");
    }

    @DisplayName("데이터를 삽입한다.")
    @Test
    void insert() {
        String name = "리버룸";
        String password = "1234";

        assertThat(roomDao.insert(name, password)).isEqualTo(1);
    }

    @DisplayName("전체 방 데이터를 가져온다.")
    @Test
    void findAll() {
        assertThat(roomDao.findAll()).isNotEmpty();
    }
}
