package chess.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class RoomTest {

    @ParameterizedTest
    @ValueSource(strings = {"", "123456789012345678901"})
    @DisplayName("Name 길이가 1 ~ 20을 만족하지 않으면 예외를 던진다.")
    void validateNameException(String name) {
        assertThatThrownBy(() -> new Room(name, "1234567890"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("방 제목");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "12345678901234567890"})
    @DisplayName("Name 길이가 1 ~ 20을 만족하면 정상 생성된다.")
    void validateName(String name) {
        Room room = new Room(name, "1234567890");
        assertThat(room.getName()).isEqualTo(name);
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456789", "123456789012345678901"})
    @DisplayName("password 길이가 10 ~ 20을 만족하지 않으면 예외를 던진다.")
    void validatePasswordException(String password) {
        assertThatThrownBy(() -> new Room("name", password))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("비밀번호");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234567890", "12345678901234567890"})
    @DisplayName("password 길이가 10 ~ 20을 만족하면 정상 생성된다.")
    void validatePassword(String password) {
        Room room = new Room("name", password);
        assertThat(room.getPassword()).isEqualTo(password);
    }
}
