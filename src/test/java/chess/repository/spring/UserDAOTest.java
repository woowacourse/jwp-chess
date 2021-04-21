package chess.repository.spring;

import chess.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class UserDAOTest {

    private UserDAO userDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO(jdbcTemplate);
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS ROOM (ID INT NOT NULL AUTO_INCREMENT, NAME VARCHAR(255), PRIMARY KEY (ID))");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS USER (ID INT NOT NULL AUTO_INCREMENT, PASSWORD  VARCHAR(255), " +
                "TEAM_TYPE VARCHAR(255), ROOM_ID   INT NOT NULL, PRIMARY KEY (ID), " +
                "CONSTRAINT USER_FK FOREIGN KEY (ROOM_ID) REFERENCES ROOM (ID) );");
        String query = "INSERT INTO ROOM (NAME) VALUES (?)";
        jdbcTemplate.update(query, "room1");
    }

    @DisplayName("User를 추가한다.")
    @Test
    void addUser() {
        userDAO.insertUser("pass123", "BLACK", 1);

        List<User> users = userDAO.findByRoomId(1);

        assertThat(users).hasSize(1);
    }
}
