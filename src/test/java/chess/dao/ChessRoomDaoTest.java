package chess.dao;

import chess.domain.Team;
import chess.dto.GameIdDto;
import chess.dto.RoomTempDto;
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

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS room(" +
                "  id bigint NOT NULL AUTO_INCREMENT,\n" +
                "  status varchar(50) NOT NULL,\n" +
                "  name varchar(50) NOT NULL,\n" +
                "  password varchar(20) NOT NULL,\n" +
                "  PRIMARY KEY (id)" +
                ")");

        jdbcTemplate.update("insert into room (id, status, name, password) values(?, ?, ?, ?)", 1000, "WHITE", "green", "1234");
    }

    @AfterEach
    void delete() {
        assertThatNoException().isThrownBy(() -> roomDao.deleteGame(1000));
    }

    @Test
    void findRoom() {
        assertThat(roomDao.findById(new RoomTempDto("green", "1234"))).isNotNull();
    }

    @Test
    void saveRoom() {
        assertThatNoException().isThrownBy(() -> roomDao.makeGame(Team.WHITE,
                new RoomTempDto("green", "1234")));
    }

    @Test
    void updateStatus() {
        assertThatNoException().isThrownBy(() -> roomDao.updateStatus(Team.WHITE,
                roomDao.findById(new GameIdDto(1000L)).getId()));
    }
}
