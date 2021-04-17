package chess.domain.board;

import static chess.utils.TestFixture.TEST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.board.move.MoveRequest;
import chess.domain.color.type.TeamColor;
import chess.domain.position.Position;
import java.sql.SQLException;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BoardTest {

    private static final String INITIAL_BOARD_STATUS = ""
        + "RNBQKBNR"
        + "PPPPPPPP"
        + "........"
        + "........"
        + "........"
        + "........"
        + "pppppppp"
        + "rnbqkbnr";

    @DisplayName("출발 위치에 자신의 기물이 없는 경우, 이동 불가 - 빈 칸인 경우")
    @Test
    void cannotMovePieceWhenStartPositionEmpty() {
        Board board = new Board(INITIAL_BOARD_STATUS);
        TeamColor currentTurnTeamColor = TeamColor.WHITE;
        Position startPosition = Position.of("a3");
        Position destination = Position.of("a4");
        MoveRequest moveRequest = new MoveRequest(currentTurnTeamColor, startPosition, destination);

        assertThatThrownBy(() -> board.movePiece(moveRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("출발 위치에 기물이 존재하지 않습니다.");
    }

    @DisplayName("출발 위치에 자신의 기물이 없는 경우, 이동 불가 - 적의 기물이 있는 경우")
    @Test
    void cannotMovePieceWhenStartPositionEnemyPiece() {
        Board board = new Board(INITIAL_BOARD_STATUS);
        TeamColor currentTurnTeamColor = TeamColor.WHITE;
        Position startPosition = Position.of("d7");
        Position destination = Position.of("d6");
        MoveRequest moveRequest = new MoveRequest(currentTurnTeamColor, startPosition, destination);

        assertThatThrownBy(() -> board.movePiece(moveRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("자신의 기물이 아닙니다.");
    }

    @DisplayName("King이 잡혔는지 확인")
    @Nested
    class KingDead {
        @DisplayName("King이 1개만 잡혔을 때")
        @Test
        void isOneKingDead() {
            Board board = new Board(""
                + ".KR....."
                + "P.PB...."
                + ".P..Q..."
                + "........"
                + ".....nq."
                + ".....p.p"
                + ".....pp."
                + "....r..."
            );

            assertThat(board.isKingDead()).isTrue();
        }

        @DisplayName("2개의 킹들이 모두 잡혔을 때")
        @Test
        void isAllKingsDead() {
            Board board = new Board(""
                + "..R....."
                + "P.PB...."
                + ".P..Q..."
                + "........"
                + ".....nq."
                + ".....p.p"
                + ".....pp."
                + "....r..."
            );

            assertThat(board.isKingDead()).isTrue();
        }

        @DisplayName("King이 잡히지 않았을 때")
        @Test
        void isNotKingDead() {
            Board board = new Board(""
                + ".KR....."
                + "P.PB...."
                + ".P..Q..."
                + "........"
                + ".....nq."
                + ".....p.p"
                + ".....pp."
                + "....rk.."
            );

            assertThat(board.isKingDead()).isFalse();
        }
    }
}