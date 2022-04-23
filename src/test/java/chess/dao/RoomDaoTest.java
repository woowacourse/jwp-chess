package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.entity.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class RoomDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RoomDao roomDao;

    @BeforeEach
    void beforeEach() {
        roomDao = new RoomDao(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE square IF EXISTS");
        jdbcTemplate.execute("DROP TABLE room IF EXISTS");
        jdbcTemplate.execute("create table room ("
                + " id bigint not null auto_increment,"
                + " name VARCHAR(255) not null,"
                + " turn varchar(10) not null,"
                + " primary key (id),"
                + " constraint uniqueName unique (name))");

        jdbcTemplate.update("INSERT INTO room(name, turn) VALUES (?,?)",
                "sojukang", "white");
    }

    @Test
    @DisplayName("ID로 Room을 가져온다.")
    void findById() {
        Room room = roomDao.findById(1).get();
        assertThat(room.getTurn()).isEqualTo("white");
    }

    @Test
    @DisplayName("Room을 새로 생성한다.")
    void save() {
        Room room = new Room("hi");
        roomDao.save(room);
        assertThat(roomDao.findById(2).get().getName()).isEqualTo("hi");
    }

    @Test
    @DisplayName("Name으로 Room을 가져온다.")
    void findByName() {
        Room room = roomDao.findByName("sojukang").get();
        assertThat(room.getTurn()).isEqualTo("white");
    }

    @Test
    @DisplayName("Room을 업데이트한다.")
    void update() {
        roomDao.update(1, "black");
        assertThat(roomDao.findById(1).get().getTurn()).isEqualTo("black");
    }
}
