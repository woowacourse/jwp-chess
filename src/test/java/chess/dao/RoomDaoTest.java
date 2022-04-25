package chess.dao;

import chess.domain.Team;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@JdbcTest
public class RoomDaoTest {

    private RoomDao roomDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        roomDao = new RoomDao(jdbcTemplate);

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS room" +
                "(" +
                "  id bigint NOT NULL AUTO_INCREMENT," +
                "  status varchar(50) NOT NULL," +
                "  PRIMARY KEY (id)" +
                ")");

        jdbcTemplate.update("insert into room (status) values(?)", "WHITE");
    }

    @AfterEach
    void delete() {
        assertThatNoException().isThrownBy(() -> roomDao.deleteGame());
    }

    @Test
    void findRoom() {
        assertThat(roomDao.findById()).isNotNull();
    }

    @Test
    void saveRoom() {
        assertThatNoException().isThrownBy(() -> roomDao.makeGame(Team.WHITE));
    }

    @Test
    void updateStatus() {
        assertThatNoException().isThrownBy(() -> roomDao.updateStatus(Team.WHITE, roomDao.findById().getId()));
    }
}
