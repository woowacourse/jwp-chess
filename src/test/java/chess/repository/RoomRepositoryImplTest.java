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
class RoomRepositoryImplTest {

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

    @DisplayName("룸의 현재 차례를 업데이트한다.")
    @Test
    void updateTeam() {
        final RoomEntity roomEntity = new RoomEntity("체스 초보만", "white", false);
        final Long id = roomRepository.insert(roomEntity).getId();
        roomRepository.updateTeam(id, "black");

        assertThat(roomRepository.findById(id).getTeam()).isEqualTo("black");
    }

    @DisplayName("룸의 상태를 종료로 변경한다")
    @Test
    void finishRoom() {
        final RoomEntity roomEntity = new RoomEntity("체스 초보만", "white", false);
        final Long id = roomRepository.insert(roomEntity).getId();
        roomRepository.updateGameOver(id);

        assertThat(roomRepository.findById(id).isGameOver()).isTrue();
    }
}
