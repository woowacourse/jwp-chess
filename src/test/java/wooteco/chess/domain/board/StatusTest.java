package wooteco.chess.domain.board;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StatusTest {

    @ParameterizedTest
    @DisplayName("white 턴인지 확인")
    @MethodSource("createWhiteStatus")
    void isWhiteTurn(Status status, boolean expected) {
        assertThat(status.isWhiteTurn()).isEqualTo(expected);
    }

    static Stream<Arguments> createWhiteStatus() {
        return Stream.of(
                Arguments.of(Status.from(0), true),
                Arguments.of(Status.from(1), false)
        );
    }

    @ParameterizedTest
    @DisplayName("black 턴인지 확인")
    @MethodSource("createBlackStatus")
    void isBlackTurn(Status status, boolean expected) {
        assertThat(status.isBlackTurn()).isEqualTo(expected);
    }

    static Stream<Arguments> createBlackStatus() {
        return Stream.of(
                Arguments.of(Status.from(0), false),
                Arguments.of(Status.from(1), true)
        );
    }

    @ParameterizedTest
    @DisplayName("processing이 아닌지 확인")
    @MethodSource("createNotProcessingStatus")
    void isNotProcessing(Status status, boolean expected) {
        assertThat(status.isNotProcessing()).isEqualTo(expected);
    }

    static Stream<Arguments> createNotProcessingStatus() {
        return Stream.of(
                Arguments.of(Status.initialStatus(), false),
                Arguments.of(Status.initialStatus().finish(), true)
        );
    }

    @ParameterizedTest
    @DisplayName("finish가 아닌지 확인")
    @MethodSource("createNotFinishedStatus")
    void isNotFinished(Status status, boolean expected) {
        assertThat(status.isNotFinished()).isEqualTo(expected);
    }

    static Stream<Arguments> createNotFinishedStatus() {
        return Stream.of(
                Arguments.of(Status.initialStatus(), true),
                Arguments.of(Status.initialStatus().finish(), false)
        );
    }

    @Test
    void nextTurn() {
        Status status = Status.initialStatus();
        int initTurn = status.getTurn();
        status = status.nextTurn();

        assertThat(status.getTurn()).isEqualTo(initTurn + 1);
    }

    @Test
    void finish() {
        Status status = Status.initialStatus();
        status = status.finish();

        assertThat(status.getStatusType()).isEqualTo(StatusType.FINISHED);
    }
}