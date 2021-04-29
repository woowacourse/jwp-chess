package chess.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChessGameTest {

    @Test
    @DisplayName("킹이 죽으면 게임 끝")
    void killKing() {
        ChessGame chessGame = new ChessGame();

        chessGame.move(Position.of("b1"), Position.of("c3"));
        chessGame.move(Position.of("b7"), Position.of("b6"));
        chessGame.move(Position.of("c3"), Position.of("d5"));
        chessGame.move(Position.of("b6"), Position.of("b5"));
        chessGame.move(Position.of("d5"), Position.of("f6"));
        chessGame.move(Position.of("b5"), Position.of("b4"));
        chessGame.move(Position.of("f6"), Position.of("e8"));
        assertThat(chessGame.isOver()).isTrue();
    }

    @Test
    @DisplayName("턴이 아닐 때 오류 발생")
    void validateTurn() {
        ChessGame chessGame = new ChessGame(new ChessBoard(), Color.WHITE);
        assertThatThrownBy(() -> {
            chessGame.move(Position.of("b7"), Position.of("b6"));
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("자신의 턴을 확인하세요.");
    }

    @Test
    @DisplayName("수동으로 finish 입력 시 종료 확인")
    void isFinished() {
        ChessGame chessGame = new ChessGame(1, Color.WHITE, true, new ChessBoard(), "test");
        assertThat(chessGame.isFinished()).isTrue();
    }
}
