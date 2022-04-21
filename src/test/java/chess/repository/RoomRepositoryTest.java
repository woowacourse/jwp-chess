package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;

import chess.entity.RoomEntity;
import java.util.List;
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

    @DisplayName("전체 룸을 가져온다.")
    @Test
    void findRooms() {
        final List<RoomEntity> rooms = roomRepository.findRooms();
        assertThat(rooms).hasSize(0);
    }

    @DisplayName("룸을 생성한다.")
    @Test
    void insert() {
        final RoomEntity roomEntity = new RoomEntity("체스 초보만", "white", false);
        final RoomEntity insertRoom = roomRepository.insert(roomEntity);
        assertThat(insertRoom).isEqualTo(roomEntity);
    }
}
