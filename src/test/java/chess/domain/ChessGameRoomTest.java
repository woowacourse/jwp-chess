package chess.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.state.Turn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChessGameRoomTest {

    @Test
    @DisplayName("종료상태가 아닌데 삭제하려하면 예외 발생")
    void checkEndTurn() {
        ChessGameRoom chessGameRoom = new ChessGameRoom(1L, "title", "password", Turn.WHITE_TURN);

        assertThatThrownBy(() -> chessGameRoom.checkCanDelete("password"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("삭제는 종료상태에서만 할 수 있습니다.");
    }

    @Test
    @DisplayName("입력된 패스워드와 다르면 예외 발생")
    void checkPassword() {
        ChessGameRoom chessGameRoom = new ChessGameRoom(1L, "title", "password", Turn.END);

        assertThatThrownBy(() -> chessGameRoom.checkCanDelete("errorPassword"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("입력된 비밀번호가 null인 경우 예외발생")
    void checkPasswordExceptionByNull() {
        ChessGameRoom chessGameRoom = new ChessGameRoom(1L, "title", "password", Turn.END);

        assertThatThrownBy(() -> chessGameRoom.checkCanDelete(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("password는 null이 들어올 수 없습니다.");
    }
}
