package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import chess.domain.Room;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@JdbcTest
public class RoomRepositoryImplTest {

    private static final Room room = new Room("summer", "summer", false);

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    private RoomRepository roomRepository;

    @BeforeEach
    void init() {
        roomRepository = new RoomRepositoryImpl(dataSource, jdbcTemplate);
    }

    @Test
    @DisplayName("뱡 insert")
    void insert() {
        int id = roomRepository.save(room);
        assertThat(id).isGreaterThan(0);
    }

    @Test
    @DisplayName("방 find")
    void find() {
        roomRepository.save(room);
        Room findRoom = roomRepository.findByName(room.getName()).orElseThrow();
        assertThat(room.getName()).isEqualTo(findRoom.getName());
    }

    @Test
    @DisplayName("이름으로 생성된 방을 삭제한다.")
    void removeByName() {
        int id = roomRepository.save(room);
        roomRepository.deleteById(id);

        assertThat(roomRepository.findByName(room.getName())).isEmpty();
    }

    @Test
    @DisplayName("저장된 전체 방을 찾아온다.")
    void findAll() {
        roomRepository.save(room);
        roomRepository.save(new Room("does", "does", false));

        List<Room> rooms = roomRepository.findAll();
        assertThat(rooms.size()).isEqualTo(2);
    }
}
