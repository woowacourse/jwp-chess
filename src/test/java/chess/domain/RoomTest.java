package chess.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class RoomTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("비밀번호 일치여부를 확인할 수 있다")
    void matchPassword() {
        final Room testRoom = new Room(1L, "test room", passwordEncoder.encode("1234"));
        assertAll(
                () -> assertThat(testRoom.matchPassword("1234", passwordEncoder)).isTrue(),
                () -> assertThat(testRoom.matchPassword("1233", passwordEncoder)).isFalse()
        );
    }
}
