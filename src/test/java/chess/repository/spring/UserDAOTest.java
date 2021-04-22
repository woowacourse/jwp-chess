package chess.repository.spring;

import chess.domain.user.User;
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
class UserDAOTest {

    private UserDAO userDAO;
    private RoomDAO roomDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO(jdbcTemplate);
        roomDAO = new RoomDAO(jdbcTemplate);
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS ROOM (ID INT NOT NULL AUTO_INCREMENT, NAME VARCHAR(255), PRIMARY KEY (ID))");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS USER (ID INT NOT NULL AUTO_INCREMENT, PASSWORD  VARCHAR(255), " +
                "TEAM_TYPE VARCHAR(255), ROOM_ID   INT NOT NULL, PRIMARY KEY (ID), " +
                "CONSTRAINT USER_FK FOREIGN KEY (ROOM_ID) REFERENCES ROOM (ID))");
        String query = "INSERT INTO ROOM (NAME) VALUES (?)";
        jdbcTemplate.update(query, "room1");
    }

    @DisplayName("User를 추가한다.")
    @Test
    void addUser() {
        int roomId = roomDAO.findLastAddedRoom().get().getId();
        userDAO.insertUser("pass123", "BLACK", roomId);

        List<User> users = userDAO.findByRoomId(roomId);

        assertThat(users).hasSize(1);
    }

    @DisplayName("모든 User를 삭제한다.")
    @Test
    void deleteAllUsers() {
        int roomId = roomDAO.findLastAddedRoom().get().getId();
        userDAO.insertUser("123", "B", roomId);
        userDAO.insertUser("123", "c", roomId);

        userDAO.deleteAllUsersByRoomId(roomId);

        assertThat(userDAO.findByRoomId(roomId)).isEmpty();
    }

    @DisplayName("특정 ID의 User를 삭제한다.")
    @Test
    void deleteUser() {
        int roomId = roomDAO.findLastAddedRoom().get().getId();
        userDAO.insertUser("123", "B", roomId);
        User user = userDAO.findByRoomId(roomId).get(0);

        userDAO.deleteUser(user);

        assertThat(userDAO.findByRoomId(roomId)).isEmpty();
    }
}
