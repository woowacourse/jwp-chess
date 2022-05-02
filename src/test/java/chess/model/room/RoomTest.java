package chess.model.room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.model.game.ChessGame;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RoomTest {

    @Test
    @DisplayName("방의 비밀번호를 확인한다")
    void checkPassword() {
        Room room = Room.fromPlainPassword("room name", "password");
        assertAll(() -> {
            assertThat(room.isSamePassword("password")).isTrue();
            assertThat(room.isSamePassword("not same")).isFalse();
        });
    }
}
