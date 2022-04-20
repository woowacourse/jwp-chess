package springchess.model;

import chess.model.ChessCommand;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GameCommandTest {

    @ParameterizedTest
    @CsvSource(value = {"start:START", "end:END"}, delimiter = ':')
    void findCommand(String commandLine, chess.model.ChessCommand command) {
        List<String> commandInput = List.of(commandLine);

        assertThat(chess.model.ChessCommand.findCommand(commandInput)).isEqualTo(command);
    }

    @ParameterizedTest
    @ValueSource(strings = {"sTaRT", "End", "hello"})
    void throwInvalidCommand(String commandLine) {
        List<String> command = List.of(commandLine);

        assertThatThrownBy(() -> ChessCommand.findCommand(command))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(String.format("잘못된 커맨드입니다. %s", command));
    }
}
