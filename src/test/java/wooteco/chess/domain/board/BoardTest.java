package wooteco.chess.domain.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.chess.domain.piece.Pawn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Rook;
import wooteco.chess.domain.piece.Team;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BoardTest {

    @Test
    @DisplayName("Board 생성 테스트")
    void create() {
        Map<Position, Piece> board = new HashMap<>();
        assertThat(new Board(board, Team.WHITE)).isInstanceOf(Board.class);
    }

    @Test
    @DisplayName("해당 위치 말 찾기 테스트")
    void findPieceOn() {
        Board board = BoardFactory.create();
        assertThat(board.findPieceOn(Position.of(1, 1))).isInstanceOf(Rook.class);
    }

    @Test
    @DisplayName("말 이동 테스트")
    void move() {
        Board board = BoardFactory.create();
        Position start = Position.of(2, 1);
        Position end = Position.of(4, 1);
        board.move(start, end);
        assertThat(board.findPieceOn(Position.of(4, 1))).isInstanceOf(Pawn.class);
    }

    @Test
    @DisplayName("말 이동 테스트 - 차례가 아닐 때")
    void move_IfWrongTurn_ThrowException() {
        Board board = BoardFactory.create();
        Position start = Position.of(7, 1);
        Position end = Position.of(5, 1);
        assertThatThrownBy(() -> board.move(start, end)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("현재 차례가 아닙니다.");
    }

    @Test
    @DisplayName("말 이동 테스트 - 움직일 수 없을 때")
    void move_IfCantMove_ThrowException() {
        Board board = BoardFactory.create();
        Position start = Position.of(2, 1);
        Position end = Position.of(2, 2);
        assertThatThrownBy(() -> board.move(start, end)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("움직일 수 없습니다");
    }

    @Test
    @DisplayName("말들의 점수 총합 테스트")
    void getDefaultScore() {
        Board board = BoardFactory.create();
        board.move(Position.of(2, 1), Position.of(4, 1));
        board.move(Position.of(7, 2), Position.of(5, 2));
        board.move(Position.of(4, 1), Position.of(5, 2));

        assertThat(board.getDefaultScore(Team.BLACK)).isEqualTo(37);
    }

    @Test
    @DisplayName("Pawn 중첩 갯수 테스트")
    void countDuplicatedPawns() {
        Board board = BoardFactory.create();
        board.move(Position.of(2, 1), Position.of(4, 1));
        board.move(Position.of(7, 2), Position.of(5, 2));
        board.move(Position.of(4, 1), Position.of(5, 2));

        assertThat(board.countDuplicatedPawns(Team.WHITE)).isEqualTo(2);
    }

    @Test
    @DisplayName("왕 생존 여부 테스트")
    void hasKing() {
        Board board = BoardFactory.create();
        assertThat(board.hasKing(Team.WHITE)).isTrue();
    }

    @Test
    @DisplayName("이동 가능 위치 테스트")
    void findMovablePositions() {
        Board board = BoardFactory.create();
        List<Position> positions = board.findMovablePositions(Position.of(2, 1));
        assertThat(positions.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("이동 가능 위치 테스트 - 차례가 아닐 때")
    void findMovablePositions_IfNoMovable_ReturnEmptyList() {
        Board board = BoardFactory.create();
        List<Position> positions = board.findMovablePositions(Position.of(7, 1));
        assertThat(positions.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("현재 팀 테스트")
    void getCurrentTurn() {
        Board board = BoardFactory.create();
        board.move(Position.of(2, 1), Position.of(4, 1));
        board.move(Position.of(7, 2), Position.of(5, 2));
        board.move(Position.of(4, 1), Position.of(5, 2));

        assertThat(board.getCurrentTurn()).isEqualTo(Team.BLACK);
    }
}
