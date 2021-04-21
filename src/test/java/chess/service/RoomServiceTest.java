package chess.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SpringBootTest
@Transactional
class RoomServiceTest {

    @Autowired RoomService roomService;

    private String roomName;
    private Long roomId;

    @BeforeEach
    void beforeEach() {
        roomName = "room";
        roomId = roomService.save(roomName);
    }

    @Test
    void testValidateDuplicatedRoom() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> roomService.save("room"))
                .withMessage("중복된 방 이름입니다.");
    }

    @Test
    void testValidateRoomNameLength() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> roomService.save("abcdefghijk"))
                .withMessage("방 이름은 10 글자 이하로 입력해주세요.");
    }

    @Test
    void delete() {
        roomService.delete(roomId);

        assertThat(roomService.loadList().size()).isEqualTo(0);
    }

    @Test
    void loadList() {
        assertThat(roomService.loadList().size()).isEqualTo(1);
    }

    @Test
    void room() throws SQLException {
        assertThat(roomService.room(roomId).getName()).isEqualTo("room");
    }
}