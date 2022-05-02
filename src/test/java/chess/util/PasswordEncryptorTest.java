package chess.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PasswordEncryptorTest {

    @DisplayName("비밀번호 암호화 테스트")
    @Test
    void same_Input_And_Salt() {
        String input = "password";
        String salt = PasswordEncryptor.generateSalt();

        String encrypted = PasswordEncryptor.encrypt(input, salt);

        assertThat(encrypted).isNotEqualTo(input);
    }
}
