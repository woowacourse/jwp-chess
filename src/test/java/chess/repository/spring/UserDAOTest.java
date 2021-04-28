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
    private int roomId;

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
        roomId = roomDAO.insertRoom("room1");
    }

    @DisplayName("User를 추가한다.")
    @Test
    void addUser() {
        userDAO.insertUser("pass123", "BLACK", roomId);

        List<User> users = userDAO.findAllByRoomId(roomId);

        assertThat(users).hasSize(1);
    }

    @DisplayName("모든 User를 삭제한다.")
    @Test
    void deleteAllUsers() {
        userDAO.insertUser("123", "B", roomId);
        userDAO.insertUser("123", "c", roomId);

        userDAO.deleteAllByRoomId(roomId);

        assertThat(userDAO.findAllByRoomId(roomId)).isEmpty();
    }

    @DisplayName("특정 ID의 User를 삭제한다.")
    @Test
    void deleteUser() {
        userDAO.insertUser("123", "B", roomId);
        User user = userDAO.findAllByRoomId(roomId).get(0);

        userDAO.deleteUser(user);

        assertThat(userDAO.findAllByRoomId(roomId)).isEmpty();
    }
}
