package wooteco.chess.domain.board;

import static wooteco.chess.domain.player.PlayerColor.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import wooteco.chess.domain.exception.InvalidMovementException;
import wooteco.chess.domain.piece.Bishop;
import wooteco.chess.domain.piece.EmptyPiece;
import wooteco.chess.domain.piece.GamePiece;
import wooteco.chess.domain.piece.Pawn;
import wooteco.chess.domain.piece.Queen;
import wooteco.chess.domain.piece.Rook;

class BoardTest {

    @ParameterizedTest
    @DisplayName("기물 이동")
    @MethodSource("createMovement")
    void move(GamePiece piece, String source, String target, int turn) {
        Map<Position, GamePiece> map = new HashMap<>(
                BoardFactory.EMPTY_BOARD.getBoard());
        map.put(Position.from(source), piece);
        Board board = Board.of(map);
        board = board.move(source, target);

        assertThat(board.getBoard().get(Position.from(source))).isEqualTo(EmptyPiece.getInstance());
        assertThat(board.getBoard().get(Position.from(target))).isEqualTo(piece);
    }

    static Stream<Arguments> createMovement() {
        return Stream.of(
                Arguments.of(new Rook(WHITE), "d3", "f3", 0),
                Arguments.of(new Rook(BLACK), "d3", "f3", 1),
                Arguments.of(new Pawn(WHITE), "d3", "d4", 0),
                Arguments.of(new Pawn(BLACK), "d3", "d2", 1)
        );
    }

    @Test
    @DisplayName("Black Pawn이 위로 올라갈 경우")
    void moveWithImpossiblePawnMovement() {
        Map<Position, GamePiece> map = new HashMap<>(
                BoardFactory.EMPTY_BOARD.getBoard());
        map.put(Position.from("d5"), new Pawn(BLACK));
        Board board = Board.of(map);

        assertThatThrownBy(() -> {
            board.move("d5", "d6");
        }).isInstanceOf(InvalidMovementException.class)
                .hasMessage("이동할 수 없습니다.\n이동할 수 없는 경로입니다.");
    }

    @Test
    @DisplayName("source 기물이 없는 경우")
    void moveWithEmptySource() {
        assertThatThrownBy(() -> {
            BoardFactory.createInitialBoard().move("d3", "d5");
        }).isInstanceOf(InvalidMovementException.class)
                .hasMessage("이동할 수 없습니다.\n기물이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("같은 팀 피스를 공격하는 경우")
    void samePlayerColor() {
        Position source = Position.from("d5");
        Position target = Position.from("d4");
        Map<Position, GamePiece> boardMap = new TreeMap<>(
                BoardFactory.EMPTY_BOARD.getBoard());
        GamePiece gamePiece = new Rook(BLACK);
        boardMap.put(source, gamePiece);
        boardMap.put(target, new Bishop(BLACK));

        Board board = BoardFactory.of(boardMap);

        assertThatThrownBy(() -> board.move("d5", "d4"))
                .isInstanceOf(InvalidMovementException.class)
                .hasMessage("이동할 수 없습니다.\n자신의 말은 잡을 수 없습니다.");
    }

    @ParameterizedTest
    @DisplayName("경로에 기물이 있는 경우")
    @MethodSource("createPathWithObstacle")
    void moveWithObstacle(GamePiece gamePiece, String source, String target) {
        Map<Position, GamePiece> map = new HashMap<>(
                BoardFactory.EMPTY_BOARD.getBoard());
        map.put(Position.from(source), gamePiece);
        map.put(Position.from("e3"), new Bishop(BLACK));
        Board board = Board.of(map);
        assertThatThrownBy(() -> {
            board.move(source, target);
        }).isInstanceOf(InvalidMovementException.class)
                .hasMessage("이동할 수 없습니다.\n경로에 기물이 존재합니다.");
    }

    static Stream<Arguments> createPathWithObstacle() {
        return Stream.of(
                Arguments.of(new Pawn(WHITE), "e2", "e4"),
                Arguments.of(new Bishop(WHITE), "d2", "f4"),
                Arguments.of(new Rook(WHITE), "e2", "e5"),
                Arguments.of(new Queen(WHITE), "e2", "e4")
        );
    }
}