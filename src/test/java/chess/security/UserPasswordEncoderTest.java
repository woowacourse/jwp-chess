package chess.security;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserPasswordEncoderTest {

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("평문을 인코딩한다.")
    public void testEncode() {
        // given
        int bcryptEncodedStringSize = 60;
        String str = "hello world";
        // when
        final String encoded = encoder.encode(str);
        // then
        assertThat(encoded.length()).isEqualTo(bcryptEncodedStringSize);
    }

    @Test
    @DisplayName("인코딩 결과값은 실행마다 다르다.")
    public void encodingResultsAreDifferentEach() {
        // given
        String str = "hello world";
        // when
        final String encodedOne = encoder.encode(str);
        final String encodedTwo = encoder.encode(str);
        // then
        assertThat(encodedOne).isNotEqualTo(encodedTwo);
    }

    @Test
    @DisplayName("matches를 통해야만 동일한 평문임을 확인할 수 있다.")
    public void encodedStringCanBeProvedByMatches() {
        // given
        String str = "hello world";

        // when
        final String encoded = encoder.encode(str);

        // then
        final boolean isMatch = encoder.matches(str, encoded);
        assertThat(isMatch).isTrue();
    }
}
