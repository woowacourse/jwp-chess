package chess.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import chess.domain.vo.Room;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoomTest {

    @Test
    @DisplayName("방 이름이 공백일 경우 예외를 발생한다.")
    void createRoomByEmptyName() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new Room("", "1234"))
            .withMessage("방 이름은 공백일 수 없습니다.");
    }

    @Test
    @DisplayName("비밀번호가 공백일 경우 예외를 발생한다.")
    void createRoomByEmptyPassword() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new Room("hoho", ""))
            .withMessage("패스워드는 공백일 수 없습니다.");
    }

    @Test
    @DisplayName("방 이름이 동일할 경우 true를 반환한다.")
    void equalsRoom() {
        Room room1 = new Room("hoho", "1234");
        Room room2 = new Room("hoho", "4321");

        assertThat(room1).isEqualTo(room2);
    }
}
