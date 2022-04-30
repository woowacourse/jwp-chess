package chess.domain.room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.ChessGame;
import chess.domain.GameStatus;
import chess.domain.chessboard.ChessBoardFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoomTest {

    @Test
    @DisplayName("게임이 진행중이 아니고, 비밀번호가 일치하면 방을 삭제 할 수 있다.")
    void canRemove() {
        // given
        final ChessGame chessGame = new ChessGame(ChessBoardFactory.createChessBoard(), GameStatus.END);
        final String plainPassword = "1234";
        final Room room = new Room("test", plainPassword, chessGame);

        // when
        final boolean actual = room.canRemove(plainPassword);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("게임이 진행중이면 방을 삭제 할 수 없다.")
    void canRemove_isPlaying() {
        // given
        final ChessGame chessGame = new ChessGame(ChessBoardFactory.createChessBoard(), GameStatus.PLAYING);
        final String plainPassword = "1234";
        final Room room = new Room("test", plainPassword, chessGame);

        // then
        assertThatThrownBy(() -> room.canRemove(plainPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게임이 진행 중입니다.");
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 방을 삭제 할 수 없다.")
    void canRemove_password() {
        // given
        final ChessGame chessGame = new ChessGame(ChessBoardFactory.createChessBoard(), GameStatus.END);
        final String plainPassword = "1234";
        final Room room = new Room("test", plainPassword, chessGame);

        // then
        assertThatThrownBy(() -> room.canRemove(plainPassword + "!"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }
}