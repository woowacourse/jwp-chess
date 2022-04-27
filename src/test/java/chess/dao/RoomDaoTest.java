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

    private RoomDaoImpl roomDao;

    @BeforeEach
    void beforeEach() {
        roomDao = new RoomDaoImpl(jdbcTemplate);
        JdbcFixture.dropTable(jdbcTemplate, "square");
        JdbcFixture.dropTable(jdbcTemplate, "room");

        JdbcFixture.createRoomTable(jdbcTemplate);

        JdbcFixture.insertRoom(jdbcTemplate, "sojukang", "1234", "white");
    }

    @Test
    @DisplayName("Room을 새로 생성한다.")
    void save() {
        Room room = new Room("hi", "");
        long savedId = roomDao.save(room);
        assertThat(savedId).isEqualTo(2L);
    }

    @Test
    @DisplayName("Room을 업데이트한다.")
    void update() {
        roomDao.updateTurn(1L, "black");
        assertThat(roomDao.findById(1L).get().getTurn()).isEqualTo("black");
    }

    @Test
    @DisplayName("Room을 삭제한다.")
    void delete() {
        roomDao.deleteRoom(1L);
        assertThat(roomDao.findAll()).isEmpty();
    }
}
