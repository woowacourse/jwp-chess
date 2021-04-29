package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class RoomDaoTest {

    @Autowired
    private RoomDao roomDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void SetUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS room cascade");
        jdbcTemplate.execute("create table room("
            + "id int not null auto_increment,"
            + "name varchar(50) not null,"
            + "primary key (id))");
        jdbcTemplate.execute("insert into room (name) values ('test')");
    }

    @DisplayName("존재하는 방 목록을 불러온다.")
    @Test
    void getRooms() {
        assertThat(roomDao.getRooms()).hasSize(1);
    }

    @DisplayName("방을 삭제할 수 있다.")
    @Test
    void delete() {
        roomDao.delete(1);
        assertThat(roomDao.getRooms()).hasSize(0);
    }

    @DisplayName("새 방을 만들 수 있다.")
    @Test
    void insert() {
        roomDao.insert("test2");
        assertThat(roomDao.getRooms()).hasSize(2);
    }
}