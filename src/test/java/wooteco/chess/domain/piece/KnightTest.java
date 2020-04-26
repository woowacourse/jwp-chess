package wooteco.chess.domain.piece;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.chess.domain.piece.pieces.Pieces;
import wooteco.chess.domain.piece.pieces.TestPiecesFactory;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.PositionFactory;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class KnightTest {

    @DisplayName("createMovablePositions 유효한 position입력시 정상 동작")
    @ParameterizedTest
    @ValueSource(strings = {"a2", "a4", "b1", "b5", "d1", "d5", "e2", "e4"})
    void createMovablePositions_normal_test(String input) {
        Position position = PositionFactory.of("c3");
        Piece knight = TestPieceFactory.createKnight(position, Color.WHITE);

        assertThat(knight.createMovablePositions(Collections.emptyList()).getPositions()).contains(PositionFactory.of(input));
    }

    @DisplayName("createMovablePositions 유효한 코너 position입력시 정상 동작")
    @ParameterizedTest
    @ValueSource(strings = {"b3", "c2"})
    void createMovablePositions_corner_test(String input) {
        Position position = PositionFactory.of("a1");
        Piece knight = TestPieceFactory.createKnight(position, Color.WHITE);

        assertThat(knight.createMovablePositions(Collections.emptyList()).getPositions()).contains(PositionFactory.of(input));
    }

    @DisplayName("createMovablePositions 아군 말이 경로를 막고있는 경우 갈 수 있는 Position의 개수 반환 테스트")
    @Test
    void createMovablePositions_blocking_count_test() {
        Position position = PositionFactory.of("c3");
        Piece knight = TestPieceFactory.createKnight(position, Color.WHITE);

        Pieces pieces = TestPiecesFactory.of(Arrays.asList(
                PositionFactory.of("a2"),
                PositionFactory.of("e4")
        ));

        assertThat(knight.createMovablePositions(pieces.getPieces()).getPositions().size()).isEqualTo(6);
    }

    @DisplayName("createMovablePositions 아군 말이 경로를 막고있는 경우 갈 수 있는 Position 반환 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"a4", "b1", "b5", "d1", "d5", "e2"})
    void createMovablePositions_blocking_test(String input) {
        Position position = PositionFactory.of("c3");
        Piece knight = TestPieceFactory.createKnight(position, Color.WHITE);

        Pieces pieces = TestPiecesFactory.of(Arrays.asList(
                PositionFactory.of("a2"),
                PositionFactory.of("e4")
        ));

        assertThat(knight.createMovablePositions(pieces.getPieces()).getPositions()).contains(PositionFactory.of(input));
    }
}
