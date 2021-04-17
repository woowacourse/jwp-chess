package chess.repository.spring;

import chess.domain.room.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class RoomDAOTest {

    private RoomDAO roomDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        roomDAO = new RoomDAO(jdbcTemplate);

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS ROOM" +
                "(ID   INT NOT NULL AUTO_INCREMENT," +
                "NAME VARCHAR(255)," +
                "PRIMARY KEY (ID)" +
                ");");
        String query = "INSERT INTO ROOM (NAME) VALUES (?)";
        jdbcTemplate.update(query, "room1");
        jdbcTemplate.update(query, "room2");
    }

    @DisplayName("모든 방 리스트를 조회한다.")
    @Test
    void findAllRooms() {
        List<Room> rooms = roomDAO.findAllRooms();

        assertThat(rooms).hasSize(2);
    }

    @DisplayName("방을 추가한다.")
    @Test
    void insertRoom() {
        roomDAO.insertRoom("room3");

        List<Room> rooms = roomDAO.findAllRooms();

        assertThat(rooms).hasSize(3);
    }
}
