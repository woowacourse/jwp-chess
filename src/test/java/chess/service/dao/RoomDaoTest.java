package chess.service.dao;

import chess.controller.dto.RoomDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class RoomDaoTest {

    @Autowired RoomDao roomDao;

    private String roomName;
    private long roomId;

    @BeforeEach
    void beforeEach() {
        roomName = "room";

        roomId = roomDao.save(roomName);
    }

    @Test
    void load() {
        roomDao.save("room2");
        roomDao.save("room3");

        List<RoomDto> rooms = roomDao.load();

        assertThat(rooms.size()).isEqualTo(3);
    }

    @Test
    void delete() {
        roomDao.delete(roomId);

        List<RoomDto> rooms = roomDao.load();

        assertThat(rooms.size()).isEqualTo(0);
    }

    @Test
    void name() throws SQLException {
        assertThat(roomDao.name(roomId)).isEqualTo("room");
    }

    @Test
    void notValidName() {
        assertThatThrownBy(() -> roomDao.name(12L)).isInstanceOf(SQLException.class);
    }
}