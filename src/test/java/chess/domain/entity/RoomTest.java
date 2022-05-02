package chess.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoomTest {

    @Test
    @DisplayName("패스워드가 다르면 true를 반환한다.")
    void isNotSamePassword() {
        Room room = new Room(1L, "title", "password");
        assertThat(room.isNotSamePassword("비번")).isTrue();
    }
}
