package chess.repository.spring;

import chess.domain.room.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@DirtiesContext
class RoomDAOTest {

    private RoomDAO roomDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        roomDAO = new RoomDAO(jdbcTemplate);
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS ROOM (ID INT NOT NULL AUTO_INCREMENT, NAME VARCHAR(255), PRIMARY KEY (ID))");
        String query = "INSERT INTO ROOM (NAME) VALUES (?)";
        jdbcTemplate.update(query, "room1");
        jdbcTemplate.update(query, "room2");
    }

    @DisplayName("방 일부를 조회한다.")
    @Test
    void findAllRooms() {
        List<Room> rooms = roomDAO.findByLimit(1, 0);

        assertThat(rooms).hasSize(1);
    }

    @DisplayName("방을 추가하고, 추가된 방의 id를 반환받는다.")
    @Test
    void insertRoom() {
        int id = roomDAO.insertRoom("room3");

        List<Room> rooms = roomDAO.findByLimit(5, 0);

        assertThat(rooms).hasSize(3);
        assertThat(id).isGreaterThan(2);
    }

    @DisplayName("방을 id로 삭제한다.")
    @Test
    void deleteRoom() {
        int id = roomDAO.insertRoom("room4");

        roomDAO.deleteById(id);

        assertThat(roomDAO.findByLimit(5, 0)).hasSize(2);
    }

    @DisplayName("방의 개수를 구한다.")
    @Test
    void counts() {
        int roomCounts = roomDAO.calculateRoomCounts();

        assertThat(roomCounts).isEqualTo(2);
    }
}
