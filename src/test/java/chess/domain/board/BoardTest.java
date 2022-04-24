package chess.domain.board;

import static chess.domain.board.position.File.A;
import static chess.domain.board.position.Rank.ONE;
import static chess.domain.board.position.Rank.SEVEN;
import static chess.domain.board.position.Rank.SIX;
import static chess.domain.board.position.Rank.TWO;
import static chess.domain.piece.Team.WHITE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.board.position.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.Team;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BoardTest {

    @Test
    @DisplayName("같은 팀 기물이 있는 위치로 이동하려고 하면 예외를 발생시킨다.")
    void moveExceptionBySameTeamPosition() {
        //given
        final Board board = new Board();
        final Position sourcePosition = Position.of(A, ONE);
        final Position targetPosition = Position.of(A, TWO);
        //when, then
        assertThatThrownBy(() -> board.movePiece(sourcePosition, targetPosition))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이동하려는 위치에 같은 팀 기물이 있습니다.");
    }

    @Test
    @DisplayName("턴에 맞지 않은 기물을 이동시키려 하면 예외를 발생시킨다.")
    void moveExceptionByInvalidTurnPiece() {
        //given
        final Board board = new Board();
        final Position blackTeamSourcePosition = Position.of(A, SEVEN);
        final Position targetPosition = Position.of(A, SIX);
        //when, then
        assertThatThrownBy(() -> board.movePiece(blackTeamSourcePosition, targetPosition))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("다른 팀 기물은 이동시킬 수 없습니다.");
    }

    @Test
    @DisplayName("정상적으로 기물이 이동하는지 테스트한다.")
    void move() {
        final Board board = new Board();
        final Board movedBoard = board.movePiece(Position.from("a2"), Position.from("a4"));
        final Map<Position, Piece> pieces = movedBoard.getPieces();
        final Piece piece = pieces.get(Position.from("a4"));
        final boolean actual = piece.isPawn();
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("정상적으로 기물이 상대방을 잡을 수 있는지 테스트한다.")
    void moveAttack() {
        Board board = new Board();
        board = board.movePiece(Position.from("a2"), Position.from("a4"));
        board = board.movePiece(Position.from("b7"), Position.from("b5"));
        board = board.movePiece(Position.from("a4"), Position.from("b5"));
        final Map<Position, Piece> pieces = board.getPieces();
        final Piece piece = pieces.get(Position.from("b5"));
        final boolean actual = piece.isTeamOf(WHITE);
        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @DisplayName("팀의 점수를 반환한다.")
    @CsvSource({"WHITE, 37", "BLACK, 37"})
    void getTotalScore(final Team team, final double expected) {
        //given
        Board board = new Board();
        board = board.movePiece(Position.from("a2"), Position.from("a4"));
        board = board.movePiece(Position.from("b7"), Position.from("b5"));
        board = board.movePiece(Position.from("a4"), Position.from("b5"));

        //when
        double actual = board.getTotalPoint(team);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("폰을 프로모션 할 수 있다.")
    void promotePawn() {
        //given
        Board board = new Board();
        board = board.movePiece(Position.from("f2"), Position.from("f4"));
        board = board.movePiece(Position.from("b7"), Position.from("b5"));
        board = board.movePiece(Position.from("f4"), Position.from("f5"));
        board = board.movePiece(Position.from("b5"), Position.from("b4"));
        board = board.movePiece(Position.from("f5"), Position.from("f6"));
        board = board.movePiece(Position.from("b4"), Position.from("b3"));
        board = board.movePiece(Position.from("f6"), Position.from("g7"));
        board = board.movePiece(Position.from("b3"), Position.from("c2"));
        board = board.movePiece(Position.from("g7"), Position.from("h8"));
        board = board.promotePawn(Position.from("h8"), "r");
        board = board.movePiece(Position.from("a7"), Position.from("a6"));
        board = board.movePiece(Position.from("h8"), Position.from("h7"));
        board = board.movePiece(Position.from("a6"), Position.from("a5"));
        board.movePiece(Position.from("h7"), Position.from("h3"));
    }

    @Test
    @DisplayName("프로모션 할 수 없는 기물을 프로모션 하려고 하면 예외를 발생시킨다.")
    void promoteException() {
        //given
        final Board board = new Board();
        final Position invalidPromotionPiecePosition = Position.from("a1");
        final String promotionType = "q";
        //when, then
        assertThatThrownBy(() -> board.promotePawn(invalidPromotionPiecePosition, promotionType))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 기물은 프로모션 할 수 없습니다.");
    }

    @Test
    @DisplayName("끝에 도달하지 않은 폰을 프로모션 하려고 하면 예외를 발생시킨다.")
    void a_Test() {
        //given
        final Board board = new Board();
        final Position invalidPromotionPiecePosition = Position.from("a2");
        final String promotionType = "q";
        //when, then
        assertThatThrownBy(() -> board.promotePawn(invalidPromotionPiecePosition, promotionType))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 기물은 프로모션 할 수 없습니다.");
    }
}
