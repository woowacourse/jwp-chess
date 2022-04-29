package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.entity.RoomEntity;
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
}
