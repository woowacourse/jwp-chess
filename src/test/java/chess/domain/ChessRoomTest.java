package chess.domain;

import chess.dto.RoomDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChessRoomTest {

    @Test
    @DisplayName("정적 팩토리 메서드로 방이 생성되는지 검증한다.")
    void initialChessRoom() {
        final RoomDto roomDto = new RoomDto(1L, "room", "1234");
        final String expected = "room";

        final String actual = ChessRoom.from(roomDto).getName();

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("방 제목이 1자 이상 25자 이하인지 검증한다.")
    @ValueSource(strings = {"", "abcdefghijklmnopqrstuvwxyz"})
    void validateChessRoomName(String input) {
        assertThatThrownBy(() -> new ChessRoom(input, "1234"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("체스방 제목은 1자 이상 25자 이하여야 합니다.");
    }

    @ParameterizedTest
    @DisplayName("방 비밀번호가 1자 이상 15자 이하인지 검증한다.")
    @ValueSource(strings = {"", "1234567890123456"})
    void validateChessRoomPassword(String input) {
        assertThatThrownBy(() -> new ChessRoom("room", input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("체스방 비밀번호는 1자 이상 15자 이하여야 합니다.");
    }
}
