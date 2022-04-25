package chess.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PasswordEncoderTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @DisplayName("password를 sha-256 알고리즘으로 인코딩한다.")
    @Test
    void encode() {
        String password = "1234";
        String encode = passwordEncoder.encode(password);

        assertAll(
                () -> assertThat(password).isNotEqualTo(encode),
                () -> assertThat(encode).isEqualTo(passwordEncoder.encode(password))
        );
    }
}
