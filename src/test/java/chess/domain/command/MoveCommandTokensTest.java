package chess.domain.command;

import chess.domain.command.exceptions.MoveCommandTokensException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static chess.domain.testAssistant.creationAssistant.createMoveCommandTokens;

class MoveCommandTokensTest {
	private MoveCommandTokens moveCommandTokens;

	@BeforeEach
	void setUp() {
		moveCommandTokens = createMoveCommandTokens("move a1 a2");
	}

	@Test
	void GetSource() {
		assertThat(moveCommandTokens.getSource()).isEqualTo("a1");
	}

	@Test
	void getDestination() {
		assertThat(moveCommandTokens.getDestination()).isEqualTo("a2");
	}

	@Test
	void of_IfInvalidSize_ThrowException() {
		assertThatThrownBy(() -> createMoveCommandTokens("move a1 a2 a3"))
				.isInstanceOf(MoveCommandTokensException.class);
	}
}