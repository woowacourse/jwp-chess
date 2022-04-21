package chess.repository;

import chess.entity.RoomEntity;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@ActiveProfiles("test")
@SpringBootTest
@Transactional
class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @DisplayName("")
    @Test
    void findRooms() {
        final List<RoomEntity> rooms = roomRepository.findRooms();

        Assertions.assertThat(rooms).hasSize(0);
    }
}
