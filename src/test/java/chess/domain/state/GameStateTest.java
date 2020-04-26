package chess.domain.state;

import org.junit.jupiter.api.Test;

import static chess.domain.testAssistant.creationAssistant.*;
import static chess.domain.testAssistant.creationAssistant.createKing;
import static org.assertj.core.api.Assertions.assertThat;

class GameStateTest {

	@Test
	void pushCommand_Start() {
		Ended ended = createEnded();
		Started started = createStartedWithStartPieces();

		assertThat(ended.pushCommand("start")).isEqualTo(started);
	}

	@Test
	void pushCommand_Move() {
		Moved moved = createMoved(createPawn("white", "a2"),
				createKing("white", "c1"),
				createKing("black", "d1"));
		Moved expect = createMoved(createPawn("white", "a4"),
				createKing("white", "c1"),
				createKing("black", "d1"));

		assertThat(moved.pushCommand("move a2 a4")).isEqualTo(expect);
	}
}