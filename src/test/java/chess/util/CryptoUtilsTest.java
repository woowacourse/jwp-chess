package chess.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CryptoUtilsTest {

    @DisplayName("서로 다른 두 텍스트를 생성하여 암호화하여 비교하더라도 그 둘의 암호화된 값은 같다")
    @Test
    void equals_after_encrypted_text() {
        String text_1 = new String("APPLE");
        String text_2 = new String("APPLE");

        String encryptedText_1 = CryptoUtils.encrypt(text_1);
        String encryptedText_2 = CryptoUtils.encrypt(text_2);

        assertThat(text_1).isNotEqualTo(encryptedText_1);
        assertThat(encryptedText_1).isEqualTo(encryptedText_2);
    }
}