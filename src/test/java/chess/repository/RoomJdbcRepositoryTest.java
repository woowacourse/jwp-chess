package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.Room;
import chess.dto.GameCreateRequest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class RoomJdbcRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Room Repository CRUD 테스트")
    void crud() {
        final GameCreateRequest gameCreateRequest = new GameCreateRequest("test room",
                passwordEncoder.encode("password"), "white", "black");

        final List<Room> before = roomRepository.findAll();
        final long id = roomRepository.save(gameCreateRequest);
        final long id2 = roomRepository.save(gameCreateRequest);
        final List<Room> after = roomRepository.findAll();

        final Room room = roomRepository.findById(id);

        roomRepository.deleteById(id);
        final List<Room> delete1 = roomRepository.findAll();

        roomRepository.deleteAll();
        final List<Room> deleteAll = roomRepository.findAll();

        assertAll(
                () -> assertThat(before.isEmpty()).isTrue(),
                () -> assertThat(after.isEmpty()).isFalse(),
                () -> assertThat(room.matchPassword("password", passwordEncoder)).isTrue(),
                () -> assertThat(delete1.size() == 1).isTrue(),
                () -> assertThat(deleteAll.isEmpty()).isTrue()
        );
    }
}
