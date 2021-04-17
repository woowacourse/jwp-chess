package chess.domain.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.board.move.MoveRequest;
import chess.domain.color.type.TeamColor;
import chess.domain.position.Position;
import org.junit.jupiter.api.DisplayName;
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
}