package chess.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class ChessRoomTest {

    @Test
    void checkSamePassword() {
        ChessRoom chessRoom = new ChessRoom("hi", "password", "empty");
        assertDoesNotThrow(() -> chessRoom.checkSamePassword("password"));
    }

    @Test
    void checkSamePasswordException() {
        ChessRoom chessRoom = new ChessRoom("hi", "password", "empty");
        assertThatThrownBy(() -> chessRoom.checkSamePassword("p"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @Test
    void isEnd() {
        ChessRoom chessRoom = new ChessRoom("hi", "password", "empty");
        assertThat(chessRoom.isEnd()).isTrue();
    }

    @Test
    void isEndFalse() {
        ChessRoom chessRoom = new ChessRoom("hi", "password", "white");
        assertThat(chessRoom.isEnd()).isFalse();
    }
}
