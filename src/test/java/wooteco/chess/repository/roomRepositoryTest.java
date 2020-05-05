package wooteco.chess.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.chess.domain.coordinate.Coordinate;
import wooteco.chess.entity.Move;
import wooteco.chess.entity.Room;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class roomRepositoryTest {
    @Autowired
    MoveRepository moveRepository;
    @Autowired
    RoomRepository roomRepository;

    @AfterEach
    void deleteAll() {
        moveRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @DisplayName("findByName 잘 작동 하는지 확인")
    @Test
    void findByName() {
        roomRepository.save(new Room("blackPassword", "whitePassword", false, "testRoom1"));
        Room room = roomRepository.findByName("testRoom1").orElse(null);
        assertThat(room.getBlackPassword()).isEqualTo("blackPassword");
    }
}
