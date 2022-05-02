package chess.domain.game.room;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoomTitleTest {
    @DisplayName("빈 체스방 제목은 생성할 수 없다.")
    @Test
    void from_throwsExceptionWithEmptyString() {
        Assertions.assertThatThrownBy(() -> RoomTitle.from(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 제목으로 생성할 수 없습니다.");
    }

    @DisplayName("체스방 제목은 50자를 초과할 수 없다.")
    @Test
    void from_throwsExceptionWithOver50Characters() {
        Assertions.assertThatThrownBy(
                        () -> RoomTitle.from("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam."))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("50자를 초과한 제목으로 생성할 수 없습니다.");
    }
}
