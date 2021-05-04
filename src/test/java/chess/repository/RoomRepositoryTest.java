package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import chess.domain.game.Room;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RoomRepositoryTest {

    @Autowired
    RoomRepository roomRepository;

    @DisplayName("방 저장 - 성공")
    @Test
    void create_success() {
        // given
        String roomName = "newRoom1";

        // then
        assertThatCode(() -> {
            roomRepository.create(roomName);
        }).doesNotThrowAnyException();
    }

    @DisplayName("방 검색 - 성공")
    @Test
    void findByName_success() {
        // given
        String roomName = "newRoom2";

        // when
        roomRepository.create(roomName);
        Room room = roomRepository.findByName(roomName);

        // then
        assertThat(room.getName()).isEqualTo(roomName);
    }

    @DisplayName("방의 체스말 정보 업데이트 - 성공")
    @Test
    void updatePieceMove_success() {
        // given
        String roomName = "newRoom3";

        // when
        roomRepository.create(roomName);
        Room room = roomRepository.findByName(roomName);

        // then
        assertThatCode(() -> {
            roomRepository.updatePieceMove(room, "12", "14");
        }).doesNotThrowAnyException();
    }

    @DisplayName("전체 방 정보 조회 - 성공")
    @Test
    void findAll_success() {
        // given
        String roomName = "newRoom4";
        roomRepository.create(roomName);

        // when
        List<Room> rooms = roomRepository.findAll();

        // then
        assertThat(rooms.size()).isGreaterThan(0);
    }
}