package chess.domain.piece.detail;

import static chess.domain.piece.detail.Direction.E;
import static chess.domain.piece.detail.Direction.ENE;
import static chess.domain.piece.detail.Direction.ESE;
import static chess.domain.piece.detail.Direction.N;
import static chess.domain.piece.detail.Direction.NE;
import static chess.domain.piece.detail.Direction.NNE;
import static chess.domain.piece.detail.Direction.NNW;
import static chess.domain.piece.detail.Direction.NW;
import static chess.domain.piece.detail.Direction.S;
import static chess.domain.piece.detail.Direction.SE;
import static chess.domain.piece.detail.Direction.SSE;
import static chess.domain.piece.detail.Direction.SSW;
import static chess.domain.piece.detail.Direction.SW;
import static chess.domain.piece.detail.Direction.W;
import static chess.domain.piece.detail.Direction.WNW;
import static chess.domain.piece.detail.Direction.WSW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import chess.domain.square.Square;

class DirectionTest {

    @Test
    void getWhitePawnDirections() {
        assertThat(Direction.getWhitePawnDirections())
                .isEqualTo(List.of(N));
    }

    @Test
    void getBlackPawnDirections() {
        assertThat(Direction.getBlackPawnDirections())
                .isEqualTo(List.of(S));
    }

    @Test
    void getWhitePawnAttackDirections() {
        assertThat(Direction.getWhitePawnAttackDirections())
                .isEqualTo(List.of(NW, NE));
    }

    @Test
    void getBlackPawnAttackDirections() {
        assertThat(Direction.getBlackPawnAttackDirections())
                .isEqualTo(List.of(SW, SE));
    }

    @Test
    void getEveryDirections() {
        assertThat(Direction.getEveryDirections())
                .isEqualTo(List.of(N, NE, E, SE, S, SW, W, NW));
    }

    @Test
    void getVerticalAndHorizontalDirections() {
        assertThat(Direction.getVerticalAndHorizontalDirections())
                .isEqualTo(List.of(N, E, S, W));
    }

    @Test
    void getDiagonalDirections() {
        assertThat(Direction.getDiagonalDirections())
                .isEqualTo(List.of(NW, NE, SE, SW));

    }

    @Test
    void getKnightDirections() {
        assertThat(Direction.getKnightDirections())
                .isEqualTo(List.of(NNE, ENE, ESE, SSE, SSW, WSW, WNW, NNW));

    }

    @ParameterizedTest
    @CsvSource({
            "N,0", "S,0", "E,1", "W,-1",
            "NE,1", "SE,1", "SW,-1", "NW,-1",
            "NNE,1", "ENE,2", "ESE,2", "SSE,1",
            "SSW,-1", "WSW,-2", "WNW,-2", "NNW,-1",
            "NONE,0"
    })
    void getXDegree(final String rawDirection, final int xDegree) {
        Direction direction = Direction.valueOf(rawDirection);
        assertThat(direction.getXDegree()).isEqualTo(xDegree);
    }

    @ParameterizedTest
    @CsvSource({
            "N,1", "S,-1", "E,0", "W,0",
            "NE,1", "SE,-1", "SW,-1", "NW,1",
            "NNE,2", "ENE,1", "ESE,-1", "SSE,-2",
            "SSW,-2", "WSW,-1", "WNW,1", "NNW,2",
            "NONE,0"
    })
    void getYDegree(final String rawDirection, final int yDegree) {
        Direction direction = Direction.valueOf(rawDirection);
        assertThat(direction.getYDegree()).isEqualTo(yDegree);
    }

    @ParameterizedTest
    @CsvSource({
            "a1,a3,N", "a1,c3,NE", "a1,c1,E", "a3,c1,SE",
            "a3,a1,S", "c3,a1,SW", "c3,a3,W", "c1,a3,NW"
    })
    void findDirection(final String rawFrom, final String rawTo, final String rawDirection) {
        final Square from = Square.from(rawFrom);
        final Square to = Square.from(rawTo);
        final Direction direction = Direction.valueOf(rawDirection);

        assertThat(Direction.findDirection(from, to)).isSameAs(direction);
    }

    @ParameterizedTest
    @CsvSource({"c4,e3", "c4,a1", "c4,h8", "a1,h2", "a1,b8"})
    void invalidDirectionException(final String rawFrom, final String rawTo) {
        final Square from = Square.from(rawFrom);
        final Square to = Square.from(rawTo);

        assertThatThrownBy(() -> Direction.findDirection(from, to))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 위치로의 방향을 찾을 수 없습니다.");
    }
}
