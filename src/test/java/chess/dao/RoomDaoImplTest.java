package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import chess.domain.Team;
import chess.entity.Room;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class RoomDaoImplTest {

    private RoomDaoImpl roomDaoImpl;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        roomDaoImpl = new RoomDaoImpl(jdbcTemplate);

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS room" +
            "(" +
            "  id bigint AUTO_INCREMENT," +
            "  team varchar(50) NOT NULL," +
            "  title varchar(50) NOT NULL," +
            "  password varchar(50) NOT NULL," +
            "  status boolean," +
            " PRIMARY KEY (id)" +
            ")");

        jdbcTemplate.update("insert into room (id, team, title, password, status) values(?, ?, ?, ?, ?)", 1,
            "WHITE", "제목", "비밀번호", true);
    }

    @Test
    void findById() {
        assertThat(roomDaoImpl.findById(1L)).isNotNull();
    }

    @Test
    void deleteBy() {
        assertThatNoException().isThrownBy(() -> roomDaoImpl.deleteBy(1L, "password"));
    }

    @Test
    void save() {
        Room room = new Room(2L, Team.WHITE, "title", "password", true);
        assertThatNoException().isThrownBy(() -> roomDaoImpl.save(room));
    }

    @Test
    void updateStatus() {
        assertThatNoException().isThrownBy(() -> roomDaoImpl.updateTeam(Team.WHITE, 1L));
    }

    @Test
    void findAll() {
        List<Room> rooms = roomDaoImpl.findAll();
        assertThat(rooms.size()).isEqualTo(1);
    }
}
