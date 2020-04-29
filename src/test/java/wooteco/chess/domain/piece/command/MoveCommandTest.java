package wooteco.chess.domain.piece.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import wooteco.chess.domain.command.InvalidCommandException;
import wooteco.chess.domain.command.MoveCommand;

public class MoveCommandTest {
	@DisplayName("유효한 Move 명령어가 들어왔을 때 정상적으로 객체 생성")
	@ParameterizedTest
	@ValueSource(strings = {"move b1 b2", "move c3 d4"})
	void constructor_ValidInput_Success(String command) {
		assertThat(new MoveCommand(command)).isInstanceOf(MoveCommand.class);
	}

	@DisplayName("유효하지 않은 Move 명령어가 들어왔을 때 예외 발생")
	@ParameterizedTest
	@ValueSource(strings = {"move b1 b2 b3", "move c3d4"})
	void constructor_InvalidInput_ExceptionThrown(String command) {
		assertThatThrownBy(() -> new MoveCommand(command))
			.isInstanceOf(InvalidCommandException.class)
			.hasMessage(InvalidCommandException.INVALID_MOVE_COMMAND);
	}
}
