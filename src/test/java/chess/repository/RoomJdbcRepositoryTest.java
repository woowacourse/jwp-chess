package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.Room;
import chess.dto.GameCreateRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(properties = "spring.config.location=classpath:/application-test.yml")
@Sql("/schema-test.sql")
class RoomJdbcRepositoryTest {

    public static final GameCreateRequest CREATE_FIXTURE = new GameCreateRequest("test room", "password", "white",
            "black");
    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    void setup() {
        roomRepository.deleteAll();
    }

    @Test
    @DisplayName("Room 생성 테스트")
    void save() {
        Room room = roomRepository.save(CREATE_FIXTURE);
        Room expected = new Room(room.getId(), "test room", "password");

        assertThat(room).isEqualTo(expected);
    }

    @Test
    @DisplayName("Room 조회 테스트")
    void findById() {
        Room room = roomRepository.save(CREATE_FIXTURE);
        Room roomById = roomRepository.findById(room.getId());

        assertThat(roomById).isEqualTo(room);
    }

    @Test
    @DisplayName("Room 삭제 테스트")
    void deleteById() {
        Room room = roomRepository.save(CREATE_FIXTURE);
        List<Room> roomsAfterSave = roomRepository.findAll();

        long rows = roomRepository.deleteById(room.getId());
        List<Room> roomsAfterDelete = roomRepository.findAll();

        assertAll(
                () -> assertThat(roomsAfterSave).isNotEmpty(),
                () -> assertThat(rows).isOne(),
                () -> assertThat(roomsAfterDelete).isEmpty()
        );
    }
}
