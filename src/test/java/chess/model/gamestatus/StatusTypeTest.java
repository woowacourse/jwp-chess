package chess.model.gamestatus;

import static org.assertj.core.api.Assertions.assertThat;

import chess.model.board.Board;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StatusTypeTest {

    private static Stream<Arguments> provideNameAndExpectedClass() {
        return Stream.of(
                Arguments.of("End", End.class),
                Arguments.of("Ready", Ready.class),
                Arguments.of("Playing", Playing.class)
        );
    }

    private static Stream<Arguments> provideStatusAndExpectedType() {
        return Stream.of(
                Arguments.of(new End(Board.init()), StatusType.END),
                Arguments.of(new Ready(), StatusType.READY),
                Arguments.of(new Playing(Board.init()), StatusType.PLAYING)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNameAndExpectedClass")
    @DisplayName("상태 이름으로 상태 객체 만들기")
    void createStatus(String name, Class<? extends Status> expect) {
        Status actual = StatusType.createStatus(name, Board.init());
        Class<? extends Status> actualClass = actual.getClass();
        assertThat(actualClass).isEqualTo(expect);
    }

    @ParameterizedTest
    @MethodSource("provideStatusAndExpectedType")
    @DisplayName("상태로 상태 타입 찾아 반환하기")
    void findByStatus(Status status, StatusType expect) {
        StatusType actual = StatusType.findByStatus(status);
        assertThat(actual).isEqualTo(expect);
    }
}