package chess.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Room 은")
class RoomTest {

    @DisplayName("패스워드가 같은지를 검사할 수 있어야 한다.")
    @ParameterizedTest(name = "{index} {displayName} password={0} expectedResult={1}")
    @CsvSource(value = {"rightPassword, true", "wrongPassword, false"})
    void check_Password(final String password, final Boolean expectedResult) {
        final Room room = new Room(1, "rightPassword");

        assertThat(room.checkPassword(password)).isEqualTo(expectedResult);
    }
}
