package chess.service;

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
public class RoomServiceTest {

    @Autowired
    private RoomService roomService;

    @DisplayName("이름과 비밀번호를 가지는 방을 생성한다.")
    @Test
    void create() {
        RoomEntity roomEntity = roomService.create("room1", "1234");
        assertThat(roomEntity.getName()).isEqualTo("room1");
        assertThat(roomEntity.getPassword()).isEqualTo("1234");
    }

    @DisplayName("모든 방을 조회한다.")
    @Test
    void findAllRooms() {
        roomService.create("room1", "1234");
        roomService.create("room2", "1234");
        roomService.create("room3", "1234");

        List<RoomEntity> rooms = roomService.findAllRooms();

        assertThat(rooms.size()).isEqualTo(3);
    }

    @DisplayName("비밀번호가 일치하는지 확인하고 방을 삭제한다.")
    @Test
    void delete() {
        RoomEntity roomEntity = roomService.create("room1", "1234");
        assertThat(roomService.delete(roomEntity.getId(), "123")).isFalse();
        assertThat(roomService.delete(roomEntity.getId(), "1234")).isTrue();

    }
}
