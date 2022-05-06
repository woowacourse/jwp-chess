package chess.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class RoomTest {
    @Test
    void name() {
        String invalidPassword = "0";
        Room room = new Room("클레이", "123");

        assertThatThrownBy(() -> room.checkPassword(invalidPassword))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
