package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import chess.repository.entity.RoomEntity;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@JdbcTest
public class RoomDaoImplTest {

    private static final RoomEntity room = new RoomEntity("summer", "summer");

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    private RoomDao roomDao;

    @BeforeEach
    void init() {
        roomDao = new RoomDaoImpl(dataSource, jdbcTemplate);
    }

    @Test
    @DisplayName("뱡 insert")
    void insert() {
        int id = roomDao.save(room);
        assertThat(id).isGreaterThan(0);
    }

    @Test
    @DisplayName("방 find")
    void find() {
        roomDao.save(room);
        RoomEntity findRoom = roomDao.findByName(room.getName()).orElseThrow();
        assertThat(room.getName()).isEqualTo(findRoom.getName());
    }

    @Test
    @DisplayName("이름으로 생성된 방을 삭제한다.")
    void removeByName() {
        int id = roomDao.save(room);
        roomDao.deleteById(id);

        assertThat(roomDao.findByName(room.getName())).isEmpty();
    }

    @Test
    @DisplayName("저장된 전체 방을 찾아온다.")
    void findAll() {
        roomDao.save(room);
        roomDao.save(new RoomEntity("does", "does"));

        List<RoomEntity> rooms = roomDao.findAll();
        assertThat(rooms.size()).isEqualTo(2);
    }
}
