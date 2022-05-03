package chess.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PasswordSha256EncoderTest {

    @DisplayName("sha-256 단방향 암호화 알고리즘으로 String 형 password를 암호화한다.")
    @Test
    void encode() {
        final String rawPassword = "1234";

        final String encodedPassword = PasswordSha256Encoder.encode(rawPassword);

        assertThat(encodedPassword).isEqualTo(PasswordSha256Encoder.encode(rawPassword));
    }
}
