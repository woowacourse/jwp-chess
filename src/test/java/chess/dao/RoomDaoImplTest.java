package chess.dao;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import chess.entity.Room;

@JdbcTest
class RoomDaoImplTest {

    private final String firstRoomPassword = "pw12345678";
    private final String secondRoomPassword = "pw12345678";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private RoomDaoImpl roomDao;

    @BeforeEach
    void beforeEach() {
        roomDao = new RoomDaoImpl(jdbcTemplate);

        JdbcFixture jdbcFixture = new JdbcFixture(jdbcTemplate);
        jdbcFixture.dropTable("square");
        jdbcFixture.dropTable("room");
        jdbcFixture.createRoomTable();
        jdbcFixture.insertRoom("sojukang", "white", firstRoomPassword);
    }

    @Test
    @DisplayName("Room을 새로 생성한다.")
    void save() {
        Room room = new Room("hi", secondRoomPassword);
        roomDao.save(room);
        assertThat(roomDao.findByName("hi").get().getName()).isEqualTo("hi");
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
        roomDao.update(1L, "black");
        assertThat(roomDao.findByName("sojukang").get().getTurn()).isEqualTo("black");
    }

    @ParameterizedTest
    @CsvSource(value = {"1:0", "2:1"}, delimiter = ':')
    @DisplayName("Room을 삭제한다.")
    void delete(long id, int size) {
        roomDao.delete(id);
        assertThat(roomDao.findAll()).hasSize(size);
    }
}
