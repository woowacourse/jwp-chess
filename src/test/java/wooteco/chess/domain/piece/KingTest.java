package wooteco.chess.domain.piece;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.chess.domain.piece.pieces.Pieces;
import wooteco.chess.domain.piece.pieces.TestPiecesFactory;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.PositionFactory;
import wooteco.chess.domain.position.positions.Positions;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class KingTest {

    @DisplayName("createMovablePositions 유효한 position입력시 정상 동작")
    @ParameterizedTest
    @ValueSource(strings = {"a1", "a2", "a3", "b1", "b3", "c1", "c2", "c3"})
    void createMovablePositions_normal_test(String input) {
        Position position = PositionFactory.of("b2");
        Piece king = TestPieceFactory.createKing(position, Color.WHITE);

        Positions positions = king.createMovablePositions(Collections.emptyList());
        assertThat(positions.getPositions()).contains(PositionFactory.of(input));
    }

    @DisplayName("createMovablePositions 코너 유효한 position입력시 정상 동작")
    @ParameterizedTest
    @ValueSource(strings = {"a2", "b1", "b2"})
    void createMovablePositions_normal_corner_test(String input) {
        Position position = PositionFactory.of("a1");
        Piece king = TestPieceFactory.createKing(position, Color.WHITE);

        assertThat(king.createMovablePositions(Collections.emptyList()).getPositions()).contains(PositionFactory.of(input));
    }

    @DisplayName("createMovablePositions 아군 말이 경로를 막고있는 경우 갈 수 있는 Position의 개수 반환 테스트")
    @Test
    void createMovablePositions_blocking_count_test() {
        Position position = PositionFactory.of("b2");
        Piece king = TestPieceFactory.createKing(position, Color.WHITE);

        Pieces pieces = TestPiecesFactory.of(Arrays.asList(
                PositionFactory.of("a3"),
                PositionFactory.of("c1")
        ));

        assertThat(king.createMovablePositions(pieces.getPieces()).getPositions().size()).isEqualTo(6);
    }

    @DisplayName("createMovablePositions 아군 말이 경로를 막고있는 경우 갈 수 있는 Position 반환 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"a1", "a2", "b1", "b3", "c2", "c3"})
    void createMovablePositions_blocking_test(String input) {
        Position position = PositionFactory.of("b2");
        Piece king = TestPieceFactory.createKing(position, Color.WHITE);

        Pieces pieces = TestPiecesFactory.of(Arrays.asList(
                PositionFactory.of("a3"),
                PositionFactory.of("c1")
        ));

        assertThat(king.createMovablePositions(pieces.getPieces()).getPositions()).contains(PositionFactory.of(input));
    }
}
