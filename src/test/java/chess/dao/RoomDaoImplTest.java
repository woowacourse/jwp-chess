package chess.dao;

import chess.domain.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

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
                "  id bigint NOT NULL," +
                "  status varchar(50) NOT NULL," +
                " PRIMARY KEY (id)" +
                ")");

        jdbcTemplate.update("insert into room (id, status) values(?, ?)", 1, "WHITE");
    }

    @Test
    void findRoom() {
        assertThat(roomDaoImpl.findById(1L)).isNotNull();
    }

    @Test
    void deleteRoom() {
        assertThatNoException().isThrownBy(() -> roomDaoImpl.delete(1L));
    }

    @Test
    void saveRoom() {
        assertThatNoException().isThrownBy(() -> roomDaoImpl.save(2L, Team.WHITE));
    }

    @Test
    void updateStatus() {
        assertThatNoException().isThrownBy(() -> roomDaoImpl.updateStatus(Team.WHITE, 1L));
    }
}
