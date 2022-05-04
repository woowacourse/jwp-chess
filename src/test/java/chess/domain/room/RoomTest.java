package chess.domain.room;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

import chess.domain.piece.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RoomTest {

    @DisplayName("password가 일치하지 않을 시 삭제 불가능하다.")
    @Test
    void validateDeleteable_invalid_password() {
        Room room = new Room(2L, true, Color.WHITE, "제목", "비밀번호");

        assertThatCode(() -> room.validateDeletable("가짜비밀번호"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("잘못된 방 비밀번호 입니다. 다시 입력해주세요.");
    }

    @DisplayName("게임이 종료되지 않았을 시 삭제 불가능하다.")
    @Test
    void validateDeleteable_invalid_endFlag() {
        Room room = new Room(2L, false, Color.WHITE, "제목", "비밀번호");

        assertThatCode(() -> room.validateDeletable("비밀번호"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("아직 게임이 끝나지 않아 삭제할 수 없습니다.");
    }

    @DisplayName("게임이 종료되고 같은 비밀 번호일 시 삭제 가능하다.")
    @Test
    void validateDeleteable_success() {
        Room room = new Room(2L, true, Color.WHITE, "제목", "비밀번호");

        assertThatCode(() -> room.validateDeletable("비밀번호"))
            .doesNotThrowAnyException();
    }

}
