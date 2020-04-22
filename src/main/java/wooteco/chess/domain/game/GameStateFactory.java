package wooteco.chess.domain.game;

import java.util.Arrays;
import java.util.function.BiFunction;

import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.piece.Team;

public enum GameStateFactory {
	READY("ready", (turn, board) -> new Ready(BoardFactory.create(board), Team.of(turn))),
	STARTED("started", (turn, board) -> new Started(BoardFactory.create(board), Team.of(turn))),
	KING_CATCHED_FINISHED("kingCatchFinish",
		(turn, board) -> new KingCatchFinished(BoardFactory.create(board), Team.of(turn))),
	SUSPEND_FINISHED("suspendFinish",
		(turn, board) -> new SuspendFinished(BoardFactory.create(board), Team.of(turn)));

	private final String state;
	private final BiFunction<String, String, Game> gameStateGenerator;

	GameStateFactory(String state, BiFunction<String, String, Game> gameStateGenerator) {
		this.state = state;
		this.gameStateGenerator = gameStateGenerator;
	}

	public static Game of(String state, String turn, String board) {
		return Arrays.stream(values())
			.filter(val -> val.state.equals(state))
			.map(val -> val.gameStateGenerator.apply(turn, board))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않는 인자입니다."));
	}
}
